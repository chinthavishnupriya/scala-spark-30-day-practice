# Day 16 - Spark Aggregations

## Objective

Practice Apache Spark DataFrame aggregations using `count`, `sum`, `avg`, `min`, and `max`, followed by grouping, multi-column grouping, HAVING-like filtering, and Spark SQL aggregation.

The practical scenario used in this exercise is hospital department revenue analysis.

## Day 16 Requirements

1. Count records using `count`
2. Calculate totals using `sum`
3. Calculate averages using `avg`
4. Find minimum values using `min`
5. Find maximum values using `max`
6. Group data by department
7. Group data by multiple columns
8. Apply HAVING-like filtering after aggregation
9. Calculate department-wise statistics
10. Analyze hospital department revenue metrics
11. Use Spark SQL for aggregation
12. Inspect the Spark execution plan

## Project Structure

```text
Day-16-Aggregations/
├── README.md
├── COMMANDS.md
├── .gitignore
├── .jvmopts
├── build.sbt
├── code/
│   └── Day16.scala
├── input/
│   └── hospital_revenue.csv
├── output/
│   └── day16-execution-output.txt
├── project/
│   └── build.properties
├── screenshots/
│   ├── 01-compilation-success.png
│   ├── 02-aggregation-result.png
│   ├── 03-having-sql-result.png
│   └── 04-execution-plan.png
├── src/
│   └── main/
│       └── scala/
│           └── Day16.scala
└── troubleshooting/
    └── README.md
```

## Technology Stack

| Technology | Version |
|---|---|
| Apache Spark | 4.2.0 |
| Scala | 2.13.18 |
| SBT | 1.10.11 |
| Java | OpenJDK 17.0.20 |
| Execution Mode | local[4] |
| OS | Ubuntu / WSL2 |

## Input Dataset

File: `input/hospital_revenue.csv`

Columns:

| Column | Description |
|---|---|
| department | Hospital department |
| city | Hospital city |
| doctor | Doctor name |
| patients | Number of patients |
| consultation_revenue | Consultation revenue |
| procedure_revenue | Procedure revenue |

The program calculates `total_revenue = consultation_revenue + procedure_revenue`.

## Sample Input

```csv
department,city,doctor,patients,consultation_revenue,procedure_revenue
Cardiology,Hyderabad,Dr Rao,42,126000,210000
Cardiology,Hyderabad,Dr Mehta,35,105000,175000
Cardiology,Vijayawada,Dr Kumar,28,84000,140000
Neurology,Hyderabad,Dr Sharma,31,93000,155000
Neurology,Hyderabad,Dr Reddy,26,78000,130000
Neurology,Vijayawada,Dr Das,22,66000,110000
Orthopedics,Hyderabad,Dr Singh,38,95000,190000
Orthopedics,Vijayawada,Dr Rao,30,75000,150000
Orthopedics,Tirupati,Dr Naidu,24,60000,120000
Pediatrics,Hyderabad,Dr Priya,45,90000,90000
Pediatrics,Vijayawada,Dr Anil,36,72000,72000
Pediatrics,Tirupati,Dr Lakshmi,29,58000,58000
Dermatology,Hyderabad,Dr Varma,20,50000,80000
Dermatology,Tirupati,Dr Swetha,18,45000,70000
```

## Implementation

Main source: `src/main/scala/Day16.scala`

Source copy: `code/Day16.scala`

Spark session:

```scala
SparkSession.builder()
  .appName("Day-16-Aggregations")
  .master("local[4]")
  .getOrCreate()
```

CSV loading uses header and schema inference.

## Total Revenue

A calculated column combines consultation and procedure revenue:

```scala
val revenueDF = hospitalDF.withColumn(
  "total_revenue",
  col("consultation_revenue") + col("procedure_revenue")
)
```

## Basic Aggregations

The program demonstrates `count`, `sum`, `avg`, `min`, and `max`.

Results:

| Metric | Result |
|---|---:|
| Record count | 14 |
| Total revenue | 2,847,000 |
| Average revenue | 203357.14285714287 |
| Minimum revenue | 115,000 |
| Maximum revenue | 336,000 |

## Department-Wise Revenue

| Department | Records | Patients | Revenue |
|---|---:|---:|---:|
| Cardiology | 3 | 105 | 840,000 |
| Orthopedics | 3 | 92 | 690,000 |
| Neurology | 3 | 79 | 632,000 |
| Pediatrics | 3 | 110 | 440,000 |
| Dermatology | 2 | 38 | 245,000 |

