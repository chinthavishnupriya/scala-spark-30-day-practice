import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

object Day23 {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf()
      .setAppName("Day 23 - DStreams Basics")
      .setMaster("local[4]")

    val ssc = new StreamingContext(conf, Seconds(5))

    ssc.sparkContext.setLogLevel("WARN")

    println("==============================================")
    println("DAY 23 - DSTREAMS BASICS")
    println("==============================================")
    println("Batch interval: 5 seconds")
    println("Waiting for log stream on localhost:9999")
    println()

    val lines = ssc.socketTextStream("localhost", 9999)

    // flatMap: split each incoming log line into words
    val words = lines
      .flatMap(_.split("\\s+"))
      .filter(_.nonEmpty)

    // Execute flatMap and show the number of generated words
    words.foreachRDD { (rdd, time) =>
      if (!rdd.isEmpty()) {
        println(
          s"Words produced by flatMap in micro-batch $time: ${rdd.count()}"
        )
      }
    }

    // filter: keep only ERROR log lines
    val errorLines = lines.filter(_.contains("ERROR"))

    // map: convert every ERROR line into a key-value pair
    val errorPairs = errorLines.map(_ => ("ERROR", 1))

    // reduceByKey: count ERROR messages in each micro-batch
    val errorCounts = errorPairs.reduceByKey(_ + _)

    errorCounts.foreachRDD { (rdd, time) =>
      println()
      println("----------------------------------------------")
      println(s"Micro-batch time: $time")

      if (rdd.isEmpty()) {
        println("ERROR count: 0")
      } else {
        rdd.collect().foreach {
          case (level, count) =>
            println(s"$level count: $count")
        }
      }

      println("----------------------------------------------")
    }

    // map: convert complete log lines to uppercase
    val upperCaseLines = lines.map(_.toUpperCase)

    upperCaseLines.foreachRDD { (rdd, time) =>
      if (!rdd.isEmpty()) {
        println(
          s"Processed ${rdd.count()} log line(s) in micro-batch $time"
        )
      }
    }

    ssc.start()

    println("StreamingContext started.")
    println("Send log lines to port 9999.")
    println("Press Ctrl+C to stop.")

    ssc.awaitTermination()
  }
}
