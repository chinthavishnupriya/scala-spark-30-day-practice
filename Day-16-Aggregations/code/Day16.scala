import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

object Day16 {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .appName("Day-16-Aggregations")
      .master("local[4]")
      .getOrCreate()

    spark.sparkContext.setLogLevel("WARN")

    println("=" * 70)
    println("DAY 16 - AGGREGATIONS")
    println("=" * 70)

    val hospitalDF = spark.read
      .option("header", "true")
      .option("inferSchema", "true")
      .csv("input/hospital_revenue.csv")

    println("\n--- INPUT DATA ---")
    hospitalDF.show(false)

    println("\n--- SCHEMA ---")
    hospitalDF.printSchema()

    println(s"\nTotal Records: ${hospitalDF.count()}")

    val revenueDF = hospitalDF.withColumn(
      "total_revenue",
      col("consultation_revenue") + col("procedure_revenue")
    )

    println("\n--- DATA WITH TOTAL REVENUE ---")
    revenueDF.show(false)

    println("\n--- BASIC AGGREGATIONS ---")

    val overallStats = revenueDF.agg(
      count("*").alias("record_count"),
      sum("total_revenue").alias("total_revenue"),
      avg("total_revenue").alias("average_revenue"),
      min("total_revenue").alias("minimum_revenue"),
      max("total_revenue").alias("maximum_revenue")
    )

    overallStats.show(false)

    println("\n--- DEPARTMENT-WISE REVENUE STATISTICS ---")

    val departmentStats = revenueDF
      .groupBy("department")
      .agg(
        count("*").alias("record_count"),
        sum("patients").alias("total_patients"),
        sum("total_revenue").alias("department_revenue"),
        avg("total_revenue").alias("average_revenue"),
        min("total_revenue").alias("minimum_revenue"),
        max("total_revenue").alias("maximum_revenue")
      )
      .orderBy(desc("department_revenue"))

    departmentStats.show(false)

    println("\n--- DEPARTMENT + CITY AGGREGATION ---")

    val departmentCityStats = revenueDF
      .groupBy("department", "city")
      .agg(
        count("*").alias("record_count"),
        sum("patients").alias("total_patients"),
        sum("total_revenue").alias("total_revenue"),
        avg("total_revenue").alias("average_revenue")
      )
      .orderBy("department", "city")

    departmentCityStats.show(false)

    println("\n--- HAVING-LIKE FILTER ---")

    val highRevenueDepartments = departmentStats
      .filter(col("department_revenue") > 300000)
      .orderBy(desc("department_revenue"))

    highRevenueDepartments.show(false)

    println("\n--- PATIENT STATISTICS BY DEPARTMENT ---")

    val patientStats = revenueDF
      .groupBy("department")
      .agg(
        sum("patients").alias("total_patients"),
        avg("patients").alias("average_patients"),
        min("patients").alias("minimum_patients"),
        max("patients").alias("maximum_patients")
      )
      .orderBy(desc("total_patients"))

    patientStats.show(false)

    println("\n--- HIGHEST REVENUE DEPARTMENT ---")

    departmentStats
      .orderBy(desc("department_revenue"))
      .limit(1)
      .show(false)

    println("\n--- SPARK SQL AGGREGATION ---")

    revenueDF.createOrReplaceTempView("hospital_revenue")

    val sqlResult = spark.sql(
      """
        SELECT
          department,
          COUNT(*) AS record_count,
          SUM(patients) AS total_patients,
          SUM(total_revenue) AS total_revenue,
          AVG(total_revenue) AS average_revenue,
          MIN(total_revenue) AS minimum_revenue,
          MAX(total_revenue) AS maximum_revenue
        FROM hospital_revenue
        GROUP BY department
        HAVING SUM(total_revenue) > 300000
        ORDER BY total_revenue DESC
      """
    )

    sqlResult.show(false)

    println("\n--- EXECUTION PLAN ---")
    departmentStats.explain(true)

    println("\n" + "=" * 70)
    println("DAY 16 COMPLETED SUCCESSFULLY")
    println("=" * 70)

    spark.stop()
  }
}
