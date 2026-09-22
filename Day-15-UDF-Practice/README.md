# Day 15 – UDF Practice

## Objective

Practice User Defined Functions (UDFs) in Apache Spark using Scala.

This practical covers:

- Creating a Scala UDF to classify salary bands
- Applying UDFs with `withColumn()`
- Creating customer transaction-risk classification
- Comparing UDF results with Spark built-in functions
- Registering a UDF with the Spark session/catalog
- Calling the registered UDF through Spark SQL
- Generating a customer risk summary
- Inspecting the Spark execution plan
- Understanding UDF performance considerations

---

## Problem Statement

Build a Spark application that reads customer data from CSV and applies custom business logic using Scala UDFs.

The application performs:

1. CSV DataFrame creation
2. Salary-band classification
3. Transaction-risk classification
4. Calculated columns using `withColumn()`
5. Comparison with Spark built-in expressions
6. SQL UDF registration
7. Spark SQL execution
8. Risk-category aggregation
9. Execution-plan inspection

---

## Technology Stack

| Component | Version |
|---|---|
| Apache Spark | 4.2.0 |
| Scala | 2.13.18 |
| Java | 17.0.20 |
| SBT | 1.10.11 |
| Execution Mode | local[4] |
| Platform | Ubuntu / WSL2 |

---

## Input Dataset

Input file:

`input/customers.csv`

Columns:

| Column | Description |
|---|---|
| `customer_id` | Unique customer identifier |
| `name` | Customer name |
| `city` | Customer city |
| `age` | Customer age |
| `salary` | Customer salary |
| `transaction_value` | Customer transaction value |

The sample dataset contains 8 customers.

---

## Salary Band UDF

The Scala UDF classifies customers according to salary.

| Salary | Salary Band |
|---:|---|
| Less than 30,000 | Low |
| 30,000 to 59,999 | Medium |
| 60,000 or more | High |

The UDF is applied using `withColumn()`.

Example:

```scala
withColumn("salary_band", salaryBandUdf(col("salary")))
```

---

## Transaction Risk UDF

A second UDF classifies customers according to transaction value.

| Transaction Value | Risk Category |
|---:|---|
| Less than 10,000 | Low Risk |
| 10,000 to 49,999 | Medium Risk |
| 50,000 or more | High Risk |

The calculated column is:

```text
risk_category
```

---

## UDF vs Built-in Spark Functions

The application implements equivalent logic using Spark's built-in `when()` and `otherwise()` expressions.

Example:

```scala
when(col("salary") < 30000, "Low")
  .when(col("salary") < 60000, "Medium")
  .otherwise("High")
```

The UDF and built-in results are compared.

Expected result:

```text
Comparison mismatches: 0
UDF and built-in results match for all customers.
```

This confirms that both implementations produce the same classifications for the sample data.

---

## Registered SQL UDF

The salary UDF is registered with the Spark session:

```scala
spark.udf.register("salary_band_udf", salaryBandUdf)
```

The registered function can then be called from Spark SQL.

Example:

```sql
SELECT
    customer_id,
    name,
    salary,
    salary_band_udf(salary) AS salary_band
FROM customers
```

The application verifies the SQL UDF execution successfully.

---

## Risk Category Summary

The application groups customers by transaction risk.

Expected result for the provided dataset:

| Risk Category | Customer Count |
|---|---:|
| High Risk | 2 |
| Medium Risk | 4 |
| Low Risk | 2 |

Total:

```text
8 customers
```

---

## Spark Transformations and Actions

Important DataFrame transformations/expressions used include:

- `select()`
- `filter()`
- `withColumn()`
- `join()`
- `groupBy()`
- `agg()`
- `createOrReplaceTempView()`

Actions used include:

- `show()`
- `count()`
- `collect()`

Actions trigger Spark execution.

---

## Execution Plan

The application uses:

```scala
result.explain(true)
```

This displays:

1. Parsed logical plan
2. Analyzed logical plan
3. Optimized logical plan
4. Physical plan

The execution plan provides evidence of how Spark processes the DataFrame operations and UDF expressions.

---

## UDF Performance Considerations

UDFs are useful when custom business logic cannot be conveniently expressed using Spark's built-in functions.

When equivalent built-in Spark SQL functions are available, they can be preferable because Spark understands those expressions directly and can optimize them as part of its query planning.

This practical therefore compares a Scala UDF with equivalent built-in expressions and verifies correctness using the same input data.

---

## Execution Commands

Compile:

```bash
sbt compile
```

Run:

```bash
sbt run
```

Save complete output:

```bash
sbt run 2>&1 | tee output/day15-execution-output.txt
```

---

## Execution Evidence

The successful run demonstrates:

```text
UDF RESULT
BUILT-IN FUNCTION RESULT
UDF VS BUILT-IN COMPARISON
Comparison mismatches: 0
REGISTERED SQL UDF RESULT
RISK CATEGORY SUMMARY
EXECUTION PLAN
DAY 15 COMPLETED SUCCESSFULLY
```

The complete terminal output is stored at:

`output/day15-execution-output.txt`

---

## Screenshots

The project contains four execution screenshots:

```text
screenshots/
├── 01-compilation-success.png
├── 02-udf-result.png
├── 03-udf-vs-builtin.png
└── 04-sql-execution-plan.png
```

### 01 – Compilation Success

Shows successful SBT compilation.

### 02 – UDF Result

Shows the customer records with the calculated UDF classifications.

### 03 – UDF vs Built-in

Shows the comparison between the UDF and Spark built-in implementation, including zero mismatches.

### 04 – SQL and Execution Plan

Shows the registered SQL UDF output and execution-plan information.

---

## Troubleshooting

Troubleshooting information is available in:

`troubleshooting/README.md`

Topics include:

- Spark native Hadoop warnings
- Local hostname warnings
- SBT compilation issues
- Missing input files
- UDF comparison mismatches
- SQL UDF registration
- Execution-plan inspection
- Ignored build artifacts

---

## Project Structure

```text
Day-15-UDF-Practice/
├── README.md
├── COMMANDS.md
├── .gitignore
├── .jvmopts
├── build.sbt
├── code/
│   └── Day15.scala
├── input/
│   └── customers.csv
├── output/
│   └── day15-execution-output.txt
├── project/
│   └── build.properties
├── screenshots/
│   ├── 01-compilation-success.png
│   ├── 02-udf-result.png
│   ├── 03-udf-vs-builtin.png
│   └── 04-sql-execution-plan.png
├── src/
│   └── main/
│       └── scala/
│           └── Day15.scala
└── troubleshooting/
    └── README.md
```

---

## Completion Checklist

- [x] Customer CSV created
- [x] Spark DataFrame created
- [x] Salary-band UDF implemented
- [x] Transaction-risk UDF implemented
- [x] Calculated columns created
- [x] Built-in Spark equivalent implemented
- [x] UDF and built-in results compared
- [x] Zero mismatches verified
- [x] SQL UDF registered
- [x] SQL UDF executed
- [x] Risk summary generated
- [x] Execution plan inspected
- [x] Project compiled successfully
- [x] Application executed successfully
- [x] Execution output saved
- [x] Screenshots captured
- [x] Troubleshooting documentation added
- [x] Documentation pushed to GitHub

---

## Result

Day 15 demonstrates Scala UDF creation, DataFrame transformations, UDF registration, Spark SQL execution, customer risk classification, comparison with built-in Spark functions, aggregation, and execution-plan inspection.
