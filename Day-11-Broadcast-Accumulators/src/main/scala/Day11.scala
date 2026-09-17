import org.apache.spark.sql.SparkSession

object Day11 {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("Day 11 - Broadcast and Accumulators")
      .master("local[4]")
      .getOrCreate()

    val sc = spark.sparkContext
    sc.setLogLevel("WARN")

    // Small reference data: ideal for broadcasting.
    val productMaster = Map(
      "P101" -> "Laptop",
      "P102" -> "Mouse",
      "P103" -> "Keyboard",
      "P104" -> "Monitor"
    )

    val productBroadcast = sc.broadcast(productMaster)
    val badRecords = sc.longAccumulator("Bad Records")

    val transactions = sc.parallelize(List(
      "T001,P101,2",
      "T002,P102,5",
      "T003,P999,1",
      "T004,P103,3",
      "T005,P888,2"
    ), 4)

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

    val results = validated.collect()

    println("=== Day 11: Broadcast and Accumulators ===")
    println(s"Spark version: ${spark.version}")
    println(s"Master: ${sc.master}")
    println(s"Broadcast product count: ${productBroadcast.value.size}")
    println("Validated transactions:")
    results.foreach(println)
    println(s"Valid records: ${results.count(_.endsWith(",VALID"))}")
    println(s"Bad records: ${badRecords.value}")

    productBroadcast.destroy()
    spark.stop()
  }
}