## Multiple-Column Grouping

The program groups by:

```scala
.groupBy("department", "city")
```

It calculates record count, total patients, total revenue, and average revenue for each department-city combination.

## HAVING-Like Filtering

The DataFrame API performs aggregation first and then filters the result:

```scala
.filter(col("department_revenue") > 300000)
```

Departments above the threshold:

- Cardiology — 840,000
- Orthopedics — 690,000
- Neurology — 632,000
- Pediatrics — 440,000

Dermatology is excluded because its revenue is 245,000.

## Patient Statistics

| Department | Total Patients | Average | Minimum | Maximum |
|---|---:|---:|---:|---:|
| Pediatrics | 110 | 36.67 | 29 | 45 |
| Cardiology | 105 | 35.00 | 28 | 42 |
| Orthopedics | 92 | 30.67 | 24 | 38 |
| Neurology | 79 | 26.33 | 22 | 31 |
| Dermatology | 38 | 19.00 | 18 | 20 |

## Highest Revenue Department

The program orders departments by revenue and takes the first row. The result is Cardiology with 840,000 revenue.

## Spark SQL

The DataFrame is registered as the temporary view `hospital_revenue`.

The SQL query groups by department, calculates count/sum/average/min/max, applies:

```sql
HAVING SUM(total_revenue) > 300000
```

and orders by revenue descending.

The SQL result contains Cardiology, Orthopedics, Neurology, and Pediatrics.

## Transformations and Actions

Main transformations:

- `withColumn`
- `groupBy`
- `agg`
- `filter`
- `orderBy`
- `createOrReplaceTempView`

Main actions:

- `show()`
- `count()`

## Shuffle Boundaries

Grouping requires records with the same grouping key to be redistributed. The physical plan contains:

```text
Exchange hashpartitioning(department, 200)
```

Sorting can also produce a range-partitioning exchange.

## Execution Plan

The program runs:

```scala
departmentStats.explain(true)
```

The physical plan includes operations such as:

```text
Sort
Exchange rangepartitioning
HashAggregate
Exchange hashpartitioning
HashAggregate
Project
FileScan csv
```

This demonstrates Spark's physical execution and shuffle boundaries.

## Performance Observations

1. `groupBy` can cause a shuffle.
2. Multi-column grouping can also require a shuffle.
3. Partial aggregation can reduce data transferred during shuffle.
4. Sorting can introduce another exchange.

## Output

Complete execution output:

```text
output/day16-execution-output.txt
```

It contains input data, schema, record count, aggregation results, SQL results, and the physical execution plan.

## Screenshots

- `screenshots/01-compilation-success.png` — successful compilation
- `screenshots/02-aggregation-result.png` — aggregation results
- `screenshots/03-having-sql-result.png` — HAVING-like filter and Spark SQL
- `screenshots/04-execution-plan.png` — physical plan and Exchange operations

## Verification

Compilation succeeded and the application completed with:

```text
DAY 16 COMPLETED SUCCESSFULLY
```

The saved output was verified with:

```bash
grep -n "DAY 16 COMPLETED SUCCESSFULLY" output/day16-execution-output.txt
```

Expected saved-output line:

```text
197:[info] DAY 16 COMPLETED SUCCESSFULLY
```

## Learning Outcomes

- Spark DataFrame aggregation
- `count`, `sum`, `avg`, `min`, `max`
- `groupBy`
- Multi-column grouping
- HAVING-like filtering
- Spark SQL aggregation
- Temporary views
- Physical execution plans
- Shuffle boundaries
- Partial aggregation
- Basic Spark performance analysis

## Completion Status

```text
Compilation:              COMPLETE
Execution:                COMPLETE
Basic Aggregations:       COMPLETE
Department Aggregation:   COMPLETE
Multi-column Grouping:    COMPLETE
HAVING-like Filtering:    COMPLETE
Patient Statistics:       COMPLETE
Spark SQL:                COMPLETE
Execution Plan:           COMPLETE
Output Verification:      COMPLETE
Documentation:            COMPLETE
Screenshots:              COMPLETE
Git Commit/Push:          COMPLETE
```

## Conclusion

Day 16 demonstrates Spark DataFrame aggregation techniques using hospital department revenue data, including basic and grouped aggregations, multi-column grouping, HAVING-like filtering, Spark SQL, and execution-plan analysis.