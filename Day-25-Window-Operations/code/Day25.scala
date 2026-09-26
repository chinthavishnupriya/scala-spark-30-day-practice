import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

object Day25 {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf()
      .setAppName("Day 25 - Window Operations")
      .setMaster("local[4]")

    val ssc = new StreamingContext(conf, Seconds(5))
    ssc.checkpoint("output/checkpoint")

    ssc.sparkContext.setLogLevel("WARN")

    println("==============================================")
    println("DAY 25 - WINDOW OPERATIONS")
    println("==============================================")
    println("Batch interval : 5 seconds")
    println("Window size    : 20 seconds")
    println("Slide interval : 10 seconds")
    println("Listening on   : localhost:9999")
    println()

    val lines = ssc.socketTextStream("localhost", 9999)

    /*
     * Input format:
     * ACCOUNT_ID,AMOUNT
     *
     * Example:
     * ACC001,100
     * ACC002,250
     */

    val transactions = lines.flatMap { line =>
      val parts = line.split(",").map(_.trim)

      if (parts.length == 2) {
        try {
          Some((parts(0), parts(1).toDouble))
        } catch {
          case _: NumberFormatException => None
        }
      } else {
        None
      }
    }

    /*
     * countByWindow:
     * Counts the total number of transactions
     * inside the current 20-second window.
     *
     * The window slides every 10 seconds.
     */
    val transactionCountWindow =
      transactions.countByWindow(
        Seconds(20),
        Seconds(10)
      )

    transactionCountWindow.foreachRDD { (rdd, time) =>
      if (!rdd.isEmpty()) {
        println()
        println("----------------------------------------------")
        println(s"COUNT BY WINDOW - $time")
        println(
          s"Transactions in last 20 seconds: ${rdd.collect().head}"
        )
        println("----------------------------------------------")
      }
    }

    /*
     * reduceByKeyAndWindow:
     * Calculates rolling sales totals for each account.
     */
    val rollingSales =
      transactions.reduceByKeyAndWindow(
        (a: Double, b: Double) => a + b,
        Seconds(20),
        Seconds(10)
      )

    rollingSales.foreachRDD { (rdd, time) =>
      println()
      println("----------------------------------------------")
      println(s"REDUCE BY KEY AND WINDOW - $time")

      if (rdd.isEmpty()) {
        println("No transactions in current window.")
      } else {
        rdd.collect().sortBy(_._1).foreach {
          case (account, amount) =>
            println(
              f"$account -> rolling sales = $amount%.2f"
            )
        }
      }

      println("----------------------------------------------")
    }

    /*
     * Detect a sudden increase in transactions.
     *
     * For this demonstration, an account is flagged when
     * it has at least 3 transactions inside the window.
     */
    val burstDetection =
      transactions
        .map { case (account, _) => (account, 1) }
        .reduceByKeyAndWindow(
          (a: Int, b: Int) => a + b,
          Seconds(20),
          Seconds(10)
        )

    burstDetection.foreachRDD { (rdd, time) =>
      val bursts = rdd.filter {
        case (_, count) => count >= 3
      }

      if (!bursts.isEmpty()) {
        println()
        println("----------------------------------------------")
        println(s"TRANSACTION BURST DETECTION - $time")

        bursts.collect().sortBy(_._1).foreach {
          case (account, count) =>
            println(
              s"ALERT: $account -> $count transactions in window"
            )
        }

        println("----------------------------------------------")
      }
    }

    ssc.start()

    println("StreamingContext started.")
    println("Send transactions using a TCP client on port 9999.")
    println("Press Ctrl+C to stop.")

    ssc.awaitTermination()
  }
}
