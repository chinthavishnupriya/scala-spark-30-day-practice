import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions._
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.storage.StorageLevel
import org.apache.spark.util.LongAccumulator

object Day30 {
  private def batchCapstone(spark: SparkSession): Unit = {
    import spark.implicits._

    val assessments = spark.read.option("header", "true").option("inferSchema", "true").csv("input/assessments.csv")
    val courses = spark.read.option("header", "true").option("inferSchema", "true").csv("input/courses.csv")
    val profiles = spark.read.option("header", "true").option("inferSchema", "true").csv("input/student-profiles.csv")

    val validRecordAccumulator: LongAccumulator =
      spark.sparkContext.longAccumulator("valid-assessment-records")

    val performanceBand = udf((score: Double) =>
      if (score >= 85) "Excellent"
      else if (score >= 70) "Good"
      else if (score >= 50) "Average"
      else "Needs Improvement"
    )

    val cleanAssessments = assessments
      .withColumn("assessment_date", to_date($"assessment_date"))
      .withColumn("marks", $"marks".cast("double"))
      .withColumn("max_marks", $"max_marks".cast("double"))
      .withColumn("attendance_pct", $"attendance_pct".cast("double"))
      .withColumn("assessment_type", upper(trim($"assessment_type")))
      .withColumn("status", upper(trim($"status")))
      .filter($"student_id".isNotNull && $"course_id".isNotNull)
      .filter($"assessment_date".isNotNull)
      .filter($"max_marks" > 0 && $"marks" >= 0 && $"marks" <= $"max_marks")
      .filter($"status" === "VALID")
      .withColumn("score_pct", round($"marks" / $"max_marks" * 100, 2))
      .withColumn("performance_band", performanceBand($"score_pct"))
      .cache()

    cleanAssessments.rdd.foreach(_ => validRecordAccumulator.add(1L))

    println("==============================================")
    println("DAY 30 - EDUCATION ANALYTICS CAPSTONE")
    println("==============================================")
    println(s"Raw assessment records: ${assessments.count()}")
    println(s"Valid assessment records: ${cleanAssessments.count()}")
    println(s"Accumulator value: ${validRecordAccumulator.value}")

    val courseRef = courses.dropDuplicates("course_id").cache()
    val studentRef = profiles.dropDuplicates("student_id")

    val enriched = cleanAssessments
      .repartition(4, $"student_id")
      .join(broadcast(courseRef), Seq("course_id"), "inner")
      .join(studentRef, Seq("student_id"), "inner")
      .persist(StorageLevel.MEMORY_AND_DISK)

    println(s"Enriched records: ${enriched.count()}")
    println(s"Enriched partitions: ${enriched.rdd.getNumPartitions}")

    println("\n--- Pair RDD: Total Marks by Course ---")
    cleanAssessments.rdd
      .map(r => (r.getAs[String]("course_id"), r.getAs[Double]("marks")))
      .reduceByKey(_ + _)
      .sortByKey()
      .collect()
      .foreach { case (course, marks) => println(f"$course -> $marks%.2f") }

    println("\n--- Spark SQL: Student Performance ---")
    enriched.createOrReplaceTempView("education_assessments")
    spark.sql("""
      SELECT student_id, student_name, semester, section,
             COUNT(*) AS assessments,
             ROUND(AVG(score_pct), 2) AS average_score
      FROM education_assessments
      GROUP BY student_id, student_name, semester, section
      ORDER BY average_score DESC
    """).show(false)

    println("\n--- Window: Ranking Students within Each Course ---")
    val courseWindow = Window.partitionBy("course_id").orderBy(desc("score_pct"), asc("student_id"))
    enriched.withColumn("course_rank", row_number().over(courseWindow))
      .select("course_id", "course_name", "student_id", "student_name",
              "assessment_type", "score_pct", "performance_band", "course_rank")
      .orderBy("course_id", "course_rank")
      .show(false)

    println("\n--- Physical Plan ---")
    enriched.groupBy("department")
      .agg(round(avg("score_pct"), 2).alias("average_score"))
      .explain("formatted")
  }

  private def streamingCapstone(spark: SparkSession): Unit = {
    val ssc = new StreamingContext(spark.sparkContext, Seconds(5))
    ssc.checkpoint("output/checkpoint/day30")
    val streamAccumulator = spark.sparkContext.longAccumulator("attendance-events-observed")
    val lines = ssc.socketTextStream("localhost", 9998)

    val events = lines.map(_.trim).filter(_.nonEmpty)
      .map(_.split(",", -1)).filter(_.length == 5)
      .map(a => (a(0), 1))

    val runningCounts = events.updateStateByKey[Int] {
      (values, previous) => Some(values.sum + previous.getOrElse(0))
    }

    runningCounts.foreachRDD { (rdd, time) =>
      val rows = rdd.collect().sortBy(_._1)
      if (rows.nonEmpty) {
        println(s"\nSTREAM $time")
        rows.foreach { case (studentId, count) =>
          println(s"student=$studentId attendance-events=$count")
        }
        streamAccumulator.add(rows.map(_._2).sum.toLong)
      }
    }

    println("\n--- Streaming component started on localhost:9998 ---")
    println("Send: cat input/attendance-events.txt | nc localhost 9998")
    println("Batch interval: 5 seconds; state uses updateStateByKey.")
    ssc.start()
    ssc.awaitTerminationOrTimeout(30000)
    ssc.stop(stopSparkContext = false, stopGracefully = true)
    println(s"Streaming accumulator contribution: ${streamAccumulator.value}")
  }

  def main(args: Array[String]): Unit = {
    val mode = args.headOption.getOrElse("batch")
    val spark = SparkSession.builder()
      .appName("Day 30 - Education Analytics Capstone")
      .master("local[4]")
      .getOrCreate()
    spark.sparkContext.setLogLevel("WARN")

    mode match {
      case "batch" => batchCapstone(spark)
      case "streaming" => streamingCapstone(spark)
      case "all" => batchCapstone(spark); streamingCapstone(spark)
      case other => println(s"Unknown mode: $other. Use batch, streaming, or all.")
    }
    spark.stop()
  }
}
