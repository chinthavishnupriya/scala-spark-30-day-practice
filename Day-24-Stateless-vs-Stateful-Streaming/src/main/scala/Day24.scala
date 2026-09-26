import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

object Day24 {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf()
      .setAppName("Day 24 - Stateless vs Stateful Streaming")
      .setMaster("local[4]")

    val ssc = new StreamingContext(conf, Seconds(5))

    ssc.sparkContext.setLogLevel("WARN")

    // updateStateByKey requires checkpointing.
    ssc.checkpoint("output/checkpoint")

    println("==============================================")
    println("DAY 24 - STATELESS VS STATEFUL STREAMING")
    println("==============================================")
    println("Batch interval: 5 seconds")
    println("Waiting for bank transaction stream on localhost:9999")
    println()
    println("Input format: ACCOUNT_ID,AMOUNT")
    println("Example: ACC001,250")
    println()

    val lines = ssc.socketTextStream("localhost", 9999)

    // ------------------------------------------------
    // Parse incoming transactions
    // ------------------------------------------------

    val transactions = lines.flatMap { line =>
      val parts = line.split(",")

      if (parts.length == 2) {
        try {
          Some((parts(0).trim, parts(1).trim.toDouble))
        } catch {
          case _: NumberFormatException => None
        }
      } else {
        None
      }
    }

    // ------------------------------------------------
    // STATELESS PROCESSING
    // Count transactions only in the current batch.
    // Previous batches are not included.
    // ------------------------------------------------

    val currentBatchCounts = transactions
      .map {
        case (account, _) => (account, 1)
      }
      .reduceByKey(_ + _)

    currentBatchCounts.foreachRDD { (rdd, time) =>

      println()
      println("----------------------------------------------")
      println(s"STATELESS RESULT - Micro-batch: $time")

      if (rdd.isEmpty()) {
        println("No transactions in this batch.")
      } else {
        rdd.collect().sortBy(_._1).foreach {
          case (account, count) =>
            println(s"$account -> $count transaction(s) in current batch")
        }
      }

      println("----------------------------------------------")
    }

    // ------------------------------------------------
    // STATEFUL PROCESSING
    // Maintain cumulative transaction counts.
    // ------------------------------------------------

    val runningCounts = transactions
      .map {
        case (account, _) => (account, 1)
      }
      .updateStateByKey[Int] {
        (newValues: Seq[Int], previousState: Option[Int]) =>
          Some(previousState.getOrElse(0) + newValues.sum)
      }

    runningCounts.foreachRDD { (rdd, time) =>

      println()
      println("**********************************************")
      println(s"STATEFUL RESULT - Micro-batch: $time")

      if (rdd.isEmpty()) {
        println("No accumulated state available yet.")
      } else {
        rdd.collect().sortBy(_._1).foreach {
          case (account, count) =>
            println(s"$account -> $count transaction(s) accumulated")
        }
      }

      println("**********************************************")
    }

    // ------------------------------------------------
    // Start StreamingContext
    // ------------------------------------------------

    ssc.start()

    println("StreamingContext started.")
    println("Send transactions to port 9999.")
    println("Press Ctrl+C to stop.")

    ssc.awaitTermination()
  }
}
