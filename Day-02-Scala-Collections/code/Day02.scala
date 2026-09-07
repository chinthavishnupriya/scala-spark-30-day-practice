import scala.collection.immutable.{List, Vector, Map}

case class Sale(
  day: String,
  product: String,
  quantity: Int,
  price: Double
)

object Day02 {

  def main(args: Array[String]): Unit = {

    println("========================================")
    println("DAY 02 - SCALA COLLECTIONS")
    println("========================================")

    // --------------------------------------------------
    // 1. map
    // --------------------------------------------------
    println("\n--- 1. map ---")

    val numbers = List(1, 2, 3, 4, 5)
    val squaredNumbers = numbers.map(n => n * n)

    println(s"Original numbers : $numbers")
    println(s"Squared numbers  : $squaredNumbers")

    // --------------------------------------------------
    // 2. filter
    // --------------------------------------------------
    println("\n--- 2. filter ---")

    val evenNumbers = numbers.filter(_ % 2 == 0)

    println(s"Numbers          : $numbers")
    println(s"Even numbers     : $evenNumbers")

    // --------------------------------------------------
    // 3. flatMap
    // --------------------------------------------------
    println("\n--- 3. flatMap ---")

    val nestedNumbers = List(
      List(1, 2),
      List(3, 4),
      List(5, 6)
    )

    val flattenedNumbers = nestedNumbers.flatMap(identity)

    println(s"Nested List      : $nestedNumbers")
    println(s"After flatMap    : $flattenedNumbers")

    // --------------------------------------------------
    // 4. reduce
    // --------------------------------------------------
    println("\n--- 4. reduce ---")

    val total = numbers.reduce(_ + _)

    println(s"Numbers          : $numbers")
    println(s"Sum using reduce : $total")

    // --------------------------------------------------
    // 5. Vector
    // --------------------------------------------------
    println("\n--- 5. Vector ---")

    val products: Vector[String] =
      Vector("Laptop", "Mouse", "Keyboard", "Monitor")

    println(s"Products         : $products")
    println(s"First product    : ${products(0)}")
    println(s"Third product    : ${products(2)}")

    // --------------------------------------------------
    // 6. Map
    // --------------------------------------------------
    println("\n--- 6. Map ---")

    val productPrices: Map[String, Double] = Map(
      "Laptop" -> 60000.0,
      "Mouse" -> 800.0,
      "Keyboard" -> 1500.0,
      "Monitor" -> 12000.0
    )

    println(s"Product prices   : $productPrices")
    println(s"Laptop price     : ${productPrices("Laptop")}")

    // --------------------------------------------------
    // 7. for-comprehension
    // --------------------------------------------------
    println("\n--- 7. For-Comprehension ---")

    val students = List(
      ("Anu", 85),
      ("Bala", 72),
      ("Charan", 95),
      ("Divya", 62)
    )

    val passedStudents = for {
      (name, mark) <- students
      if mark >= 70
    } yield (name, mark)

    println("Students scoring 70 or above:")
    passedStudents.foreach(println)

    // --------------------------------------------------
    // 8. Daily Sales Dataset
    // --------------------------------------------------
    println("\n--- 8. Daily Sales Dataset ---")

    val sales = List(
      Sale("Monday", "Laptop", 2, 60000.0),
      Sale("Monday", "Mouse", 5, 800.0),
      Sale("Monday", "Keyboard", 3, 1500.0),
      Sale("Tuesday", "Laptop", 1, 60000.0),
      Sale("Tuesday", "Monitor", 2, 12000.0),
      Sale("Tuesday", "Mouse", 4, 800.0),
      Sale("Wednesday", "Keyboard", 5, 1500.0),
      Sale("Wednesday", "Monitor", 1, 12000.0),
      Sale("Wednesday", "Laptop", 1, 60000.0)
    )

    println("Sales records:")
    sales.foreach(println)

    // --------------------------------------------------
    // 9. Calculate revenue using map
    // --------------------------------------------------
    println("\n--- 9. Revenue Calculation using map ---")

    val salesWithRevenue = sales.map { sale =>
      val revenue = sale.quantity * sale.price
      (sale.day, sale.product, sale.quantity, revenue)
    }

    println("Day       Product     Quantity   Revenue")
    println("------------------------------------------")

    salesWithRevenue.foreach {
      case (day, product, quantity, revenue) =>
        println(f"$day%-9s$product%-12s$quantity%-11d₹$revenue%.2f")
    }

    // --------------------------------------------------
    // 10. Filter high-value sales
    // --------------------------------------------------
    println("\n--- 10. filter - High Value Sales ---")

    val highValueSales = salesWithRevenue.filter {
      case (_, _, _, revenue) => revenue >= 10000
    }

    highValueSales.foreach {
      case (day, product, quantity, revenue) =>
        println(
          f"$day%-9s$product%-12s$quantity%-11d₹$revenue%.2f"
        )
    }

    // --------------------------------------------------
    // 11. Daily Sales Summary
    // --------------------------------------------------
    println("\n--- 11. Daily Sales Summary ---")

    val dailyRevenue: Map[String, Double] =
      sales
        .map { sale =>
          sale.day -> (sale.quantity * sale.price)
        }
        .groupBy(_._1)
        .map {
          case (day, records) =>
            day -> records.map(_._2).sum
        }

    println("Day        Total Revenue")
    println("-----------------------")

    dailyRevenue.toSeq
      .sortBy(_._1)
      .foreach {
        case (day, revenue) =>
          println(f"$day%-10s₹$revenue%.2f")
      }

    // --------------------------------------------------
    // 12. Total Revenue using reduce
    // --------------------------------------------------
    println("\n--- 12. Total Revenue using reduce ---")

    val totalRevenue =
      sales
        .map(sale => sale.quantity * sale.price)
        .reduce(_ + _)

    println(f"Total revenue: ₹$totalRevenue%.2f")

    // --------------------------------------------------
    // 13. flatMap on product names
    // --------------------------------------------------
    println("\n--- 13. flatMap - Product Characters ---")

    val productCharacters =
      products.flatMap(_.toList)

    println(s"Products          : $products")
    println(s"Flattened letters : $productCharacters")

    // --------------------------------------------------
    // 14. Final Summary
    // --------------------------------------------------
    println("\n--- 14. Day 2 Summary ---")

    println(s"Number of sales records : ${sales.size}")
    println(s"Number of products      : ${products.size}")
    println(f"Total revenue           : ₹$totalRevenue%.2f")
    println("map, filter, flatMap, reduce, Vector, Map and")
    println("for-comprehension were successfully demonstrated.")

    println("\n========================================")
    println("DAY 02 COMPLETED SUCCESSFULLY")
    println("========================================")
  }
}
