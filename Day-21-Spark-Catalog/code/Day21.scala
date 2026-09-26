import org.apache.spark.sql.SparkSession

object Day21 {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .appName("Day 21 - Spark Catalog")
      .master("local[4]")
      .config("spark.sql.warehouse.dir", "output/spark-warehouse")
      .getOrCreate()

    spark.sparkContext.setLogLevel("WARN")

    println("==============================================")
    println("DAY 21 - SPARK CATALOG")
    println("==============================================")

    // 1. Create database
    println()
    println("--- 1. CREATE ANALYTICS DATABASE ---")

    spark.sql("CREATE DATABASE IF NOT EXISTS hotel_analytics")

    println("Database hotel_analytics created or already exists.")

    // 2. List databases
    println()
    println("--- 2. LIST DATABASES ---")

    spark.sql("SHOW DATABASES").show(false)

    // 3. Read hotel booking data
    println()
    println("--- 3. READ HOTEL BOOKINGS ---")

    val bookingsDF = spark.read
      .option("header", "true")
      .option("inferSchema", "true")
      .csv("input/hotel_bookings.csv")

    bookingsDF.show(false)

    println("Schema:")
    bookingsDF.printSchema()

    // 4. Create temporary view
    println()
    println("--- 4. CREATE TEMPORARY VIEW ---")

    bookingsDF.createOrReplaceTempView("hotel_bookings_view")

    println("Temporary view hotel_bookings_view created.")

    println()
    println("Temporary view query:")

    spark.sql("""
      SELECT hotel, city, SUM(amount) AS total_revenue
      FROM hotel_bookings_view
      GROUP BY hotel, city
      ORDER BY total_revenue DESC
    """).show(false)

    // 5. Create permanent catalog table
    println()
    println("--- 5. CREATE CATALOG TABLE ---")

    spark.sql("USE hotel_analytics")

    spark.sql("DROP TABLE IF EXISTS hotel_bookings")

    bookingsDF.write
      .mode("overwrite")
      .saveAsTable("hotel_bookings")

    println("Table hotel_analytics.hotel_bookings created.")

    // 6. List tables
    println()
    println("--- 6. LIST TABLES ---")

    spark.sql("SHOW TABLES").show(false)

    // 7. Query registered table
    println()
    println("--- 7. QUERY REGISTERED TABLE ---")

    spark.sql("""
      SELECT city,
             COUNT(*) AS bookings,
             SUM(amount) AS revenue
      FROM hotel_bookings
      GROUP BY city
      ORDER BY revenue DESC
    """).show(false)

    // 8. Describe table
    println()
    println("--- 8. DESCRIBE TABLE ---")

    spark.sql("DESCRIBE hotel_bookings").show(false)

    // 9. Describe extended
    println()
    println("--- 9. DESCRIBE EXTENDED ---")

    spark.sql("DESCRIBE EXTENDED hotel_bookings").show(50, false)

    // 10. Spark Catalog API
    println()
    println("--- 10. SPARK CATALOG API ---")

    println("Current database: " + spark.catalog.currentDatabase)

    println("Tables:")
    spark.catalog.listTables().show(false)

    println("Columns:")
    spark.catalog.listColumns("hotel_bookings").show(false)

    println(
      "hotel_bookings table type: " +
        spark.catalog.getTable("hotel_bookings").tableType
    )

    println(
      "hotel_bookings_view table type: " +
        spark.catalog.getTable("hotel_bookings_view").tableType
    )

    // 11. Catalog metadata summary
    println()
    println("--- 11. CATALOG METADATA SUMMARY ---")

    println(s"Database: ${spark.catalog.currentDatabase}")
    println(s"Table exists: ${spark.catalog.tableExists("hotel_bookings")}")
    println(
      s"Temporary view exists: ${spark.catalog.tableExists("hotel_bookings_view")}"
    )

    println()
    println("==============================================")
    println("DAY 21 COMPLETED SUCCESSFULLY")
    println("==============================================")

    spark.stop()
  }
}
