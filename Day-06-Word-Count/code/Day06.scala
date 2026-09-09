import org.apache.spark.sql.SparkSession

object Day06 {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .appName("Day 06 - Word Count")
      .master("local[4]")
      .getOrCreate()

    spark.sparkContext.setLogLevel("WARN")

    val sc = spark.sparkContext

    println("========================================")
    println("DAY 06 - WORD COUNT")
    println("========================================")

    val logs = sc.textFile("input/application.log", 4)

    println(s"Input partitions: ${logs.getNumPartitions}")

    val classicWordCounts = logs
      .flatMap(_.split("\\s+"))
      .filter(_.nonEmpty)
      .map(word => (word, 1))
      .reduceByKey(_ + _)

    println("\n--- Classic Word Count ---")

    classicWordCounts
      .sortByKey()
      .collect()
      .foreach { case (word, count) =>
        println(f"$word%-20s $count")
      }

    val normalizedWords = logs
      .flatMap { line =>
        line
          .toLowerCase
          .replaceAll("[^a-z0-9]+", " ")
          .split("\\s+")
      }
      .filter(_.nonEmpty)

    val wordCounts = normalizedWords
      .map(word => (word, 1))
      .reduceByKey(_ + _)

    println("\n--- Case-Insensitive Word Count ---")

    wordCounts
      .sortByKey()
      .collect()
      .foreach { case (word, count) =>
        println(f"$word%-20s $count")
      }

    val top10Words = wordCounts
      .sortBy(
        { case (_, count) => count },
        ascending = false
      )
      .take(10)

    println("\n--- Top 10 Most Frequent Words ---")

    top10Words.zipWithIndex.foreach {
      case ((word, count), index) =>
        println(f"${index + 1}%2d. $word%-20s $count")
    }

    println("\n--- Spark Execution Concepts ---")
    println("Transformations:")
    println("  flatMap, filter, map, reduceByKey, sortByKey, sortBy")

    println("\nActions:")
    println("  collect, take")

    println("\nLazy transformations:")
    println("  flatMap, filter, map, reduceByKey, sortByKey, sortBy")

    println("\nShuffle operations:")
    println("  reduceByKey")
    println("  sortByKey / sortBy")

    println("\nOptimization:")
    println("  reduceByKey performs local aggregation before shuffle.")
    println("  take(10) avoids collecting the complete result.")

    println("\n========================================")
    println("DAY 06 APPLICATION COMPLETED")
    println("========================================")

    spark.stop()
  }
}
