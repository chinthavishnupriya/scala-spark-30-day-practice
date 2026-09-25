import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

object Day19 {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .appName("Day-19-Broadcast-Join")
      .master("local[4]")
      .getOrCreate()

    spark.sparkContext.setLogLevel("WARN")

    println("==============================================")
    println("DAY 19 - BROADCAST JOIN")
    println("==============================================")
    println(s"Spark Version : ${spark.version}")
    println(s"Java Version  : ${System.getProperty("java.version")}")
    println("Master        : local[4]")
    println("==============================================")

    // --------------------------------------------------
    // 1. Read transaction fact data
    // --------------------------------------------------

    val transactionsDF = spark.read
      .option("header", "true")
      .option("inferSchema", "true")
      .csv("input/transactions.csv")

    // --------------------------------------------------
    // 2. Read small branch reference data
    // --------------------------------------------------

    val branchesDF = spark.read
      .option("header", "true")
      .option("inferSchema", "true")
      .csv("input/branch_master.csv")

    println("\n--- TRANSACTION DATA ---")
    transactionsDF.show(false)

    println("\n--- BRANCH MASTER DATA ---")
    branchesDF.show(false)

    println("\n--- RECORD COUNTS ---")
    println(s"Transaction records : ${transactionsDF.count()}")
    println(s"Branch records      : ${branchesDF.count()}")

    println("\n--- SCHEMAS ---")
    transactionsDF.printSchema()
    branchesDF.printSchema()

    // --------------------------------------------------
    // 3. Normal join
    // --------------------------------------------------

    println("\n--- NORMAL INNER JOIN ---")

    val normalJoinDF = transactionsDF
      .join(
        branchesDF,
        transactionsDF("branch_id") === branchesDF("branch_id"),
        "inner"
      )
      .select(
        transactionsDF("transaction_id"),
        transactionsDF("customer_id"),
        transactionsDF("branch_id"),
        branchesDF("branch_name"),
        branchesDF("city"),
        branchesDF("region"),
        transactionsDF("amount"),
        transactionsDF("transaction_type")
      )

    normalJoinDF.show(false)

    // --------------------------------------------------
    // 4. Broadcast join
    // --------------------------------------------------

    println("\n--- BROADCAST JOIN ---")

    val broadcastJoinDF = transactionsDF
      .join(
        broadcast(branchesDF),
        transactionsDF("branch_id") === branchesDF("branch_id"),
        "inner"
      )
      .select(
        transactionsDF("transaction_id"),
        transactionsDF("customer_id"),
        transactionsDF("branch_id"),
        branchesDF("branch_name"),
        branchesDF("city"),
        branchesDF("region"),
        transactionsDF("amount"),
        transactionsDF("transaction_type")
      )

    broadcastJoinDF.show(false)

    // --------------------------------------------------
    // 5. Compare result counts
    // --------------------------------------------------

    println("\n--- RESULT COMPARISON ---")

    val normalCount = normalJoinDF.count()
    val broadcastCount = broadcastJoinDF.count()

    println(s"Normal Join Count    : $normalCount")
    println(s"Broadcast Join Count : $broadcastCount")
    println(s"Result counts match  : ${normalCount == broadcastCount}")

    // --------------------------------------------------
    // 6. Broadcast execution plan
    // --------------------------------------------------

    println("\n--- BROADCAST JOIN EXECUTION PLAN ---")
    broadcastJoinDF.explain(true)

    // --------------------------------------------------
    // 7. SQL demonstration
    // --------------------------------------------------

    transactionsDF.createOrReplaceTempView("transactions")
    branchesDF.createOrReplaceTempView("branch_master")

    println("\n--- SPARK SQL JOIN ---")

    val sqlJoinDF = spark.sql(
      """
        |SELECT
        |  t.transaction_id,
        |  t.customer_id,
        |  t.branch_id,
        |  b.branch_name,
        |  b.city,
        |  b.region,
        |  t.amount,
        |  t.transaction_type
        |FROM transactions t
        |INNER JOIN branch_master b
        |  ON t.branch_id = b.branch_id
        |ORDER BY t.transaction_id
        |""".stripMargin
    )

    sqlJoinDF.show(false)

    // --------------------------------------------------
    // 8. Aggregation after broadcast join
    // --------------------------------------------------

    println("\n--- BRANCH-WISE TRANSACTION SUMMARY ---")

    val branchSummaryDF = broadcastJoinDF
      .groupBy(
        col("branch_id"),
        col("branch_name"),
        col("city")
      )
      .agg(
        count("*").alias("transaction_count"),
        sum("amount").alias("total_amount"),
        avg("amount").alias("average_amount")
      )
      .orderBy(col("total_amount").desc)

    branchSummaryDF.show(false)

    // --------------------------------------------------
    // 9. Important observations
    // --------------------------------------------------

    println("\n--- BROADCAST JOIN OBSERVATIONS ---")
    println("1. The branch master is the small reference dataset.")
    println("2. broadcast(branchesDF) explicitly marks it for broadcast.")
    println("3. Each executor can use the broadcast reference data locally.")
    println("4. The join avoids shuffling the large transaction side for the broadcast strategy.")
    println("5. Broadcasting is appropriate only when the reference dataset is small enough.")
    println("6. A very large broadcast dataset can create memory pressure.")
    println("7. Shuffle Sort-Merge Join remains useful when both datasets are large.")

    println("\n==============================================")
    println("DAY 19 COMPLETED")
    println("==============================================")

    spark.stop()
  }
}
