import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

object Day22 {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .appName("Day 22 - Batch Mini Project")
      .master("local[4]")
      .config("spark.sql.warehouse.dir", "output/spark-warehouse")
      .getOrCreate()

    spark.sparkContext.setLogLevel("WARN")

    println("==============================================")
    println("DAY 22 - BATCH MINI PROJECT")
    println("==============================================")

    // --------------------------------------------------
    // 1. READ RAW TRANSACTIONS
    // --------------------------------------------------

    println()
    println("--- 1. READ RAW TRANSACTIONS ---")

    val transactionsDF = spark.read
      .option("header", "true")
      .option("inferSchema", "true")
      .csv("input/transactions.csv")

    println("Raw transaction count: " + transactionsDF.count())
    transactionsDF.show(false)

    // --------------------------------------------------
    // 2. READ CUSTOMER DATA
    // --------------------------------------------------

    println()
    println("--- 2. READ CUSTOMER DATA ---")

    val customersDF = spark.read
      .option("header", "true")
      .option("inferSchema", "true")
      .csv("input/customers.csv")

    println("Customer count: " + customersDF.count())
    customersDF.show(false)

    // --------------------------------------------------
    // 3. READ PRODUCT DATA
    // --------------------------------------------------

    println()
    println("--- 3. READ PRODUCT DATA ---")

    val productsDF = spark.read
      .option("header", "true")
      .option("inferSchema", "true")
      .csv("input/products.csv")

    println("Product count: " + productsDF.count())
    productsDF.show(false)

    // --------------------------------------------------
    // 4. CLEAN INVALID TRANSACTIONS
    // --------------------------------------------------

    println()
    println("--- 4. CLEAN INVALID TRANSACTIONS ---")

    val cleanedTransactionsDF = transactionsDF
      .filter(col("status") === "COMPLETED")
      .filter(col("quantity") > 0)
      .filter(col("unit_price") > 0)

    val invalidCount =
      transactionsDF.count() - cleanedTransactionsDF.count()

    println("Invalid records removed: " + invalidCount)
    println("Clean transaction count: " + cleanedTransactionsDF.count())

    cleanedTransactionsDF.show(false)

    // --------------------------------------------------
    // 5. CALCULATE TRANSACTION REVENUE
    // --------------------------------------------------

    println()
    println("--- 5. CALCULATE TRANSACTION REVENUE ---")

    val enrichedTransactionsDF = cleanedTransactionsDF
      .withColumn(
        "revenue",
        col("quantity") * col("unit_price")
      )

    enrichedTransactionsDF.show(false)

    // --------------------------------------------------
    // 6. JOIN CUSTOMER DATA
    // --------------------------------------------------

    println()
    println("--- 6. JOIN CUSTOMER DATA ---")

    val customerEnrichedDF = enrichedTransactionsDF
      .join(
        customersDF,
        Seq("customer_id"),
        "inner"
      )

    customerEnrichedDF.show(false)

    // --------------------------------------------------
    // 7. JOIN PRODUCT DATA
    // --------------------------------------------------

    println()
    println("--- 7. JOIN PRODUCT DATA ---")

    val fullyEnrichedDF = customerEnrichedDF
      .join(
        productsDF,
        Seq("product_id"),
        "inner"
      )

    fullyEnrichedDF.show(false)

    // --------------------------------------------------
    // 8. DAILY SALES AGGREGATION
    // --------------------------------------------------

    println()
    println("--- 8. DAILY SALES AGGREGATION ---")

    val dailySalesDF = fullyEnrichedDF
      .groupBy(
        col("transaction_date"),
        col("city"),
        col("category")
      )
      .agg(
        count("*").alias("transaction_count"),
        sum("quantity").alias("units_sold"),
        sum("revenue").alias("total_revenue")
      )
      .orderBy(
        col("transaction_date"),
        col("total_revenue").desc
      )

    dailySalesDF.show(false)

    // --------------------------------------------------
    // 9. CUSTOMER REVENUE AGGREGATION
    // --------------------------------------------------

    println()
    println("--- 9. CUSTOMER REVENUE AGGREGATION ---")

    val customerRevenueDF = fullyEnrichedDF
      .groupBy(
        col("customer_id"),
        col("customer_name"),
        col("city"),
        col("segment")
      )
      .agg(
        count("*").alias("transaction_count"),
        sum("revenue").alias("total_revenue")
      )
      .orderBy(col("total_revenue").desc)

    customerRevenueDF.show(false)

    // --------------------------------------------------
    // 10. WRITE PARTITIONED PARQUET OUTPUT
    // --------------------------------------------------

    println()
    println("--- 10. WRITE PARTITIONED PARQUET OUTPUT ---")

    dailySalesDF
      .write
      .mode("overwrite")
      .partitionBy("transaction_date")
      .parquet("output/daily-sales")

    println("Partitioned Parquet output written to:")
    println("output/daily-sales")

    // --------------------------------------------------
    // 11. READ OUTPUT BACK
    // --------------------------------------------------

    println()
    println("--- 11. VERIFY PARQUET OUTPUT ---")

    val outputDF = spark.read
      .parquet("output/daily-sales")

    println("Output record count: " + outputDF.count())
    outputDF.show(false)

    // --------------------------------------------------
    // 12. EXECUTION PLAN
    // --------------------------------------------------

    println()
    println("--- 12. EXECUTION PLAN ---")

    dailySalesDF.explain("formatted")

    println()
    println("==============================================")
    println("DAY 22 COMPLETED SUCCESSFULLY")
    println("==============================================")

    spark.stop()
  }
}
