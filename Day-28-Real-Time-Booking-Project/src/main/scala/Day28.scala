import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.sql.SparkSession

case class BookingEvent(
  bookingId: String,
  customerId: String,
  eventType: String,
  timestamp: String,
  routeId: String,
  seats: Int
)

case class RouteReference(
  routeName: String,
  capacity: Int,
  mode: String
)

case class BookingState(
  bookedSeats: Int,
  cancelledSeats: Int
)

object Day28 {
  private val Port = 9997
  private val BatchSeconds = 5
  private val WindowSeconds = 20
  private val SlideSeconds = 10

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setAppName("Day 28 - Real-Time Booking Project")
      .setMaster("local[4]")

    val ssc = new StreamingContext(conf, Seconds(BatchSeconds))
    ssc.checkpoint("output/checkpoint")
    ssc.sparkContext.setLogLevel("WARN")

    val spark = SparkSession.builder().config(ssc.sparkContext.getConf).getOrCreate()
    import spark.implicits._

    val routeReference = Map(
      "R001" -> RouteReference("Hyderabad-Warangal", 40, "BUS"),
      "R002" -> RouteReference("Hyderabad-Vijayawada", 50, "BUS"),
      "R003" -> RouteReference("Hyderabad-Tirupati", 45, "TRAIN")
    )

    val routeBroadcast = ssc.sparkContext.broadcast(routeReference)
    val lines = ssc.socketTextStream("localhost", Port)

    val events = lines.flatMap { line =>
      val p = line.split(",").map(_.trim)
      if (p.length == 6 && (p(2) == "BOOK" || p(2) == "CANCEL")) {
        try {
          val seats = p(5).toInt
          if (seats > 0) Some(BookingEvent(p(0), p(1), p(2), p(3), p(4), seats))
          else None
        } catch {
          case _: NumberFormatException => None
        }
      } else None
    }

    val stateUpdates = events.map { event =>
      val delta =
        if (event.eventType == "BOOK") (event.seats, 0)
        else (0, event.seats)
      (event.routeId, delta)
    }

    val bookingState = stateUpdates.updateStateByKey[BookingState] {
      (updates: Seq[(Int, Int)], current: Option[BookingState]) =>
        val old = current.getOrElse(BookingState(0, 0))
        val booked = old.bookedSeats + updates.map(_._1).sum
        val cancelled = old.cancelledSeats + updates.map(_._2).sum
        Some(BookingState(booked, cancelled))
    }

    val bookingPairs = events
      .filter(_.eventType == "BOOK")
      .map(event => (event.routeId, 1))
      .reduceByKeyAndWindow(
        (a: Int, b: Int) => a + b,
        Seconds(WindowSeconds),
        Seconds(SlideSeconds)
      )

    bookingPairs.foreachRDD { (rdd, time) =>
      if (!rdd.isEmpty()) {
        println("\n----------------------------------------------")
        println("ROLLING BOOKING COUNT - " + time)
        rdd.sortByKey().collect().foreach { case (route, count) =>
          println(s"$route -> $count bookings in $WindowSeconds seconds")
        }
        println("----------------------------------------------")
      }
    }

    bookingState.foreachRDD { (rdd, time) =>
      if (!rdd.isEmpty()) {
        val references = routeBroadcast.value

        val reportRows = rdd.map { case (routeId, state) =>
          val ref = references.getOrElse(routeId, RouteReference("Unknown", 0, "UNKNOWN"))
          val occupied = math.max(0, state.bookedSeats - state.cancelledSeats)
          val available = math.max(0, ref.capacity - occupied)
          (routeId, ref.routeName, ref.mode, ref.capacity, state.bookedSeats,
            state.cancelledSeats, occupied, available)
        }

        val report = reportRows.toDF(
          "routeId", "routeName", "mode", "capacity", "bookedSeats",
          "cancelledSeats", "occupiedSeats", "availableSeats"
        )

        report.createOrReplaceTempView("booking_report")

        val sqlReport = spark.sql(
          """
            SELECT
              routeId,
              routeName,
              mode,
              capacity,
              bookedSeats,
              cancelledSeats,
              occupiedSeats,
              availableSeats,
              ROUND((occupiedSeats * 100.0) / capacity, 2) AS occupancyPercent
            FROM booking_report
            ORDER BY routeId
          """
        )

        println("\n==============================================")
        println("SPARK SQL BOOKING REPORT - " + time)
        println("==============================================")
        sqlReport.show(false)
      }
    }

    println("==============================================")
    println("DAY 28 - REAL-TIME BOOKING PROJECT")
    println("==============================================")
    println(s"Batch interval : $BatchSeconds seconds")
    println(s"Window size    : $WindowSeconds seconds")
    println(s"Slide interval : $SlideSeconds seconds")
    println("Socket server  : localhost:" + Port)
    println("State model    : cumulative BOOK/CANCEL seats by route")
    println("Reference data : broadcast route capacity/mode")
    println("Reporting      : Spark SQL")
    println()

    ssc.start()
    println("StreamingContext started.")
    println("Send booking events to port " + Port + ".")
    println("Press Ctrl+C to stop.")
    ssc.awaitTermination()
  }
}
