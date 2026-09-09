import org.apache.spark.sql.SparkSession
import org.apache.spark.HashPartitioner

object Day10 {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .appName("Day 10 - Partitioning")
      .master("local[4]")
      .config("spark.serializer", "org.apache.spark.serializer.JavaSerializer")
      .getOrCreate()

    spark.sparkContext.setLogLevel("WARN")

    println("========================================")
    println("DAY 10 - PARTITIONING")
    println("========================================")

    val inputPath = "input/sales.txt"

    // --------------------------------------------------
    // 1. Create an RDD with too few partitions
    // --------------------------------------------------
    val salesRDD = spark.sparkContext.textFile(inputPath, 1)

    println("\n--- Original Dataset ---")
    println(s"Records              : ${salesRDD.count()}")
    println(s"Original partitions  : ${salesRDD.getNumPartitions}")

    // --------------------------------------------------
    // 2. Increase partitions using repartition
    // --------------------------------------------------
    val repartitionedRDD = salesRDD.repartition(4)

    println("\n--- repartition(4) ---")
    println(s"Partitions after repartition : ${repartitionedRDD.getNumPartitions}")
    println("repartition can increase or decrease partitions.")
    println("It normally involves a shuffle.")

    // --------------------------------------------------
    // 3. Reduce partitions using coalesce
    // --------------------------------------------------
    val coalescedRDD = repartitionedRDD.coalesce(2)

    println("\n--- coalesce(2) ---")
    println(s"Partitions after coalesce : ${coalescedRDD.getNumPartitions}")
    println("coalesce is useful for reducing partitions.")
    println("It can avoid a full shuffle when reducing partitions.")

    // --------------------------------------------------
    // 4. Compare partition counts
    // --------------------------------------------------
    println("\n--- Partition Count Comparison ---")

    println(s"Original      : ${salesRDD.getNumPartitions}")
    println(s"Repartitioned : ${repartitionedRDD.getNumPartitions}")
    println(s"Coalesced     : ${coalescedRDD.getNumPartitions}")

    // --------------------------------------------------
    // 5. Demonstrate partition contents
    // --------------------------------------------------
    println("\n--- Partition Contents ---")

    repartitionedRDD
      .mapPartitionsWithIndex { (index, iterator) =>
        Iterator(
          s"Partition $index -> ${iterator.size} records"
        )
      }
      .collect()
      .foreach(println)

    // --------------------------------------------------
    // 6. Create a Pair RDD
    // --------------------------------------------------
    val productRevenue = salesRDD.map { line =>
      val parts = line.split(",")

      val saleId = parts(0)
      val product = parts(1)
      val revenue = parts(2).toDouble

      (product, (saleId, revenue))
    }

    println("\n--- Pair RDD Before partitionBy ---")
    println(s"Partitions : ${productRevenue.getNumPartitions}")

    // --------------------------------------------------
    // 7. partitionBy
    // --------------------------------------------------
    val partitionedPairRDD = productRevenue.partitionBy(
      new HashPartitioner(4)
    )

    println("\n--- Pair RDD After partitionBy ---")
    println(s"Partitions : ${partitionedPairRDD.getNumPartitions}")
    println("partitionBy assigns keys to partitions using a partitioner.")

    // --------------------------------------------------
    // 8. Verify partitioner
    // --------------------------------------------------
    println("\n--- Partitioner Information ---")

    println(
      s"Partitioner: ${partitionedPairRDD.partitioner.getOrElse("None")}"
    )

    // --------------------------------------------------
    // 9. Aggregate revenue by product
    // --------------------------------------------------
    val revenueByProduct = salesRDD.map { line =>
      val parts = line.split(",")

      val product = parts(1)
      val revenue = parts(2).toDouble

      (product, revenue)
    }

    println("\n--- Revenue by Product ---")

    revenueByProduct
      .reduceByKey(_ + _)
      .sortByKey()
      .collect()
      .foreach {
        case (product, revenue) =>
          println(f"$product%-12s ₹$revenue%.2f")
      }

    // --------------------------------------------------
    // 10. When to increase partitions
    // --------------------------------------------------
    println("\n--- When to Increase Partitions ---")

    println("Increase partitions when:")
    println("  The dataset is large.")
    println("  Existing partitions contain too much data.")
    println("  More parallelism is available.")
    println("  Tasks are taking too long.")

    // --------------------------------------------------
    // 11. When to decrease partitions
    // --------------------------------------------------
    println("\n--- When to Decrease Partitions ---")

    println("Decrease partitions when:")
    println("  There are too many small partitions.")
    println("  Task scheduling overhead becomes significant.")
    println("  A smaller number of output files is desired.")

    // --------------------------------------------------
    // 12. Optimization scenario
    // --------------------------------------------------
    println("\n--- Optimization Scenario ---")

    println("Problem:")
    println("  The original dataset has only 1 partition.")

    println("Optimization:")
    println("  Repartition the dataset to 4 partitions.")

    println("Reason:")
    println("  More partitions allow more tasks to process data")
    println("  concurrently when multiple CPU cores are available.")

    println("\n--- Partitioning Summary ---")

    println("repartition:")
    println("  Changes partition count using a shuffle.")

    println("coalesce:")
    println("  Primarily reduces partition count with less data movement.")

    println("partitionBy:")
    println("  Controls how Pair RDD keys are distributed.")

    println("Original partitions : 1")
    println("Optimized partitions: 4")

    println("\n========================================")
    println("DAY 10 COMPLETED SUCCESSFULLY")
    println("========================================")

    spark.stop()
  }
}
