import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

object Day13 {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .appName("Day 13 - Spark SQL Basics")
      .master("local[4]")
      .getOrCreate()

    spark.sparkContext.setLogLevel("WARN")

    println("========================================")
    println("DAY 13 - SPARK SQL BASICS")
    println("========================================")

    println(s"Spark version : ${spark.version}")
    println(s"Master        : ${spark.sparkContext.master}")

    // ------------------------------------------------
    // 1. READ CSV INTO DATAFRAME
    // ------------------------------------------------

    println("\n--- 1. READ CUSTOMER CSV ---")

    val customers = spark.read
      .option("header", "true")
      .option("inferSchema", "true")
      .csv("input/customers.csv")

    println(s"Number of records: ${customers.count()}")

    // ------------------------------------------------
    // 2. DISPLAY DATA
    // ------------------------------------------------

    println("\n--- 2. CUSTOMER DATA ---")

    customers.show(false)

    // ------------------------------------------------
    // 3. INSPECT SCHEMA
    // ------------------------------------------------

    println("\n--- 3. SCHEMA ---")

    customers.printSchema()

    // ------------------------------------------------
    // 4. SELECT COLUMNS
    // ------------------------------------------------

    println("\n--- 4. SELECT COLUMNS ---")

    customers
      .select("customer_id", "name", "city", "total_spend")
      .show(false)

    // ------------------------------------------------
    // 5. FILTER CUSTOMERS
    // ------------------------------------------------

    println("\n--- 5. FILTER: SPENDING ABOVE 10000 ---")

    customers
      .filter(col("total_spend") > 10000)
      .select("customer_id", "name", "city", "total_spend")
      .show(false)

    // ------------------------------------------------
    // 6. WITHCOLUMN
    // ------------------------------------------------

    println("\n--- 6. WITHCOLUMN: SPENDING CATEGORY ---")

    val categorizedCustomers = customers.withColumn(
      "spending_category",
      when(col("total_spend") >= 15000, "High")
        .when(col("total_spend") >= 10000, "Medium")
        .otherwise("Low")
    )

    categorizedCustomers.show(false)

    // ------------------------------------------------
    // 7. CALCULATED COLUMN
    // ------------------------------------------------

    println("\n--- 7. WITHCOLUMN: LOYALTY POINTS ---")

    val analyticsDF = categorizedCustomers.withColumn(
      "loyalty_points",
      floor(col("total_spend") / 100)
    )

    analyticsDF.show(false)

    // ------------------------------------------------
    // 8. TEMPORARY VIEW
    // ------------------------------------------------

    println("\n--- 8. CREATE TEMPORARY VIEW ---")

    analyticsDF.createOrReplaceTempView("customers")

    println("Temporary view 'customers' created successfully.")

    // ------------------------------------------------
    // 9. SPARK SQL QUERY
    // ------------------------------------------------

    println("\n--- 9. SQL: HIGH-VALUE CUSTOMERS ---")

    val highValueCustomers = spark.sql(
      """
        |SELECT customer_id,
        |       name,
        |       city,
        |       total_spend,
        |       spending_category,
        |       loyalty_points
        |FROM customers
        |WHERE total_spend >= 15000
        |ORDER BY total_spend DESC
        |""".stripMargin
    )

    highValueCustomers.show(false)

    // ------------------------------------------------
    // 10. CITY ANALYTICS
    // ------------------------------------------------

    println("\n--- 10. SQL: CUSTOMER SPENDING BY CITY ---")

    val cityReport = spark.sql(
      """
        |SELECT city,
        |       COUNT(*) AS customer_count,
        |       ROUND(SUM(total_spend), 2) AS total_spend,
        |       ROUND(AVG(total_spend), 2) AS average_spend
        |FROM customers
        |GROUP BY city
        |ORDER BY total_spend DESC
        |""".stripMargin
    )

    cityReport.show(false)

    // ------------------------------------------------
    // 11. TOP CUSTOMERS
    // ------------------------------------------------

    println("\n--- 11. SQL: TOP 3 CUSTOMERS ---")

    val topCustomers = spark.sql(
      """
        |SELECT customer_id,
        |       name,
        |       city,
        |       total_spend
        |FROM customers
        |ORDER BY total_spend DESC
        |LIMIT 3
        |""".stripMargin
    )

    topCustomers.show(false)

    // ------------------------------------------------
    // 12. EXPLAIN SPARK SQL
    // ------------------------------------------------

    println("\n--- 12. SPARK SQL EXECUTION ---")

    println("DataFrame operations:")
    println("  select, filter, withColumn")

    println("\nSQL operations:")
    println("  SELECT, WHERE, GROUP BY, ORDER BY, LIMIT")

    println("\nAction:")
    println("  show, count")

    println("\nTemporary view:")
    println("  customers")

    println("\nOptimization:")
    println("  Spark SQL uses Catalyst to optimize SQL/DataFrame plans.")

    println("\n========================================")
    println("DAY 13 COMPLETED SUCCESSFULLY")
    println("========================================")

    spark.stop()
  }
}
