import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions._
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.storage.StorageLevel
import org.apache.spark.util.LongAccumulator

object Day30 {
  private def batchCapstone(spark: SparkSession): Unit = {
    import spark.implicits._
    val orders = spark.read.option("header", "true").option("inferSchema", "true").csv("input/orders.csv")
    val products = spark.read.option("header", "true").option("inferSchema", "true").csv("input/products.csv")
    val customers = spark.read.option("header", "true").option("inferSchema", "true").csv("input/customers.csv")
    val validRowAccumulator: LongAccumulator = spark.sparkContext.longAccumulator("valid-completed-orders")
    val normalizeCategory = udf((category: String) => Option(category).map(_.trim.toLowerCase match { case "electronics" => "Electronics"; case "stationery" => "Stationery"; case other => other.capitalize }).getOrElse("Unknown"))
    val clean = orders.withColumn("order_date", to_date($"order_date")).withColumn("quantity", $"quantity".cast("int")).withColumn("unit_price", $"unit_price".cast("double")).withColumn("payment_method", upper(trim($"payment_method"))).withColumn("status", upper(trim($"status"))).filter($"order_id".isNotNull && $"customer_id".isNotNull && $"product_id".isNotNull).filter($"order_date".isNotNull && $"quantity" > 0 && $"unit_price" > 0).filter($"status" === "COMPLETED").withColumn("revenue", round($"quantity" * $"unit_price", 2)).cache()
    clean.rdd.foreach(_ => validRowAccumulator.add(1L))
    println(s"Valid completed orders counted by accumulator: \${validRowAccumulator.value}")
    val productRef = products.withColumn("category", normalizeCategory($"category")).dropDuplicates("product_id").cache()
    val customerRef = customers.dropDuplicates("customer_id")
    val enriched = clean.repartition(4, $"customer_id").join(broadcast(productRef), Seq("product_id"), "inner").join(customerRef, Seq("customer_id"), "inner").persist(StorageLevel.MEMORY_AND_DISK)
    println(s"Raw orders: \${orders.count()}")
    println(s"Clean completed orders: \${clean.count()}")
    println(s"Enriched orders: \${enriched.count()}")
    println(s"Enriched partitions: \${enriched.rdd.getNumPartitions}")
    println("\n--- Batch Pair RDD: Revenue by Payment Method ---")
    clean.rdd.map(r => (r.getAs[String]("payment_method"), r.getAs[Double]("revenue"))).reduceByKey(_ + _).sortByKey().collect().foreach { case (method, revenue) => println(f"$method%-6s -> ₹$revenue%.2f") }
    println("\n--- Batch SQL: Customer Revenue ---")
    enriched.createOrReplaceTempView("enriched_orders")
    spark.sql("""SELECT customer_id, customer_name, region, COUNT(DISTINCT order_id) AS orders, ROUND(SUM(revenue), 2) AS revenue FROM enriched_orders GROUP BY customer_id, customer_name, region ORDER BY revenue DESC""").show(false)
    println("\n--- Batch Window: Customer Order Ranking ---")
    val w = Window.partitionBy("customer_id").orderBy(desc("revenue"), asc("order_date"))
    enriched.withColumn("rank", row_number().over(w)).select("order_id", "customer_id", "product_name", "revenue", "rank").orderBy("customer_id", "rank").show(false)
    println("\n--- Batch Physical Plan ---")
    enriched.groupBy("region").agg(round(sum("revenue"), 2).alias("revenue")).explain("formatted")
    println("\nBatch concepts demonstrated: broadcast, accumulator, cache/persist, repartition, RDD, SQL, joins, aggregation, window, UDF.")
  }
  private def streamingCapstone(spark: SparkSession): Unit = {
    val ssc = new StreamingContext(spark.sparkContext, Seconds(5))
    ssc.checkpoint("output/checkpoint/day30")
    val statefulCount = spark.sparkContext.longAccumulator("stream-events-seen")
    val lines = ssc.socketTextStream("localhost", 9998)
    val events = lines.map(_.trim).filter(_.nonEmpty).map(_.split(",", -1)).filter(_.length == 8).map(a => (a(1), 1))
    val running = events.updateStateByKey[Int] { (values, previous) => Some(values.sum + previous.getOrElse(0)) }
    running.foreachRDD { (rdd, time) => val rows = rdd.collect().sortBy(_._1); if (rows.nonEmpty) { rows.foreach { case (customerId, count) => println(s"STREAM $time customer=$customerId events=$count") }; statefulCount.add(rows.map(_._2).sum.toLong) } }
    println("\n--- Streaming component started on localhost:9998 ---")
    println("Send CSV events with: nc localhost 9998 < input/stream-events.txt")
    println("Streaming batch interval: 5 seconds; state is maintained with updateStateByKey.")
    ssc.start(); ssc.awaitTerminationOrTimeout(30000); ssc.stop(stopSparkContext = false, stopGracefully = true)
    println(s"Streaming accumulator observed event count contribution: \${statefulCount.value}")
  }
  def main(args: Array[String]): Unit = {
    val mode = args.headOption.getOrElse("batch")
    val spark = SparkSession.builder().appName("Day 30 - Final E-Commerce Capstone").master("local[4]").getOrCreate()
    spark.sparkContext.setLogLevel("WARN")
    println("=============================================="); println("DAY 30 - FINAL E-COMMERCE CAPSTONE"); println("=============================================="); println(s"Mode: $mode")
    mode match { case "batch" => batchCapstone(spark); case "streaming" => streamingCapstone(spark); case "all" => batchCapstone(spark); streamingCapstone(spark); case other => println(s"Unknown mode: $other. Use batch, streaming, or all.") }
    spark.stop()
  }
}