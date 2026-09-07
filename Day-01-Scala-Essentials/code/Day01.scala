import scala.collection.immutable.{List, Vector, Set, Map}

case class Student(name: String, marks: List[Int])

trait Logger {
  def log(message: String): Unit
}

class ConsoleLogger extends Logger {
  override def log(message: String): Unit =
    println(s"[ConsoleLogger] $message")
}

class AuditLogger extends Logger {
  override def log(message: String): Unit =
    println(s"[AuditLogger] $message")
}

object Day01 {

  def calculateAverage(marks: List[Int]): Double =
    marks.sum.toDouble / marks.size

  def calculateGrade(average: Double): String = {
    if (average >= 90) "A+"
    else if (average >= 80) "A"
    else if (average >= 70) "B"
    else if (average >= 60) "C"
    else if (average >= 50) "D"
    else "F"
  }

  def main(args: Array[String]): Unit = {

    println("========================================")
    println("DAY 01 - SCALA ESSENTIALS")
    println("========================================")

    // 1. val, var and lazy val
    println("\n--- 1. val, var and lazy val ---")

    val courseName = "Scala Essentials"
    var practiceCount = 1

    println(s"val courseName = $courseName")

    practiceCount += 1
    println(s"var practiceCount after update = $practiceCount")

    lazy val expensiveMessage = {
      println("lazy val is being initialized now")
      "Lazy computation completed"
    }

    println("lazy val declared but not accessed yet")
    println(s"First access: $expensiveMessage")
    println(s"Second access: $expensiveMessage")

    // 2. Immutable collections
    println("\n--- 2. Immutable Collections ---")

    val numbers = List(10, 20, 30, 40, 50)
    val doubledNumbers = numbers.map(_ * 2)

    println(s"Original immutable List : $numbers")
    println(s"New List after map      : $doubledNumbers")

    // 3. For-comprehension with yield
    println("\n--- 3. For-Comprehension with yield ---")

    val students = List(
      Student("Anu", List(85, 90, 88)),
      Student("Bala", List(72, 75, 70)),
      Student("Charan", List(95, 92, 96)),
      Student("Divya", List(60, 65, 62))
    )

    val studentMarks = for {
      student <- students
      mark <- student.marks
    } yield (student.name, mark)

    println("Student and individual marks:")
    studentMarks.foreach(println)

    // 4. List, Vector, Set and Map
    println("\n--- 4. List, Vector, Set and Map ---")

    val studentList: List[String] =
      List("Anu", "Bala", "Charan", "Divya")

    val studentVector: Vector[String] =
      Vector("Anu", "Bala", "Charan", "Divya")

    val studentSet: Set[String] =
      Set("Anu", "Bala", "Charan", "Divya", "Anu")

    val studentMap: Map[String, Int] =
      Map(
        "Anu" -> 85,
        "Bala" -> 72,
        "Charan" -> 95,
        "Divya" -> 60
      )

    println(s"List   : $studentList")
    println(s"Vector : $studentVector")
    println(s"Set    : $studentSet")
    println(s"Map    : $studentMap")

    println("\nCollection observations:")
    println("- List   : ordered collection, efficient sequential access")
    println("- Vector : indexed collection with efficient random access")
    println("- Set    : stores unique elements")
    println("- Map    : stores key-value pairs")

    // 5. Logger trait with two implementations
    println("\n--- 5. Logger Trait ---")

    val consoleLogger: Logger = new ConsoleLogger
    val auditLogger: Logger = new AuditLogger

    consoleLogger.log("Student processing started")
    auditLogger.log("Student processing recorded")

    // 6. Student-grade processor
    println("\n--- 6. Student Grade Processor ---")

    val gradeReport = students.map { student =>
      val total = student.marks.sum
      val average = calculateAverage(student.marks)
      val grade = calculateGrade(average)

      (student.name, total, average, grade)
    }

    println("Name     Total   Average   Grade")
    println("---------------------------------")

    gradeReport.foreach {
      case (name, total, average, grade) =>
        println(f"$name%-8s$total%-8d$average%-10.2f$grade")
    }

    // 7. Filtering students
    println("\n--- 7. Students with Grade A or A+ ---")

    val topStudents = gradeReport.filter {
      case (_, _, _, grade) =>
        grade == "A" || grade == "A+"
    }

    topStudents.foreach {
      case (name, _, average, grade) =>
        println(f"$name%-8s Average: $average%.2f Grade: $grade")
    }

    // 8. Final summary
    println("\n--- 8. Day 1 Summary ---")

    println(s"Number of students processed: ${students.size}")
    println(s"Number of top students      : ${topStudents.size}")
    println("All processing completed using immutable Scala collections.")

    println("\n========================================")
    println("DAY 01 COMPLETED SUCCESSFULLY")
    println("========================================")
  }
}
