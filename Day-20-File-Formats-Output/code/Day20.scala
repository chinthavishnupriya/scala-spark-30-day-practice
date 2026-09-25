import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

import java.io.File

object Day20 {

  // Recursively find data files inside an output directory.
  def dataFiles(path: String, extension: String): Seq[File] = {

    def loop(file: File): Seq[File] = {
      if (!file.exists()) {
        Seq.empty
      } else if (file.isFile && file.getName.endsWith(extension)) {
        Seq(file)
      } else if (file.isDirectory) {
        Option(file.listFiles())
          .toSeq
          .flatten
          .flatMap(loop)
      } else {
        Seq.empty
      }
    }

    loop(new File(path))
  }

  // Print the complete directory/file layout recursively.
  def printLayout(path: String): Unit = {

    def loop(file: File, prefix: String): Unit = {

      if (file.isDirectory) {
        println(s"$prefix${file.getName}/")

        Option(file.listFiles())
          .toSeq
          .flatten
          .sortBy(_.getName)
          .foreach(child => loop(child, prefix + "  "))

      } else {
        println(s"$prefix${file.getName}")
      }
    }

    loop(new File(path), "")
  }

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .appName("Day20-File-Formats-Output")
      .master("local[4]")
      .getOrCreate()

    spark.sparkContext.setLogLevel("WARN")

    val inputPath = "input/daily_sales.csv"

    val csvOutput = "output/day20_csv"
    val jsonOutput = "output/day20_json"
    val parquetOutput = "output/day20_parquet"
    val partitionedOutput = "output/day20_partitioned_parquet"

    println("\n==============================================")
    println("DAY 20 - FILE FORMATS AND OUTPUT")
    println("==============================================")

    // ------------------------------------------------------------
    // READ CSV
    // ------------------------------------------------------------

    println("\n--- READING CSV INPUT ---")

    val salesDF = spark.read
      .option("header", "true")
      .option("inferSchema", "true")
      .csv(inputPath)

    salesDF.show(false)

    println(s"Input record count: ${salesDF.count()}")

    println("\nInput schema:")
    salesDF.printSchema()

    // ------------------------------------------------------------
    // WRITE CSV
    // ------------------------------------------------------------

    println("\n--- WRITING CSV ---")

    salesDF.write
      .mode("overwrite")
      .option("header", "true")
      .csv(csvOutput)

    println(s"CSV output written to: $csvOutput")
    println(s"CSV data files: ${dataFiles(csvOutput, ".csv").size}")

    // ------------------------------------------------------------
    // WRITE JSON
    // ------------------------------------------------------------

    println("\n--- WRITING JSON ---")

    salesDF.write
      .mode("overwrite")
      .json(jsonOutput)

    println(s"JSON output written to: $jsonOutput")
    println(s"JSON data files: ${dataFiles(jsonOutput, ".json").size}")

    // ------------------------------------------------------------
    // WRITE PARQUET
    // ------------------------------------------------------------

    println("\n--- WRITING PARQUET ---")

    salesDF.write
      .mode("overwrite")
      .parquet(parquetOutput)

    println(s"Parquet output written to: $parquetOutput")
    println(s"Parquet data files: ${dataFiles(parquetOutput, ".parquet").size}")

    // ------------------------------------------------------------
    // READ PARQUET BACK
    // ------------------------------------------------------------

    println("\n--- READING PARQUET BACK ---")

    val parquetDF = spark.read
      .parquet(parquetOutput)

    parquetDF.show(false)

    println(s"Parquet record count: ${parquetDF.count()}")

    // ------------------------------------------------------------
    // ADD YEAR / MONTH / DAY
    // ------------------------------------------------------------

    println("\n--- ADDING YEAR / MONTH / DAY COLUMNS ---")

    val datedSalesDF = salesDF
      .withColumn("sale_date", to_date(col("sale_date")))
      .withColumn("year", year(col("sale_date")))
      .withColumn("month", month(col("sale_date")))
      .withColumn("day", dayofmonth(col("sale_date")))

    datedSalesDF.show(false)

    println("\nDate-partitioned schema:")
    datedSalesDF.printSchema()

    // ------------------------------------------------------------
    // REPARTITION BEFORE WRITING
    // ------------------------------------------------------------

    println("\n--- REPARTITIONING DATA ---")

    val repartitionedDF = datedSalesDF
      .repartition(3, col("year"), col("month"), col("day"))

    println(s"Partitions after repartition: ${repartitionedDF.rdd.getNumPartitions}")

    // ------------------------------------------------------------
    // WRITE PARTITIONED PARQUET
    // ------------------------------------------------------------

    println("\n--- WRITING PARTITIONED PARQUET ---")

    repartitionedDF.write
      .mode("overwrite")
      .partitionBy("year", "month", "day")
      .parquet(partitionedOutput)

    println(s"\nPartitioned Parquet output written to: $partitionedOutput")

    // ------------------------------------------------------------
    // SHOW OUTPUT LAYOUT
    // ------------------------------------------------------------

    println("\n--- PARTITIONED OUTPUT LAYOUT ---")

    printLayout(partitionedOutput)

    println(
      s"Partitioned Parquet data files: ${dataFiles(partitionedOutput, ".parquet").size}"
    )

    // ------------------------------------------------------------
    // EXPLAIN PLAN
    // ------------------------------------------------------------

    println("\n--- EXECUTION PLAN ---")

    datedSalesDF.explain(true)

    println("\n==============================================")
    println("DAY 20 COMPLETED SUCCESSFULLY")
    println("==============================================")

    spark.stop()
  }
}
