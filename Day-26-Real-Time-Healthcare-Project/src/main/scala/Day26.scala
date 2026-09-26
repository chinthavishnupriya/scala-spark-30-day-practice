import org.apache.spark.SparkConf
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.{Seconds, StreamingContext}

case class PatientVital(
    patientId: String,
    timestamp: String,
    heartRate: Double,
    temperature: Double,
    spo2: Double
)

case class VitalThresholds(
    maxHeartRate: Double,
    maxTemperature: Double,
    minSpo2: Double
)

object Day26 {

  def main(args: Array[String]): Unit = {

    // --------------------------------------------------
    // Spark Configuration
    // --------------------------------------------------

    val conf = new SparkConf()
      .setAppName("Day 26 - Real-Time Healthcare Project")
      .setMaster("local[4]")

    // --------------------------------------------------
    // Streaming Context
    // --------------------------------------------------

    val ssc =
      new StreamingContext(conf, Seconds(5))

    // Required for stateful/window operations
    ssc.checkpoint("output/checkpoint")

    ssc.sparkContext.setLogLevel("WARN")

    println("==============================================")
    println("DAY 26 - REAL-TIME HEALTHCARE PROJECT")
    println("==============================================")
    println("Batch interval : 5 seconds")
    println("Window size    : 20 seconds")
    println("Slide interval : 10 seconds")
    println("Socket server  : localhost:9999")
    println()

    // --------------------------------------------------
    // Broadcast Variable
    // --------------------------------------------------

    val thresholds = VitalThresholds(
      maxHeartRate = 120.0,
      maxTemperature = 38.0,
      minSpo2 = 94.0
    )

    val broadcastThresholds =
      ssc.sparkContext.broadcast(thresholds)

    // --------------------------------------------------
    // Accumulator
    // --------------------------------------------------

    val abnormalVitalAccumulator =
      ssc.sparkContext.longAccumulator(
        "AbnormalVitalRecords"
      )

    // --------------------------------------------------
    // Socket Input Stream
    // --------------------------------------------------

    val lines =
      ssc.socketTextStream(
        "localhost",
        9999
      )

    // --------------------------------------------------
    // Parse Patient Vital Events
    //
    // Input format:
    // patientId,timestamp,heartRate,temperature,spo2
    // --------------------------------------------------

    val vitals =
      lines.flatMap { line =>

        val parts =
          line.split(",").map(_.trim)

        if (parts.length == 5) {

          try {

            Some(
              PatientVital(
                patientId = parts(0),
                timestamp = parts(1),
                heartRate = parts(2).toDouble,
                temperature = parts(3).toDouble,
                spo2 = parts(4).toDouble
              )
            )

          } catch {

            case _: NumberFormatException =>
              None
          }

        } else {

          None
        }
      }

    // --------------------------------------------------
    // Abnormal Vital Detection
    //
    // Conditions:
    // HR > 120
    // Temperature > 38
    // SpO2 < 94
    // --------------------------------------------------

    val abnormalVitals =
      vitals
        .filter { vital =>

          val limit =
            broadcastThresholds.value

          val abnormal =
            vital.heartRate > limit.maxHeartRate ||
            vital.temperature > limit.maxTemperature ||
            vital.spo2 < limit.minSpo2

          if (abnormal) {
            abnormalVitalAccumulator.add(1)
          }

          abnormal
        }
        .persist(StorageLevel.MEMORY_ONLY)

    // --------------------------------------------------
    // Immediate Abnormal Vital Alerts
    // --------------------------------------------------

    abnormalVitals.foreachRDD { (rdd, time) =>

      println()
      println("----------------------------------------------")
      println(s"ABNORMAL VITAL ALERTS - $time")

      if (rdd.isEmpty()) {

        println(
          "No abnormal vital readings in this batch."
        )

      } else {

        rdd.collect()
          .sortBy(_.patientId)
          .foreach { vital =>

            println(
              f"ALERT: ${vital.patientId} | " +
              f"HR=${vital.heartRate}%.1f | " +
              f"Temp=${vital.temperature}%.1f | " +
              f"SpO2=${vital.spo2}%.1f"
            )
          }
      }

      println("----------------------------------------------")
    }

    // --------------------------------------------------
    // Repeated Abnormal Readings
    //
    // Window: 20 seconds
    // Slide : 10 seconds
    //
    // A patient is flagged when they have
    // at least 2 abnormal readings in the window.
    // --------------------------------------------------

    val repeatedAbnormal =
      abnormalVitals
        .map(vital => (vital.patientId, 1))
        .reduceByKeyAndWindow(
          (a: Int, b: Int) => a + b,
          Seconds(20),
          Seconds(10)
        )

    // --------------------------------------------------
    // Repeated Abnormal Reading Alerts
    // --------------------------------------------------

    repeatedAbnormal.foreachRDD { (rdd, time) =>

      val repeated =
        rdd.filter {
          case (_, count) =>
            count >= 2
        }

      if (!repeated.isEmpty()) {

        println()
        println("----------------------------------------------")
        println(
          s"REPEATED ABNORMAL READING ALERT - $time"
        )

        repeated.collect()
          .sortBy(_._1)
          .foreach {

            case (patientId, count) =>

              println(
                s"REPEATED ALERT: $patientId -> " +
                s"$count abnormal readings in window"
              )
          }

        println("----------------------------------------------")
      }
    }

    // --------------------------------------------------
    // Vital Stream Statistics
    // --------------------------------------------------

    vitals.foreachRDD { (rdd, time) =>

      if (!rdd.isEmpty()) {

        println()
        println("----------------------------------------------")
        println(s"VITAL STREAM - $time")

        println(
          s"Patients processed: ${rdd.count()}"
        )

        println(
          s"Total abnormal records: " +
          s"${abnormalVitalAccumulator.value}"
        )

        println("----------------------------------------------")
      }
    }

    // --------------------------------------------------
    // Start Streaming
    // --------------------------------------------------

    ssc.start()

    println("StreamingContext started.")
    println("Send patient vital events to port 9999.")
    println("Press Ctrl+C to stop.")

    // Keep the application running
    ssc.awaitTermination()
  }
}
