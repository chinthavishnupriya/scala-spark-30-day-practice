import org.apache.spark.sql.SparkSession
import org.apache.spark.storage.StorageLevel

object Day12 {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("Day 12 - Cache and Persist")
      .master("local[4]")
      .getOrCreate()

    val sc = spark.sparkContext
    sc.setLogLevel("WARN")

    println("========================================")
    println("DAY 12 - CACHE AND PERSIST")
    println("========================================")

    val transactions = sc.parallelize(List(
      "T001,C101,Electronics,5000",
      "T002,C102,Books,1200",
      "T003,C101,Clothing,2500",
      "T004,C103,Electronics,7500",
      "T005,C104,Books,1800",
      "T006,C102,Clothing,2200",
      "T007,C101,Electronics,4500",
      "T008,C105,Books,900",
      "T009,C103,Clothing,3200",
      "T010,C104,Electronics,6800"
    ), 4)

    val cleaned = transactions
      .map(_.trim)
      .filter(_.nonEmpty)
      .filter(_.split(",").length == 4)
      .map { line =>
        val p = line.split(",")
        (p(0), p(1), p(2), p(3).toDouble)
      }

    println("\n--- Dataset ---")
    println(s"Raw transaction count     : ${transactions.count()}")
    println(s"Cleaned transaction count : ${cleaned.count()}")
    println(s"Partitions                : ${cleaned.getNumPartitions}")

    // Cache the cleaned RDD because it will be reused by three reports.
    cleaned.cache()

    println("\n--- Cache ---")
    println(s"Storage level after cache : ${cleaned.getStorageLevel}")

    println("\n--- Report 1: Total Revenue ---")
    val totalRevenue = cleaned.map(_._4).sum()
    println(f"Total revenue: ₹$totalRevenue%.2f")

    println("\n--- Report 2: Revenue by Category ---")
    cleaned
      .map { case (_, _, category, amount) => (category, amount) }
      .reduceByKey(_ + _)
      .sortByKey()
      .collect()
      .foreach { case (category, amount) =>
        println(f"$category%-12s ₹$amount%.2f")
      }

    println("\n--- Report 3: Customer Transaction Counts ---")
    cleaned
      .map { case (_, customer, _, _) => (customer, 1) }
      .reduceByKey(_ + _)
      .sortByKey()
      .collect()
      .foreach { case (customer, count) =>
        println(s"$customer -> $count transactions")
      }

    println("\n--- Persist Comparison ---")
    val persisted = cleaned.map(identity).persist(StorageLevel.MEMORY_AND_DISK)
    println(s"Storage level with persist(MEMORY_AND_DISK): ${persisted.getStorageLevel}")
    println("cache() is shorthand for persist() with MEMORY_ONLY.")
    println("persist() allows selecting a storage level explicitly.")

    // Materialize the persisted RDD and release it after the comparison.
    persisted.count()
    persisted.unpersist()
    cleaned.unpersist()

    println("\n--- When Caching Can Hurt ---")
    println("Caching can hurt when the RDD is used only once.")
    println("It can also hurt when the dataset is too large for available memory.")
    println("Unnecessary cached data can cause memory pressure and eviction.")

    println("\n--- Summary ---")
    println("Cleaned RDD reused in three reports: YES")
    println("cache(): MEMORY_ONLY by default")
    println("persist(): configurable storage level")
    println("Example storage level: MEMORY_AND_DISK")

    println("\n========================================")
    println("DAY 12 COMPLETED SUCCESSFULLY")
    println("========================================")

    spark.stop()
  }
}
