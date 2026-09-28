import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions._

object Day29 {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().appName("Day 29 - End-to-End E-Commerce Project").master("local[4]").getOrCreate()
    spark.sparkContext.setLogLevel("WARN")
    import spark.implicits._

    val rawOrders = spark.read.option("header", "true").option("inferSchema", "true").csv("input/orders.csv")
    val products = spark.read.option("header", "true").option("inferSchema", "true").csv("input/products.csv")
    val customers = spark.read.option("header", "true").option("inferSchema", "true").csv("input/customers.csv")

    println("==============================================")
    println("DAY 29 - END-TO-END E-COMMERCE PROJECT")
    println("==============================================")
    println("Raw orders : " + rawOrders.count())
    println("Products   : " + products.count())
    println("Customers  : " + customers.count())

    val normalizeCategory = udf((category: String) =>
      Option(category).map(_.trim.toLowerCase match {
        case "legacy-electronics" => "Electronics"
        case "legacy-stationery"  => "Stationery"
        case other                => other.capitalize
      }).getOrElse("Unknown")
    )

    val cleanOrders = rawOrders
      .withColumn("order_date", to_date($"order_date"))
      .withColumn("quantity", col("quantity").cast("int"))
      .withColumn("unit_price", col("unit_price").cast("double"))
      .withColumn("payment_method", upper(trim($"payment_method")))
      .withColumn("status", upper(trim($"status")))
      .filter(col("order_id").isNotNull && col("customer_id").isNotNull && col("product_id").isNotNull && col("order_date").isNotNull && col("quantity") > 0 && col("unit_price") > 0 && col("status") === "COMPLETED")
      .withColumn("revenue", round(col("quantity") * col("unit_price"), 2))
      .cache()

    println("Clean completed orders: " + cleanOrders.count())

    val normalizedProducts = products.withColumn("category", normalizeCategory(col("category"))).dropDuplicates("product_id").cache()

    val enriched = cleanOrders
      .repartition(4, col("customer_id"))
      .join(normalizedProducts, Seq("product_id"), "inner")
      .join(customers.dropDuplicates("customer_id"), Seq("customer_id"), "inner")
      .cache()

    println("Enriched rows: " + enriched.count())

    val paymentRevenue = cleanOrders.rdd
      .map(row => (row.getAs[String]("payment_method"), row.getAs[Double]("revenue")))
      .reduceByKey(_ + _).sortByKey()

    println("\n--- Pair RDD: Revenue by Payment Method ---")
    paymentRevenue.collect().foreach { case (method, revenue) => println(f"$method%-6s -> ₹$revenue%.2f") }

    val customerSummary = enriched.groupBy("customer_id", "customer_name", "region")
      .agg(countDistinct("order_id").alias("order_count"), sum("quantity").alias("items"), round(sum("revenue"), 2).alias("total_revenue"))
      .orderBy(desc("total_revenue"))

    println("\n--- DataFrame: Customer Revenue Summary ---")
    customerSummary.show(false)

    val customerWindow = Window.partitionBy("customer_id").orderBy(desc("revenue"), asc("order_date"))
    val rankedOrders = enriched.withColumn("customer_order_rank", row_number().over(customerWindow))
      .select("order_id", "customer_id", "customer_name", "product_name", "category", "order_date", "revenue", "customer_order_rank")
      .orderBy("customer_id", "customer_order_rank")

    println("\n--- Window: Orders Ranked Within Customer ---")
    rankedOrders.show(false)

    val categorySummary = enriched.groupBy("category").agg(countDistinct("order_id").alias("orders"), round(sum("revenue"), 2).alias("revenue")).orderBy(desc("revenue"))
    println("\n--- Enriched Category Summary ---")
    categorySummary.show(false)

    println("\n--- Physical Plan / Shuffle Inspection ---")
    customerSummary.explain("formatted")

    println("\n--- Pipeline Summary ---")
    println("Raw -> Clean -> Enrich -> Aggregate")
    println("RDD/Pair RDD: payment-method revenue")
    println("DataFrame: joins, customer/category aggregates, window ranking")
    println("UDF: legacy category normalization only")
    println("Persistence: cleanOrders, normalizedProducts, enriched")
    println("Partitioning: enriched.repartition(4, customer_id)")
    println("Shuffle points: Pair RDD reduceByKey, repartition, joins, groupBy, window")
    println("Spark SQL/DataFrame optimizer: Catalyst + physical plan inspection")
    spark.stop()
  }
}
