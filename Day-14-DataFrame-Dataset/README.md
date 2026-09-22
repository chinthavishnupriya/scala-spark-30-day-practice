# Day 14 — DataFrame and Dataset

## Overview

Day 14 focuses on Apache Spark's structured APIs: **DataFrame** and **Dataset**.

The practical task uses an employee payroll dataset to demonstrate:

- Creating a Scala case class
- Reading CSV data into a DataFrame
- Converting a DataFrame to `Dataset[Employee]`
- Performing typed Dataset transformations
- Converting a Dataset back to a DataFrame
- Performing payroll analysis
- Comparing RDD, DataFrame, and Dataset
- Understanding type safety
- Understanding Catalyst optimization
- Inspecting logical and physical execution plans
- Identifying a shuffle boundary

---

## Objectives

1. Create an `Employee` case class.
2. Read employee records from CSV.
3. Create a Spark DataFrame.
4. Convert the DataFrame to `Dataset[Employee]`.
5. Perform typed Dataset transformations.
6. Convert the Dataset back to a DataFrame.
7. Perform department-level payroll analysis.
8. Compare RDD, DataFrame, and Dataset APIs.
9. Explain Dataset type safety.
10. Inspect Catalyst logical and physical plans.

---

## Environment

| Component | Version |
|---|---|
| Operating System | Ubuntu / WSL2 |
| Java | OpenJDK 17.0.20 |
| Spark | 4.2.0 |
| Spark Scala | 2.13.18 |
| Standalone Scala Runner | 2.12.20 |
| SBT Project Version | 1.10.11 |
| SBT Runner | 2.0.7 |
| Spark Master | `local[4]` |

The SBT project explicitly uses Scala 2.13.18.

---

## Project Structure

```text
Day-14-DataFrame-Dataset/
├── README.md
├── COMMANDS.md
├── .gitignore
├── .jvmopts
├── build.sbt
├── code/
│   └── Day14.scala
├── input/
│   └── employees.csv
├── output/
│   └── result.txt
├── project/
│   ├── README.md
│   └── build.properties
├── screenshots/
│   ├── 01-compilation-success.png
│   ├── 02-dataframe-dataset-execution.png
│   └── 03-payroll-catalyst-result.png
├── src/main/scala/
│   └── Day14.scala
└── troubleshooting/
    └── README.md
```

### File purposes

| File/Directory | Purpose |
|---|---|
| `README.md` | Day 14 explanation and results |
| `COMMANDS.md` | Commands used during setup, execution, verification, and Git |
| `build.sbt` | Scala, Spark, and project dependencies |
| `.jvmopts` | JVM memory settings |
| `.gitignore` | Ignores generated/build files |
| `code/Day14.scala` | Practice source copy |
| `src/main/scala/Day14.scala` | SBT source file |
| `input/employees.csv` | Employee payroll input |
| `output/result.txt` | Saved execution output |
| `screenshots/` | Evidence of compilation and execution |
| `project/build.properties` | SBT project version |

---

## Input Dataset

The practice uses `input/employees.csv` with these columns:

```text
employee_id,name,department,age,salary
```

The dataset contains 8 employee records across Engineering, Finance, and HR.

---

## Implementation

### 1. Create the Employee case class

```scala
case class Employee(
    employee_id: String,
    name: String,
    department: String,
    age: Int,
    salary: Long
)
```

The case class gives the Dataset a strongly typed Scala representation.

### 2. Create the SparkSession

The application runs Spark locally with four worker threads:

```scala
SparkSession.builder()
  .appName("Day 14 - DataFrame and Dataset")
  .master("local[4]")
  .getOrCreate()
```

### 3. Read CSV into a DataFrame

The CSV is read with a header and inferred schema:

```scala
val employeeDF = spark.read
  .option("header", "true")
  .option("inferSchema", "true")
  .csv("input/employees.csv")
```

### 4. Convert DataFrame to Dataset

```scala
val employeeDS: Dataset[Employee] = employeeDF.as[Employee]
```

This converts the untyped DataFrame representation into a typed `Dataset[Employee]`.

### 5. Typed Dataset transformation

Employees with salary of at least 50,000 are filtered and their annual salary is calculated.

```scala
employeeDS
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
```

### 6. Dataset back to DataFrame

```scala
val employeeBackToDF = employeeDS.toDF()
```

This demonstrates the reverse conversion.

### 7. Department payroll analysis

The Dataset groups employees by department and calculates:

- Employee count
- Monthly payroll
- Annual payroll

