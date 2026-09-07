import org.apache.spark.sql.SparkSession

object Day03 {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .appName("Day 3 - Spark Setup")
      .master(s"local[${if (args.nonEmpty) args(0) else "2"}]")
      .getOrCreate()

    val sc = spark.sparkContext

    println("========================================")
    println("DAY 03 - SPARK SETUP")
    println("========================================")

    println(s"Spark version    : ${spark.version}")
    println(s"Application name : ${spark.sparkContext.appName}")
    println(s"Master           : ${spark.sparkContext.master}")
    println(s"Default parallelism : ${sc.defaultParallelism}")

    println()
    println("--- Reading input/sample.txt ---")

    val lines = sc.textFile("input/sample.txt")

    println(s"Number of lines : ${lines.count()}")

    lines.collect().foreach { line =>
      println(line)
    }

    println()
    println("========================================")
    println("DAY 03 APPLICATION COMPLETED")
    println("========================================")

    spark.stop()
  }
}
