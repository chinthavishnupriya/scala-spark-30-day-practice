import org.apache.spark.sql.SparkSession

object Day04 {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .appName("Day 4 - RDD Creation and Operations")
      .master("local[4]")
      .getOrCreate()

    val sc = spark.sparkContext

    println("========================================")
    println("DAY 04 - RDD CREATION AND OPERATIONS")
    println("========================================")

    println(s"Spark version      : ${spark.version}")
    println(s"Master             : ${sc.master}")
    println(s"Default parallelism: ${sc.defaultParallelism}")

    // ------------------------------------------------
    // 1. RDD FROM SCALA COLLECTION
    // ------------------------------------------------
    println()
    println("--- 1. RDD FROM SCALA COLLECTION ---")

    val numbers = sc.parallelize(1 to 10, 4)

    println(s"Number of elements : ${numbers.count()}")
    println(s"Number of partitions: ${numbers.getNumPartitions}")

    println("Squared values:")
    numbers.map(n => n * n).collect().foreach(println)

    // ------------------------------------------------
    // 2. RDD FROM TEXT FILE
    // ------------------------------------------------
    println()
    println("--- 2. RDD FROM TEXT FILE ---")

    val customers = sc.textFile("input/customers.txt", 4)

    println(s"Customer records   : ${customers.count()}")
    println(s"Customer partitions: ${customers.getNumPartitions}")

    // ------------------------------------------------
    // 3. MAP
    // ------------------------------------------------
    println()
    println("--- 3. MAP ---")

    val customerNames = customers
      .map(line => line.split(",")(1))

    customerNames.collect().foreach(println)

    // ------------------------------------------------
    // 4. FILTER
    // ------------------------------------------------
    println()
    println("--- 4. FILTER ---")

    val chennaiCustomers = customers
      .filter(line => line.split(",")(2) == "Chennai")

    println("Customers from Chennai:")
    chennaiCustomers.collect().foreach(println)

    // ------------------------------------------------
    // 5. FLATMAP
    // ------------------------------------------------
    println()
    println("--- 5. FLATMAP ---")

    val customerFields = customers
      .flatMap(line => line.split(","))

    println(s"Total fields produced by flatMap: ${customerFields.count()}")

    // ------------------------------------------------
    // 6. SALES CALCULATION
    // ------------------------------------------------
    println()
    println("--- 6. TOTAL SALES ---")

    val sales = sc.textFile("input/sales.txt", 4)

    val totalSales = sales
      .map { line =>
        val fields = line.split(",")
        val quantity = fields(2).toInt
        val price = fields(3).toDouble
        quantity * price
      }
      .reduce(_ + _)

    println(f"Total sales: Rs. $totalSales%.2f")

    // ------------------------------------------------
    // 7. PARTITION INSPECTION
    // ------------------------------------------------
    println()
    println("--- 7. PARTITION INSPECTION ---")

    println(s"Numbers RDD partitions  : ${numbers.getNumPartitions}")
    println(s"Customers RDD partitions: ${customers.getNumPartitions}")
    println(s"Sales RDD partitions    : ${sales.getNumPartitions}")

    val partitionInfo = customers
      .mapPartitionsWithIndex {
        (index, iterator) =>
          Iterator(s"Partition $index contains ${iterator.size} records")
      }
      .collect()

    partitionInfo.foreach(println)

    println()
    println("========================================")
    println("DAY 04 APPLICATION COMPLETED")
    println("========================================")

    spark.stop()
  }
}
