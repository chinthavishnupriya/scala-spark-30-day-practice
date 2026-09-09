import org.apache.spark.sql.SparkSession

object Day08 {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .appName("Day 8 - DAG and Spark Execution")
      .master("local[4]")
      .config("spark.serializer", "org.apache.spark.serializer.JavaSerializer")
      .getOrCreate()

    spark.sparkContext.setLogLevel("WARN")

    println("========================================")
    println("DAY 08 - DAG AND SPARK EXECUTION")
    println("========================================")

    val inputPath = "input/sales.txt"

    // --------------------------------------------------
    // 1. Create the initial RDD
    // --------------------------------------------------
    val salesRDD = spark.sparkContext.textFile(inputPath, 4)

    println(s"\nOriginal RDD partitions : ${salesRDD.getNumPartitions}")

    // --------------------------------------------------
    // 2. Several transformations
    // --------------------------------------------------
    val validSales = salesRDD
      .filter(_.split(",").length == 4)

    val parsedSales = validSales.map { line =>
      val parts = line.split(",")

      val day = parts(0)
      val product = parts(1)
      val quantity = parts(2).toInt
      val price = parts(3).toDouble

      (day, product, quantity, price)
    }

    val revenueByDay = parsedSales.map {
      case (day, _, quantity, price) =>
        (day, quantity * price)
    }

    // Wide transformation: shuffle by key
    val dailyRevenue = revenueByDay.reduceByKey(_ + _)

    // Another wide transformation: sorting requires redistribution
    val sortedRevenue = dailyRevenue.sortByKey()

    // --------------------------------------------------
    // 3. DAG representation
    // --------------------------------------------------
    println("\n--- DAG ---")
    println("salesRDD")
    println("   |")
    println("   v")
    println("filter(valid records)")
    println("   |")
    println("   v")
    println("map(parse records)")
    println("   |")
    println("   v")
    println("map(calculate revenue)")
    println("   |")
    println("   v")
    println("reduceByKey(_ + _)   <-- SHUFFLE")
    println("   |")
    println("   v")
    println("sortByKey()          <-- SHUFFLE")
    println("   |")
    println("   v")
    println("collect()            <-- ACTION")

    // --------------------------------------------------
    // 4. Stage prediction
    // --------------------------------------------------
    println("\n--- Stage Prediction ---")
    println("Stage 0:")
    println("  salesRDD -> filter -> map -> map")
    println()
    println("Shuffle Boundary 1:")
    println("  reduceByKey")
    println()
    println("Stage 1:")
    println("  reduceByKey output")
    println()
    println("Shuffle Boundary 2:")
    println("  sortByKey")
    println()
    println("Stage 2:")
    println("  sorted result -> collect")

    println("\nExpected stages for this pipeline: 3")

    // --------------------------------------------------
    // 5. Execute an action
    // --------------------------------------------------
    println("\n--- Daily Revenue ---")

    sortedRevenue.collect().foreach {
      case (day, revenue) =>
        println(f"$day%-10s ₹$revenue%.2f")
    }

    // --------------------------------------------------
    // 6. Job / Stage / Task / Partition explanation
    // --------------------------------------------------
    println("\n--- Spark Execution Concepts ---")

    println("Job:")
    println("  Created when an action such as collect() is executed.")

    println("Stage:")
    println("  A group of tasks separated by shuffle boundaries.")

    println("Task:")
    println("  The unit of work executed for one partition.")

    println("Partition:")
    println("  A logical piece of the distributed RDD.")

    println("\n--- Narrow vs Wide Transformations ---")

    println("Narrow:")
    println("  filter and map")
    println("  No data redistribution is normally required.")

    println("Wide:")
    println("  reduceByKey and sortByKey")
    println("  Data may need to move between partitions.")
    println("  These operations create shuffle boundaries.")

    // --------------------------------------------------
    // 7. Multiple actions
    // --------------------------------------------------
    println("\n--- Additional Actions ---")

    println(s"Number of revenue records : ${dailyRevenue.count()}")

    val firstRevenue = sortedRevenue.first()
    println(
      f"First sorted revenue      : ${firstRevenue._1}%-10s ₹${firstRevenue._2}%.2f"
    )

    println("\n--- Execution Summary ---")
    println("Transformations: filter -> map -> map -> reduceByKey -> sortByKey")
    println("Actions: collect, count, first")
    println("Wide transformations: reduceByKey, sortByKey")
    println("Predicted stages: 3")
    println("The DAG is divided into stages at shuffle boundaries.")

    println("\n========================================")
    println("DAY 08 COMPLETED SUCCESSFULLY")
    println("========================================")

    spark.stop()
  }
}
