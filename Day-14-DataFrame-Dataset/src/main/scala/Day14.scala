import org.apache.spark.sql.{Dataset, SparkSession}
import org.apache.spark.sql.functions._

case class Employee(
    employee_id: String,
    name: String,
    department: String,
    age: Int,
    salary: Long
)

object Day14 {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .appName("Day 14 - DataFrame and Dataset")
      .master("local[4]")
      .getOrCreate()

    spark.sparkContext.setLogLevel("WARN")

    import spark.implicits._

    println("\n" + "=" * 70)
    println("DAY 14 - DATAFRAME AND DATASET")
    println("=" * 70)

    println(s"Spark Version : ${spark.version}")
    println(s"Master        : ${spark.sparkContext.master}")

    // ------------------------------------------------------------
    // 1. Read employee data as a DataFrame
    // ------------------------------------------------------------

    println("\n--- 1. DataFrame from CSV ---")

    val employeeDF = spark.read
      .option("header", "true")
      .option("inferSchema", "true")
      .csv("input/employees.csv")

    println(s"DataFrame Record Count: ${employeeDF.count()}")

    employeeDF.show(false)

    println("DataFrame Schema:")
    employeeDF.printSchema()

    // ------------------------------------------------------------
    // 2. DataFrame -> Dataset
    // ------------------------------------------------------------

    println("\n--- 2. DataFrame -> Dataset[Employee] ---")

    val employeeDS: Dataset[Employee] = employeeDF
      .as[Employee]

    employeeDS.show(false)

    println("Dataset Type: Dataset[Employee]")

    // ------------------------------------------------------------
    // 3. Typed Dataset transformation
    // ------------------------------------------------------------

    println("\n--- 3. Typed Dataset Payroll Pipeline ---")

    val highSalaryEmployees = employeeDS
      .filter(_.salary >= 50000)
      .map(employee =>
        (
          employee.employee_id,
          employee.name,
          employee.department,
          employee.salary,
          employee.salary * 12
        )
      )
      .toDF(
        "employee_id",
        "name",
        "department",
        "monthly_salary",
        "annual_salary"
      )

    println("Employees with monthly salary >= 50000:")
    highSalaryEmployees.show(false)

    // ------------------------------------------------------------
    // 4. Dataset -> DataFrame
    // ------------------------------------------------------------

    println("\n--- 4. Dataset -> DataFrame ---")

    val employeeBackToDF = employeeDS.toDF()

    employeeBackToDF.show(false)

    println("Converted Dataset[Employee] back to DataFrame.")

    // ------------------------------------------------------------
    // 5. Department payroll analysis
    // ------------------------------------------------------------

    println("\n--- 5. Department Payroll Analysis ---")

    val departmentPayroll = employeeDS
      .groupByKey(_.department)
      .mapGroups { (department: String, employees: Iterator[Employee]) =>
        val employeeList = employees.toList
        val totalSalary = employeeList.map(_.salary).sum
        val employeeCount = employeeList.size

        (
          department,
          employeeCount,
          totalSalary,
          totalSalary * 12
        )
      }
      .toDF(
        "department",
        "employee_count",
        "monthly_payroll",
        "annual_payroll"
      )
      .orderBy(desc("monthly_payroll"))

    departmentPayroll.show(false)

    // ------------------------------------------------------------
    // 6. DataFrame payroll analysis
    // ------------------------------------------------------------

    println("\n--- 6. DataFrame Payroll Analysis ---")

    val dataframePayroll = employeeDF
      .groupBy("department")
      .agg(
        count("*").alias("employee_count"),
        sum("salary").alias("monthly_payroll"),
        sum("salary").multiply(12).alias("annual_payroll")
      )
      .orderBy(desc("monthly_payroll"))

    dataframePayroll.show(false)

    // ------------------------------------------------------------
    // 7. Catalyst / execution plan
    // ------------------------------------------------------------

    println("\n--- 7. Catalyst Optimization / Physical Plan ---")

    employeeDS
      .filter(_.salary >= 50000)
      .groupByKey(_.department)
      .count()
      .explain(true)

    // ------------------------------------------------------------
    // 8. RDD, DataFrame and Dataset comparison
    // ------------------------------------------------------------

    println("\n--- 8. RDD vs DataFrame vs Dataset ---")

    println(
      """
        |RDD:
        |- Low-level distributed collection
        |- No structured schema by default
        |- More manual control
        |- Less optimization compared with structured APIs
        |
        |DataFrame:
        |- Distributed table with named columns
        |- Schema-based
        |- Catalyst optimizer and Tungsten execution
        |- Convenient for SQL and analytics
        |
        |Dataset:
        |- Strongly typed structured API
        |- Combines object-oriented programming with Spark SQL
        |- Compile-time type safety for typed operations
        |- Uses Spark SQL optimization
        |""".stripMargin
    )

    println("\n--- 9. Key Learning Points ---")

    println("1. DataFrame provides structured tabular data.")
    println("2. Dataset[Employee] provides a strongly typed representation.")
    println("3. Case classes define the schema for typed records.")
    println("4. Dataset can be converted back to a DataFrame using toDF().")
    println("5. Catalyst optimizes Spark SQL and Dataset execution plans.")
    println("6. Typed operations provide compile-time type checking.")
    println("7. Payroll calculations can be performed using Dataset and DataFrame APIs.")

    println("\n" + "=" * 70)
    println("DAY 14 EXECUTION COMPLETED SUCCESSFULLY")
    println("=" * 70)

    spark.stop()
  }
}
