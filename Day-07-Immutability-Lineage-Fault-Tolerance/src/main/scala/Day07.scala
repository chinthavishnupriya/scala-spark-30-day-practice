import org.apache.spark.sql.SparkSession

object Day07 {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("Day 7 - Immutability, Lineage and Fault Tolerance")
      .master("local[4]")
      .config("spark.serializer", "org.apache.spark.serializer.JavaSerializer")
      .getOrCreate()

    spark.sparkContext.setLogLevel("WARN")

    println("========================================")
    println("DAY 07 - IMMUTABILITY, LINEAGE & FAULT TOLERANCE")
    println("========================================")

    val inputPath = "input/sales.txt"
    val salesRDD = spark.sparkContext.textFile(inputPath, 4)

    println(s"\nOriginal RDD partitions : ${salesRDD.getNumPartitions}")
    println("Original RDD is immutable: transformations create new RDDs.")

    // Multi-step transformation chain.
    val validSales = salesRDD.filter(_.split(",").length == 4)

    val parsedSales = validSales.map { line =>
      val fields = line.split(",")
      (fields(0), fields(1), fields(2).toInt, fields(3).toDouble)
    }

    val revenueByDay = parsedSales.map {
      case (day, product, quantity, price) =>
        (day, quantity * price)
    }

    // reduceByKey introduces a shuffle boundary.
    val dailyRevenue = revenueByDay.reduceByKey(_ + _)

    println("\n--- Transformation Chain ---")
    println("salesRDD")
    println("  -> filter(valid records)")
    println("  -> map(parse records)")
    println("  -> map(calculate revenue)")
    println("  -> reduceByKey(sum by day)")

    println("\n--- RDD Lineage ---")
    println(dailyRevenue.toDebugString)

    println("\n--- Daily Revenue ---")
    dailyRevenue.sortByKey().collect().foreach {
      case (day, revenue) => println(f"$day%-10s ₹$revenue%.2f")
    }

    println("\n--- Immutability Demonstration ---")
    println(s"Original salesRDD count : ${salesRDD.count()}")
    println(s"Valid sales count       : ${validSales.count()}")
    println("The original salesRDD remains unchanged after filter/map operations.")

    println("\n--- Fault Tolerance ---")
    println("1. Spark records the lineage of every derived RDD.")
    println("2. If a partition is lost, Spark identifies the missing partition.")
    println("3. Spark recomputes that partition from the required parent RDDs.")
    println("4. Other healthy partitions do not need to be recomputed.")
    println("5. In this pipeline, Spark can rebuild a lost output partition by replaying the required transformations.")

    println("\n--- Conceptual Executor-Loss Simulation ---")
    println("Assume an executor holding one dailyRevenue partition is lost.")
    println("Spark would schedule the lost task on another executor.")
    println("The missing partition is recomputed from its lineage: salesRDD -> filter -> map -> map -> reduceByKey.")
    println("No manual reconstruction of the original dataset is required.")

    println("\n--- Performance Notes ---")
    println("Transformations are lazy until an action such as collect or count runs.")
    println("filter and map are narrow transformations.")
    println("reduceByKey is a wide transformation and creates a shuffle boundary.")
    println("Lineage provides fault recovery, while caching/persistence can reduce recomputation when reused data is available.")

    println("\n========================================")
    println("DAY 07 COMPLETED SUCCESSFULLY")
    println("========================================")

    spark.stop()
  }
}
