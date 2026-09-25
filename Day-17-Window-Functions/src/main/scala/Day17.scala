import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions._

object Day17 {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .appName("Day-17-Window-Functions")
      .master("local[4]")
      .getOrCreate()

    spark.sparkContext.setLogLevel("WARN")

    println("=" * 75)
    println("DAY 17 - WINDOW FUNCTIONS")
    println("=" * 75)

    // ------------------------------------------------------------
    // 1. Read student data
    // ------------------------------------------------------------

    val studentsDF = spark.read
      .option("header", "true")
      .option("inferSchema", "true")
      .csv("input/students.csv")

    println("\n--- STUDENT INPUT DATA ---")
    studentsDF.show(false)

    println("\n--- STUDENT SCHEMA ---")
    studentsDF.printSchema()

    println(s"\nStudent Records: ${studentsDF.count()}")

    // ------------------------------------------------------------
    // 2. row_number, rank and dense_rank
    // ------------------------------------------------------------

    val studentWindow =
      Window
        .partitionBy("course")
        .orderBy(desc("score"), asc("student"))

    val rankedStudentsDF = studentsDF
      .withColumn("row_number", row_number().over(studentWindow))
      .withColumn("rank", rank().over(studentWindow))
      .withColumn("dense_rank", dense_rank().over(studentWindow))

    println("\n--- ROW_NUMBER / RANK / DENSE_RANK ---")
    rankedStudentsDF
      .orderBy("course", "row_number")
      .show(false)

    // ------------------------------------------------------------
    // 3. Top 3 students per course
    // ------------------------------------------------------------

    val top3StudentsDF = rankedStudentsDF
      .filter(col("row_number") <= 3)
      .orderBy("course", "row_number")

    println("\n--- TOP 3 STUDENTS PER COURSE ---")
    top3StudentsDF.show(false)

    // ------------------------------------------------------------
    // 4. Ranking comparison using score-only ordering
    //    This makes ties visible for rank and dense_rank.
    // ------------------------------------------------------------

    val scoreOnlyWindow =
      Window
        .partitionBy("course")
        .orderBy(desc("score"))

    val tieRankingDF = studentsDF
      .withColumn("row_number", row_number().over(scoreOnlyWindow))
      .withColumn("rank", rank().over(scoreOnlyWindow))
      .withColumn("dense_rank", dense_rank().over(scoreOnlyWindow))

    println("\n--- RANKING WITH SCORE TIES ---")
    tieRankingDF
      .orderBy("course", "rank", "student")
      .show(false)

    // ------------------------------------------------------------
    // 5. Read customer policy data
    // ------------------------------------------------------------

    val policiesDF = spark.read
      .option("header", "true")
      .option("inferSchema", "true")
      .csv("input/customer_policies.csv")

    println("\n--- CUSTOMER POLICY DATA ---")
    policiesDF.show(false)

    println("\n--- POLICY SCHEMA ---")
    policiesDF.printSchema()

    println(s"\nPolicy Records: ${policiesDF.count()}")

    // ------------------------------------------------------------
    // 6. Latest policy per customer
    // ------------------------------------------------------------

    val latestPolicyWindow =
      Window
        .partitionBy("customer_id")
        .orderBy(desc("policy_date"))

    val chronologicalPolicyWindow =
      Window
        .partitionBy("customer_id")
        .orderBy(asc("policy_date"))

    val policyHistoryDF = policiesDF
      .withColumn("latest_row_number", row_number().over(latestPolicyWindow))
      .withColumn("previous_premium", lag("premium", 1).over(chronologicalPolicyWindow))
      .withColumn("next_premium", lead("premium", 1).over(chronologicalPolicyWindow))

    println("\n--- POLICY HISTORY WITH LAG / LEAD ---")
    policyHistoryDF
      .orderBy("customer_id", "policy_date")
      .show(false)

    val latestPolicyDF = policyHistoryDF
      .filter(col("latest_row_number") === 1)
      .drop("latest_row_number")
      .orderBy("customer_id")

    println("\n--- LATEST POLICY PER CUSTOMER ---")
    latestPolicyDF.show(false)

    // ------------------------------------------------------------
    // 7. Premium change using lag
    // ------------------------------------------------------------

    val premiumChangeDF = policyHistoryDF
      .withColumn(
        "premium_change",
        col("premium") - col("previous_premium")
      )

    println("\n--- PREMIUM CHANGE USING LAG ---")
    premiumChangeDF
      .orderBy("customer_id", "policy_date")
      .show(false)

    // ------------------------------------------------------------
    // 8. Spark SQL
    // ------------------------------------------------------------

    rankedStudentsDF.createOrReplaceTempView("ranked_students")
    latestPolicyDF.createOrReplaceTempView("latest_policies")

    println("\n--- SPARK SQL: TOP 3 STUDENTS PER COURSE ---")

    val top3SqlDF = spark.sql(
      """
        SELECT
          course,
          student,
          score,
          row_number,
          rank,
          dense_rank
        FROM ranked_students
        WHERE row_number <= 3
        ORDER BY course, row_number
      """
    )

    top3SqlDF.show(false)

    println("\n--- EXECUTION PLAN: TOP 3 STUDENTS ---")
    top3StudentsDF.explain(true)

    println("\n--- EXECUTION PLAN: LATEST POLICY ---")
    latestPolicyDF.explain(true)

    println("\n" + "=" * 75)
    println("DAY 17 COMPLETED SUCCESSFULLY")
    println("=" * 75)

    spark.stop()
  }
}
