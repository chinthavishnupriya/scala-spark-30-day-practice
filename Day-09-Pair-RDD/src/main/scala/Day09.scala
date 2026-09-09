import org.apache.spark.sql.SparkSession

object Day09 {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .appName("Day 9 - Pair RDD")
      .master("local[4]")
      .config("spark.serializer", "org.apache.spark.serializer.JavaSerializer")
      .getOrCreate()

    spark.sparkContext.setLogLevel("WARN")

    println("========================================")
    println("DAY 09 - PAIR RDD")
    println("========================================")

    val inputPath = "input/transactions.txt"

    // --------------------------------------------------
    // 1. Read transaction data
    // --------------------------------------------------
    val transactionsRDD = spark.sparkContext
      .textFile(inputPath, 4)

    println(s"\nOriginal RDD partitions : ${transactionsRDD.getNumPartitions}")
    println(s"Transaction count      : ${transactionsRDD.count()}")

    // --------------------------------------------------
    // 2. Parse records
    // --------------------------------------------------
    val parsedTransactions = transactionsRDD.map { line =>
      val parts = line.split(",")

      val transactionId = parts(0)
      val accountId = parts(1)
      val product = parts(2)
      val quantity = parts(3).toInt
      val price = parts(4).toDouble

      (transactionId, accountId, product, quantity, price)
    }

    // --------------------------------------------------
    // 3. Create Pair RDD: product -> revenue
    // --------------------------------------------------
    val productRevenue = parsedTransactions.map {
      case (_, _, product, quantity, price) =>
        (product, quantity * price)
    }

    println("\n--- Product Revenue Using reduceByKey ---")

    val revenueByProduct = productRevenue
      .reduceByKey(_ + _)

    revenueByProduct
      .sortByKey()
      .collect()
      .foreach {
        case (product, revenue) =>
          println(f"$product%-12s ₹$revenue%.2f")
      }

    // --------------------------------------------------
    // 4. Department revenue
    // --------------------------------------------------
    val departmentRevenue = parsedTransactions.map {
      case (_, _, product, quantity, price) =>
        val department =
          if (product == "Electronics") "Electronics"
          else if (product == "Books") "Books"
          else "Clothing"

        (department, quantity * price)
    }

    println("\n--- Department Revenue ---")

    departmentRevenue
      .reduceByKey(_ + _)
      .sortByKey()
      .collect()
      .foreach {
        case (department, revenue) =>
          println(f"$department%-12s ₹$revenue%.2f")
      }

    // --------------------------------------------------
    // 5. Demonstrate mapValues
    // --------------------------------------------------
    val quantityByProduct = parsedTransactions.map {
      case (_, _, product, quantity, _) =>
        (product, quantity)
    }

    val doubledQuantity = quantityByProduct
      .mapValues(quantity => quantity * 2)
      .reduceByKey(_ + _)

    println("\n--- mapValues Demonstration ---")
    println("The quantity values are doubled using mapValues.")

    doubledQuantity
      .sortByKey()
      .collect()
      .foreach {
        case (product, quantity) =>
          println(f"$product%-12s $quantity")
      }

    // --------------------------------------------------
    // 6. Demonstrate groupByKey
    // --------------------------------------------------
    val revenueValuesByProduct = parsedTransactions.map {
      case (_, _, product, quantity, price) =>
        (product, quantity * price)
    }

    println("\n--- groupByKey Demonstration ---")

    revenueValuesByProduct
      .groupByKey()
      .sortByKey()
      .collect()
      .foreach {
        case (product, values) =>
          val valuesList = values.toList
          val total = valuesList.sum

          println(
            f"$product%-12s values=${valuesList.mkString("[", ", ", "]")} total=₹$total%.2f"
          )
      }

    // --------------------------------------------------
    // 7. reduceByKey vs groupByKey
    // --------------------------------------------------
    println("\n--- reduceByKey vs groupByKey ---")

    println("reduceByKey:")
    println("  Combines values locally before shuffle.")
    println("  Usually more efficient for aggregation.")
    println("  Sends less data across the network.")

    println("\ngroupByKey:")
    println("  Groups all values for each key.")
    println("  Can move more data across the network.")
    println("  Useful when all individual values are required.")

    // --------------------------------------------------
    // 8. Bank transaction aggregation by account ID
    // --------------------------------------------------
    val accountTransactions = parsedTransactions.map {
      case (transactionId, accountId, _, quantity, price) =>
        val amount = quantity * price
        (accountId, amount)
    }

    println("\n--- Bank Transaction Aggregation by Account ID ---")

    val accountTotals = accountTransactions
      .reduceByKey(_ + _)
      .sortByKey()

    accountTotals.collect().foreach {
      case (accountId, total) =>
        println(f"$accountId%-8s ₹$total%.2f")
    }

    // --------------------------------------------------
    // 9. Account transaction counts
    // --------------------------------------------------
    val transactionCountByAccount = parsedTransactions
      .map {
        case (_, accountId, _, _, _) =>
          (accountId, 1)
      }
      .reduceByKey(_ + _)
      .sortByKey()

    println("\n--- Transaction Count by Account ---")

    transactionCountByAccount.collect().foreach {
      case (accountId, count) =>
        println(f"$accountId%-8s $count transactions")
    }

    // --------------------------------------------------
    // 10. Pair RDD concepts
    // --------------------------------------------------
    println("\n--- Pair RDD Concepts ---")

    println("Pair RDD:")
    println("  An RDD containing key-value pairs such as (key, value).")

    println("reduceByKey:")
    println("  Aggregates values belonging to the same key.")

    println("groupByKey:")
    println("  Groups all values belonging to the same key.")

    println("mapValues:")
    println("  Changes only the values while preserving the keys.")

    println("\n--- Execution Summary ---")

    println("Pair RDD operations:")
    println("  reduceByKey")
    println("  groupByKey")
    println("  mapValues")

    println("Aggregation examples:")
    println("  Revenue by product")
    println("  Revenue by department")
    println("  Transactions by account ID")

    println("\n========================================")
    println("DAY 09 COMPLETED SUCCESSFULLY")
    println("========================================")

    spark.stop()
  }
}
