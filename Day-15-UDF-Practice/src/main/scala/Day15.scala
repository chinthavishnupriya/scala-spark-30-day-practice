import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

object Day15 {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .appName("Day 15 - UDF Practice")
      .master("local[4]")
      .getOrCreate()

    spark.sparkContext.setLogLevel("WARN")

    val customers = spark.read
      .option("header", "true")
      .option("inferSchema", "true")
      .csv("input/customers.csv")

    println("\n===== CUSTOMER DATA =====")
    customers.show(false)

    val salaryBand = udf((salary: Long) => {
      if (salary < 30000) "Low"
      else if (salary < 60000) "Medium"
      else "High"
    })

    val riskCategory = udf((transactionValue: Long) => {
      if (transactionValue < 10000) "Low Risk"
      else if (transactionValue < 50000) "Medium Risk"
      else "High Risk"
    })

    spark.udf.register(
      "salary_band_udf",
      (salary: Long) => {
        if (salary < 30000) "Low"
        else if (salary < 60000) "Medium"
        else "High"
      }
    )

    val udfResult = customers
      .withColumn("salary_band_udf", salaryBand(col("salary")))
      .withColumn("risk_category_udf", riskCategory(col("transaction_value")))

    println("\n===== UDF RESULT =====")
    udfResult
      .select(
        "customer_id",
        "name",
        "salary",
        "salary_band_udf",
        "transaction_value",
        "risk_category_udf"
      )
      .show(false)

    val builtInResult = customers
      .withColumn(
        "salary_band_builtin",
        when(col("salary") < 30000, "Low")
          .when(col("salary") < 60000, "Medium")
          .otherwise("High")
      )
      .withColumn(
        "risk_category_builtin",
        when(col("transaction_value") < 10000, "Low Risk")
          .when(col("transaction_value") < 50000, "Medium Risk")
          .otherwise("High Risk")
      )

    println("\n===== BUILT-IN FUNCTION RESULT =====")
    builtInResult
      .select(
        "customer_id",
        "name",
        "salary",
        "salary_band_builtin",
        "transaction_value",
        "risk_category_builtin"
      )
      .show(false)

    val comparison = udfResult
      .join(
        builtInResult,
        Seq("customer_id"),
        "inner"
      )
      .select(
        col("customer_id"),
        col("salary_band_udf"),
        col("salary_band_builtin"),
        col("risk_category_udf"),
        col("risk_category_builtin")
      )
      .withColumn(
        "salary_match",
        col("salary_band_udf") === col("salary_band_builtin")
      )
      .withColumn(
        "risk_match",
        col("risk_category_udf") === col("risk_category_builtin")
      )

    println("\n===== UDF VS BUILT-IN COMPARISON =====")
    comparison.show(false)

    val mismatches = comparison
      .filter(
        !col("salary_match") || !col("risk_match")
      )
      .count()

    println(s"Comparison mismatches: $mismatches")

    if (mismatches == 0) {
      println("UDF and built-in results match for all customers.")
    }

    customers.createOrReplaceTempView("customers")

    val sqlResult = spark.sql(
      """
        SELECT
          customer_id,
          name,
          salary,
          salary_band_udf(salary) AS salary_band_sql,
          transaction_value,
          CASE
            WHEN transaction_value < 10000 THEN 'Low Risk'
            WHEN transaction_value < 50000 THEN 'Medium Risk'
            ELSE 'High Risk'
          END AS risk_category_sql
        FROM customers
      """
    )

    println("\n===== REGISTERED UDF THROUGH SPARK SQL =====")
    sqlResult.show(false)

    println("\n===== CUSTOMER RISK SUMMARY =====")

    udfResult
      .groupBy("risk_category_udf")
      .count()
      .orderBy("risk_category_udf")
      .show(false)

    println("\n===== UDF EXECUTION PLAN =====")
    udfResult.explain(true)

    println("\n===== DAY 15 COMPLETED SUCCESSFULLY =====")

    spark.stop()
  }
}
