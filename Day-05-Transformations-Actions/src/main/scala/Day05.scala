import org.apache.spark.sql.SparkSession

object Day05 {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .appName("Day 5 - Transformations and Actions")
      .master("local[4]")
      .getOrCreate()

    val sc = spark.sparkContext

    println("========================================")
    println("DAY 05 - TRANSFORMATIONS AND ACTIONS")
    println("========================================")

    println(s"Spark version      : ${spark.version}")
    println(s"Master             : ${sc.master}")
    println(s"Default parallelism: ${sc.defaultParallelism}")

    // ------------------------------------------------
    // 1. READ LOG FILE
    // ------------------------------------------------
    println()
    println("--- 1. READ LOG FILE ---")

    val logs = sc.textFile("input/application.log", 4)

    println(s"Total log records  : ${logs.count()}")
    println(s"Number of partitions: ${logs.getNumPartitions}")

    // ------------------------------------------------
    // 2. MAP TRANSFORMATION
    // ------------------------------------------------
    println()
    println("--- 2. MAP TRANSFORMATION ---")

    val logLevels = logs.map { line =>
      line.split(" ")(2)
    }

    println("Log levels:")
    logLevels.collect().foreach(println)

    // ------------------------------------------------
    // 3. FILTER TRANSFORMATION
    // ------------------------------------------------
    println()
    println("--- 3. FILTER TRANSFORMATION ---")

    val errorLogs = logs.filter { line =>
      line.contains(" ERROR ")
    }

    println("ERROR log messages:")
    errorLogs.collect().foreach(println)

    // ------------------------------------------------
    // 4. FLATMAP TRANSFORMATION
    // ------------------------------------------------
    println()
    println("--- 4. FLATMAP TRANSFORMATION ---")

    val words = logs.flatMap { line =>
      line.split(" ")
    }

    println(s"Total words produced: ${words.count()}")

    println("First 10 words:")
    words.take(10).foreach(println)

    // ------------------------------------------------
    // 5. DISTINCT TRANSFORMATION
    // ------------------------------------------------
    println()
    println("--- 5. DISTINCT TRANSFORMATION ---")

    val uniqueLogLevels = logLevels.distinct()

    println("Unique log levels:")
    uniqueLogLevels.collect().sorted.foreach(println)

    // ------------------------------------------------
    // 6. UNION TRANSFORMATION
    // ------------------------------------------------
    println()
    println("--- 6. UNION TRANSFORMATION ---")

    val infoLogs = logs.filter(_.contains(" INFO "))
    val warnLogs = logs.filter(_.contains(" WARN "))

    val combinedLogs = infoLogs.union(warnLogs)

    println(s"INFO records       : ${infoLogs.count()}")
    println(s"WARN records       : ${warnLogs.count()}")
    println(s"Combined records   : ${combinedLogs.count()}")

    // ------------------------------------------------
    // 7. COUNT ACTION
    // ------------------------------------------------
    println()
    println("--- 7. COUNT ACTION ---")

    val errorCount = errorLogs.count()

    println(s"Total ERROR messages: $errorCount")

    // ------------------------------------------------
    // 8. COLLECT ACTION
    // ------------------------------------------------
    println()
    println("--- 8. COLLECT ACTION ---")

    val collectedErrors = errorLogs.collect()

    println("Collected ERROR messages:")
    collectedErrors.foreach(println)

    // ------------------------------------------------
    // 9. FIRST ACTION
    // ------------------------------------------------
    println()
    println("--- 9. FIRST ACTION ---")

    println(s"First log: ${logs.first()}")

    // ------------------------------------------------
    // 10. TAKE ACTION
    // ------------------------------------------------
    println()
    println("--- 10. TAKE ACTION ---")

    println("First 3 logs:")
    logs.take(3).foreach(println)

    // ------------------------------------------------
    // 11. REDUCE ACTION
    // ------------------------------------------------
    println()
    println("--- 11. REDUCE ACTION ---")

    val numbers = sc.parallelize(1 to 10, 4)

    val sum = numbers.reduce(_ + _)

    println(s"Numbers: 1 to 10")
    println(s"Sum using reduce: $sum")

    // ------------------------------------------------
    // 12. ERROR LOG ANALYZER
    // ------------------------------------------------
    println()
    println("--- 12. ERROR LOG ANALYZER ---")

    val errorAnalyzer = logs
      .filter(_.contains(" ERROR "))
      .map { line =>
        line.split(" ERROR ", 2)(1)
      }

    println(s"ERROR count: ${errorAnalyzer.count()}")

    println("ERROR details:")
    errorAnalyzer.collect().foreach(error => println(s"- $error"))

    // ------------------------------------------------
    // 13. TRANSFORMATIONS VS ACTIONS
    // ------------------------------------------------
    println()
    println("--- 13. TRANSFORMATIONS VS ACTIONS ---")

    println("Transformations:")
    println("map, filter, flatMap, distinct, union")

    println()
    println("Actions:")
    println("count, collect, first, take, reduce")

    // ------------------------------------------------
    // 14. LAZY EVALUATION
    // ------------------------------------------------
    println()
    println("--- 14. LAZY EVALUATION ---")

    val lazyExample = logs
      .filter(_.contains(" ERROR "))
      .map(_.toUpperCase)

    println("Transformation chain created.")
    println("No Spark computation is triggered until an action is called.")

    val lazyResult = lazyExample.count()

    println(s"Action count result: $lazyResult")

    println()
    println("========================================")
    println("DAY 05 APPLICATION COMPLETED")
    println("========================================")

    spark.stop()
  }
}