The Dataset implementation uses `groupByKey` and `mapGroups`.

### 8. DataFrame payroll comparison

The same payroll calculation is performed using DataFrame operations:

```scala
employeeDF
  .groupBy("department")
  .agg(
    count("*").alias("employee_count"),
    sum("salary").alias("monthly_payroll"),
    sum("salary").multiply(12).alias("annual_payroll")
  )
```

This allows the Dataset and DataFrame approaches to be compared.

---

## Results

### DataFrame

The input contains:

```text
DataFrame Record Count: 8
```

### Dataset

The conversion produced:

```text
Dataset Type: Dataset[Employee]
```

### High-salary employees

Employees with monthly salary >= 50,000 were identified and their annual salaries calculated.

| Employee | Department | Monthly | Annual |
|---|---|---:|---:|
| Charan | Engineering | 62000 | 744000 |
| Divya | Finance | 52000 | 624000 |
| Eswar | Engineering | 57000 | 684000 |
| Gopal | Finance | 68000 | 816000 |

### Department payroll

| Department | Employees | Monthly Payroll | Annual Payroll |
|---|---:|---:|---:|
| Engineering | 4 | 213000 | 2556000 |
| Finance | 2 | 120000 | 1440000 |
| HR | 2 | 79000 | 948000 |

The Dataset and DataFrame payroll calculations produced the same department totals.

---

## RDD vs DataFrame vs Dataset

| API | Main characteristic | Type safety | Catalyst optimization |
|---|---|---|---|
| RDD | Low-level distributed collection | No compile-time schema | Limited |
| DataFrame | Structured tabular API | Column/schema based | Yes |
| Dataset | Structured and typed API | Yes for Scala types | Yes |

### Dataset type safety

With `Dataset[Employee]`, Scala can validate the typed fields used by transformations such as:

```scala
employeeDS.filter(_.salary >= 50000)
```

This is different from referring to a DataFrame column only by a string.

---

## Catalyst Optimization

The program calls:

```scala
explain(true)
```

to display:

- Parsed Logical Plan
- Analyzed Logical Plan
- Optimized Logical Plan
- Physical Plan

The physical plan contains a hash-partitioning exchange for the grouped operation. This identifies a **shuffle boundary** caused by grouping data by department.

---

## Transformations, Actions, and Shuffle

### Transformations

Examples used in Day 14:

- `filter`
- `map`
- `groupByKey`
- `mapGroups`
- `groupBy`
- `agg`
- `orderBy`
- `toDF`

### Actions

Examples include:

- `count`
- `show`
- `explain` for plan inspection

### Shuffle boundary

Department-level grouping requires data to be redistributed by department. The physical plan shows an exchange/hash-partitioning stage, demonstrating the shuffle boundary.

---

## Compilation and Execution

The project was successfully compiled using:

```bash
sbt clean compile
```

The application was successfully executed using:

```bash
sbt run
```

The execution output was also saved to:

```text
output/result.txt
```

---

## Screenshots

### Compilation

`01-compilation-success.png`

Shows successful project compilation.

### DataFrame and Dataset execution

`02-dataframe-dataset-execution.png`

Shows the Spark application execution and structured API results.

### Payroll and Catalyst plan

`03-payroll-catalyst-result.png`

Shows department payroll results and Catalyst/physical-plan output.

---

## Key Learning Points

1. A DataFrame provides a structured tabular representation.
2. A Scala Dataset provides compile-time type information.
3. A case class can define the Dataset schema.
4. DataFrames and Datasets can be converted between each other.
5. Dataset transformations can use typed Scala expressions.
6. Grouping by a key can introduce a shuffle.
7. Catalyst optimizes Spark SQL/DataFrame/Dataset operations.
8. `explain(true)` helps inspect Spark execution plans.
9. DataFrame and Dataset APIs can express the same structured analytics in different ways.
10. Execution plans help identify expensive stages and shuffle boundaries.

---

## Verification

Final Day 14 verification included:

```text
DataFrame Record Count: 8
Dataset Type: Dataset[Employee]
Engineering | 4 | 213000 | 2556000
Finance | 2 | 120000 | 1440000
HR | 2 | 79000 | 948000
Catalyst Optimization / Physical Plan
DAY 14 EXECUTION COMPLETED SUCCESSFULLY
```

---

## Conclusion

Day 14 demonstrates the practical use of Spark DataFrames and typed Datasets with Scala. The project covers conversion between the two APIs, typed employee transformations, payroll aggregation, comparison of structured Spark APIs, Catalyst optimization, and identification of a shuffle boundary.
