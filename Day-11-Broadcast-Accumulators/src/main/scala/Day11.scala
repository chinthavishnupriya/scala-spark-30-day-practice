import org.apache.spark.sql.SparkSession

object Day11 {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("Day 11 - Broadcast and Accumulators")
      .master("local[4]")
      .getOrCreate()

    val sc = spark.sparkContext
    sc.setLogLevel("WARN")

    println("========================================")
    println("DAY 11 - BROADCAST AND ACCUMULATORS")
    println("========================================")

    // 1. Small reference data: suitable for broadcasting.
    val productMaster = Map(
      "P101" -> "Laptop",
      "P102" -> "Mouse",
      "P103" -> "Keyboard",
      "P104" -> "Monitor"
    )

    // 2. Broadcast the read-only reference map.
    val productBroadcast = sc.broadcast(productMaster)

    // 3. Accumulator for invalid/bad transaction records.
    val badRecords = sc.longAccumulator("Bad Records")

    // 4. Create transaction RDD with multiple partitions.
    val transactions = sc.parallelize(List(
      "T001,P101,2",
      "T002,P102,5",
      "T003,P999,1",
      "T004,P103,3",
      "T005,P888,2"
    ), 4)

    println("\n--- Spark Configuration ---")
    println(s"Spark version         : ${spark.version}")
    println(s"Master                : ${sc.master}")
    println(s"Transaction records   : ${transactions.count()}")
    println(s"Transaction partitions: ${transactions.getNumPartitions}")

    // 5. Validate transactions using the broadcast map.
    val validated = transactions.map { record =>
      val fields = record.split(",").map(_.trim)
      val transactionId = fields(0)
      val productId = fields(1)
      val quantity = fields(2).toInt

      productBroadcast.value.get(productId) match {
        case Some(productName) =>
          s"$transactionId,$productId,$productName,$quantity,VALID"
        case None =>
          badRecords.add(1)
          s"$transactionId,$productId,UNKNOWN,$quantity,INVALID"
      }
    }

    // 6. Action triggers execution.
    val results = validated.collect()

    println("\n--- Broadcast Information ---")
    println(s"Broadcast product count: ${productBroadcast.value.size}")
    println("The product master map is read-only and shared efficiently.")

    println("\n--- Accumulator Information ---")
    println("Accumulator: Bad Records")
    println("It is incremented when a transaction contains an unknown product ID.")

    println("\n--- Validated Transactions ---")
    results.foreach(println)

    val validCount = results.count(_.endsWith(",VALID"))

    println("\n--- Final Results ---")
    println(s"Valid records: $validCount")
    println(s"Bad records: ${badRecords.value}")

    println("\n--- Why Not a Normal Driver Variable? ---")
    println("Normal mutable driver variables should not be used for distributed updates.")
    println("Spark tasks execute on executors, while accumulators provide a supported counter mechanism.")

    println("\n========================================")
    println("DAY 11 COMPLETED SUCCESSFULLY")
    println("========================================")

    productBroadcast.destroy()
    spark.stop()
  }
}
