# Day 13 – Spark SQL Basics

## Objective

Practice Spark SQL fundamentals using a customer dataset.

The Day 13 problem focuses on creating a DataFrame from CSV/JSON data, inspecting the schema, selecting and filtering data, creating derived columns, registering a temporary SQL view, running SQL queries, and producing a customer analytics report.

## Topics Covered

- Create a DataFrame from CSV
- Inspect schema and records
- Select required columns
- Filter records using DataFrame expressions
- Create derived columns with `withColumn`
- Register a temporary SQL view
- Execute Spark SQL queries
- Generate customer analytics
- Understand transformations, actions, shuffles, and Spark SQL optimization

## Environment

- Apache Spark: 4.2.0
- Scala: 2.13.18
- SBT: 1.10.11
- Java: OpenJDK 17.0.20
- Master: `local[4]`
- OS: Ubuntu 26.04 / WSL2

## Project Structure

```text
Day-13-Spark-SQL-Basics/
├── README.md
├── COMMANDS.md
├── .gitignore
├── .jvmopts
├── build.sbt
├── code/
│   └── Day13.scala
├── input/
│   └── customers.csv
├── output/
│   └── result.txt
├── project/
│   ├── README.md
│   └── build.properties
├── screenshots/
│   ├── 01-compilation-success.png
│   ├── 02-spark-sql-execution.png
│   └── 03-sql-analytics-result.png
├── src/main/scala/
│   └── Day13.scala
└── troubleshooting/
    └── README.md
```

### Directory and File Purpose

| Path | Purpose |
|---|---|
| `README.md` | Complete Day 13 explanation, implementation details, results, and learning outcomes. |
| `COMMANDS.md` | Commands used for setup, compilation, execution, verification, screenshots, and Git. |
| `.gitignore` | Prevents generated build files and IDE files from being committed. |
| `.jvmopts` | JVM options used by the SBT project. |
| `build.sbt` | Defines the Scala version and Spark Core/Spark SQL dependencies. |
| `code/Day13.scala` | Practical source-code copy for reference. |
| `input/customers.csv` | Customer input dataset used by the Spark SQL application. |
| `output/result.txt` | Saved execution output from the successful Day 13 run. |
| `project/build.properties` | Pins the SBT version to 1.10.11. |
| `project/README.md` | Documents the SBT project configuration and versions. |
| `screenshots/` | Evidence of compilation and Spark SQL execution/results. |
| `src/main/scala/Day13.scala` | Main SBT source location for the Day 13 application. |
| `troubleshooting/README.md` | Common errors, checks, and recovery commands. |

Generated SBT directories such as `target/` and `project/target/` are excluded through `.gitignore` and are not part of the project structure committed to Git.

## Input Dataset

Input file:

```text
input/customers.csv
```

The CSV contains these columns:

| Column | Description |
|---|---|
| `customer_id` | Customer identifier |
| `name` | Customer name |
| `city` | Customer city |
| `age` | Customer age |
| `total_spend` | Total customer spending |

The practical dataset contains 8 customer records.

## Implementation

The application reads the CSV using Spark SQL:

```scala
val customers = spark.read
  .option("header", "true")
  .option("inferSchema", "true")
  .csv("input/customers.csv")
```

The program then:

1. Displays the number of records.
2. Displays the DataFrame.
3. Prints the inferred schema.
4. Selects customer information and spending columns.
5. Filters customers whose `total_spend` is greater than 10000.
6. Creates a `spending_category` column.
7. Creates a `loyalty_points` column.
8. Registers the DataFrame as the temporary view `customers`.
9. Executes SQL analytics queries.

## Spending Category

The program derives `spending_category` using `when` expressions:

- `total_spend >= 15000` → `High`
- `total_spend >= 10000` → `Medium`
- Otherwise → `Low`

Loyalty points are calculated as:

```text
floor(total_spend / 100)
```

## Spark SQL Queries

### 1. High-value Customers

The SQL query selects customers with `total_spend >= 15000` and orders them by spending in descending order.

### 2. City-wise Analytics

The program calculates:

- Customer count
- Total spending
- Average spending

grouped by city.

### 3. Top 3 Customers

The program orders customers by `total_spend` in descending order and returns the top three records.

## Observed Results

The high-value customer query returned:

```text
C107  Gopal   Nellore        22400  High  224
C105  Eswar   Hyderabad      19800  High  198
C103  Charan  Visakhapatnam  15600  High  156
```

The city-wise report included:

```text
Hyderabad      3  41600  13866.67
Nellore        1  22400  22400.0
Visakhapatnam  1  15600  15600.0
Tirupati       1  11200  11200.0
Vijayawada     1   8700   8700.0
Warangal       1   6400   6400.0
```

The top three customers were C107, C105, and C103 according to the executed `ORDER BY total_spend DESC LIMIT 3` query.

## Spark Execution Concepts

### Transformations / Lazy Evaluation

Examples used in the program include:

- `select`
- `filter`
- `withColumn`
- SQL query construction

These operations build execution plans. Spark evaluates the required work when an action such as `show()` or `count()` is executed.

### Actions

The program uses actions including:

- `count()`
- `show()`

These trigger computation and produce results.

### Shuffle Boundary

The city-wise SQL query uses:

```sql
GROUP BY city
```

Grouping can require data to be redistributed by key, creating a shuffle boundary in the physical execution plan.

The ordering operations used by the analytics queries can also require distributed sorting work.

### Catalyst Optimization

Spark SQL/DataFrame operations are converted into a logical plan and optimized by Spark SQL's Catalyst optimizer before execution. The final physical plan determines how the query is executed.

## Output

The complete execution output was saved to:

```text
output/result.txt
```

The application completed successfully and reported the total execution time.

## Screenshots

### 1. Compilation Success

`screenshots/01-compilation-success.png`

Shows successful SBT compilation.

### 2. Spark SQL Execution

`screenshots/02-spark-sql-execution.png`

Shows the Spark SQL application running and producing results.

### 3. SQL Analytics Result

`screenshots/03-sql-analytics-result.png`

Shows the customer analytics query results.

## How to Run

From the Day 13 directory:

```bash
sbt clean compile
sbt "runMain Day13"
```

To save a fresh execution log:

```bash
sbt "runMain Day13" | tee output/result.txt
```

To inspect the saved output:

```bash
cat output/result.txt
```

## Verification

Check the important project files:

```bash
ls -lh input/
ls -lh output/
ls -lh screenshots/
git status --short
```

Build artifacts should remain ignored:

```bash
git status --ignored --short | grep -E 'target/|project/target/'
```

## Learning Outcome

After completing Day 13, the practical workflow covered is:

```text
CSV
 ↓
DataFrame
 ↓
Schema inspection
 ↓
Select / Filter
 ↓
Derived columns
 ↓
Temporary SQL View
 ↓
Spark SQL
 ↓
Analytics Results
```

This demonstrates the basic Spark SQL workflow required for the Day 13 practice problem.
