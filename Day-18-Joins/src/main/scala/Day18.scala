import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

object Day18 {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .appName("Day-18-Joins")
      .master("local[4]")
      .getOrCreate()

    spark.sparkContext.setLogLevel("WARN")
// Disable automatic broadcasting so the physical plan can demonstrate
// Shuffle Sort-Merge Join for educational purposes.
    spark.conf.set("spark.sql.autoBroadcastJoinThreshold", -1)
    println("=" * 75)
    println("DAY 18 - JOINS")
    println("=" * 75)

    // ------------------------------------------------------------
    // 1. Read input data
    // ------------------------------------------------------------

    val customersDF = spark.read
      .option("header", "true")
      .option("inferSchema", "true")
      .csv("input/customers.csv")

    val ordersDF = spark.read
      .option("header", "true")
      .option("inferSchema", "true")
      .csv("input/orders.csv")

    val paymentsDF = spark.read
      .option("header", "true")
      .option("inferSchema", "true")
      .csv("input/payments.csv")

    println("\n--- CUSTOMERS ---")
    customersDF.show(false)

    println("\n--- ORDERS ---")
    ordersDF.show(false)

    println("\n--- PAYMENTS ---")
    paymentsDF.show(false)

    println("\n--- RECORD COUNTS ---")
    println(s"Customers: ${customersDF.count()}")
    println(s"Orders: ${ordersDF.count()}")
    println(s"Payments: ${paymentsDF.count()}")

    // ------------------------------------------------------------
    // 2. Inner Join
    // ------------------------------------------------------------

    val customerOrdersInnerDF =
      customersDF.alias("c")
        .join(
          ordersDF.alias("o"),
          col("c.customer_id") === col("o.customer_id"),
          "inner"
        )
        .select(
          col("c.customer_id"),
          col("c.customer_name"),
          col("c.city"),
          col("o.order_id"),
          col("o.product"),
          col("o.amount")
        )

    println("\n--- INNER JOIN: CUSTOMERS + ORDERS ---")
    customerOrdersInnerDF.show(false)

    // ------------------------------------------------------------
    // 3. Left Join
    // ------------------------------------------------------------

    val customerOrdersLeftDF =
      customersDF.alias("c")
        .join(
          ordersDF.alias("o"),
          col("c.customer_id") === col("o.customer_id"),
          "left"
        )
        .select(
          col("c.customer_id"),
          col("c.customer_name"),
          col("o.order_id"),
          col("o.product"),
          col("o.amount")
        )
        .orderBy("customer_id", "order_id")

    println("\n--- LEFT JOIN: CUSTOMERS + ORDERS ---")
    customerOrdersLeftDF.show(false)

    // ------------------------------------------------------------
    // 4. Null handling after LEFT JOIN
    // ------------------------------------------------------------

    val customerOrdersNullHandledDF =
      customerOrdersLeftDF
        .withColumn(
          "order_status",
          when(col("order_id").isNull, lit("No Order"))
            .otherwise(lit("Has Order"))
        )
        .withColumn(
          "order_amount",
          coalesce(col("amount"), lit(0))
        )

    println("\n--- LEFT JOIN WITH NULL HANDLING ---")
    customerOrdersNullHandledDF.show(false)

    // ------------------------------------------------------------
    // 5. Right Join
    // ------------------------------------------------------------

    val customerOrdersRightDF =
      customersDF.alias("c")
        .join(
          ordersDF.alias("o"),
          col("c.customer_id") === col("o.customer_id"),
          "right"
        )
        .select(
          col("c.customer_id"),
          col("c.customer_name"),
          col("o.order_id"),
          col("o.product"),
          col("o.amount")
        )
        .orderBy("order_id")

    println("\n--- RIGHT JOIN: CUSTOMERS + ORDERS ---")
    customerOrdersRightDF.show(false)

    // ------------------------------------------------------------
    // 6. Full Outer Join
    // ------------------------------------------------------------

    val customerOrdersFullDF =
      customersDF.alias("c")
        .join(
          ordersDF.alias("o"),
          col("c.customer_id") === col("o.customer_id"),
          "full"
        )
        .select(
          col("c.customer_id"),
          col("c.customer_name"),
          col("o.customer_id").alias("order_customer_id"),
          col("o.order_id"),
          col("o.product"),
          col("o.amount")
        )
        .orderBy("customer_id", "order_customer_id")

    println("\n--- FULL OUTER JOIN: CUSTOMERS + ORDERS ---")
    customerOrdersFullDF.show(false)

    // ------------------------------------------------------------
    // 7. Orders + Payments
    // ------------------------------------------------------------

    val ordersPaymentsDF =
      ordersDF.alias("o")
        .join(
          paymentsDF.alias("p"),
          col("o.order_id") === col("p.order_id"),
          "left"
        )
        .select(
          col("o.order_id"),
          col("o.customer_id"),
          col("o.product"),
          col("o.amount"),
          col("p.payment_method"),
          col("p.payment_status")
        )
        .orderBy("order_id")

    println("\n--- ORDERS + PAYMENTS ---")
    ordersPaymentsDF.show(false)

    // ------------------------------------------------------------
    // 8. Complete scenario: Customers + Orders + Payments
    // ------------------------------------------------------------

    val completeOrdersDF =
      customersDF.alias("c")
        .join(
          ordersDF.alias("o"),
          col("c.customer_id") === col("o.customer_id"),
          "left"
        )
        .join(
          paymentsDF.alias("p"),
          col("o.order_id") === col("p.order_id"),
          "left"
        )
        .select(
          col("c.customer_id"),
          col("c.customer_name"),
          col("c.city"),
          col("o.order_id"),
          col("o.product"),
          col("o.amount"),
          col("p.payment_method"),
          col("p.payment_status")
        )
        .orderBy("customer_id", "order_id")

    println("\n--- COMPLETE CUSTOMER + ORDER + PAYMENT JOIN ---")
    completeOrdersDF.show(false)

    // ------------------------------------------------------------
    // 9. Spark SQL Join
    // ------------------------------------------------------------

    customersDF.createOrReplaceTempView("customers")
    ordersDF.createOrReplaceTempView("orders")
    paymentsDF.createOrReplaceTempView("payments")

    val sqlJoinDF = spark.sql(
      """
        SELECT
          c.customer_id,
          c.customer_name,
          o.order_id,
          o.product,
          o.amount,
          p.payment_method,
          p.payment_status
        FROM customers c
        LEFT JOIN orders o
          ON c.customer_id = o.customer_id
        LEFT JOIN payments p
          ON o.order_id = p.order_id
        ORDER BY c.customer_id, o.order_id
      """
    )

    println("\n--- SPARK SQL: COMPLETE JOIN ---")
    sqlJoinDF.show(false)

    // ------------------------------------------------------------
    // 10. Execution plan
    // ------------------------------------------------------------

    println("\n--- EXECUTION PLAN: COMPLETE JOIN ---")
    completeOrdersDF.explain(true)

    println("\n" + "=" * 75)
    println("DAY 18 COMPLETED SUCCESSFULLY")
    println("=" * 75)

    spark.stop()
  }
}
