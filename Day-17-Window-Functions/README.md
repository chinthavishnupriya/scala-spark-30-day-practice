# Day 17 — Window Functions

## Objective

Practice Apache Spark Window Functions using Scala and DataFrames.

This exercise covers:

- `row_number()`
- `rank()`
- `dense_rank()`
- Window partitioning
- Top 3 students per course
- Latest record per customer
- `lag()`
- `lead()`
- Premium change calculation
- Spark SQL
- Execution plan analysis

## Technologies

| Technology | Version |
|---|---|
| Apache Spark | 4.2.0 |
| Scala | 2.13.18 |
| SBT | 1.10.11 |
| Java | 17.0.20 |
| Execution Mode | local[4] |
| OS | Ubuntu / WSL2 |

## Project Structure

```text
Day-17-Window-Functions/
├── README.md
├── COMMANDS.md
├── .gitignore
├── .jvmopts
├── build.sbt
├── code/
│   └── Day17.scala
├── input/
│   ├── students.csv
│   └── customer_policies.csv
├── output/
│   └── day17-execution-output.txt
├── project/
│   └── build.properties
├── screenshots/
│   ├── 01-compilation-success.png
│   ├── 02-ranking-result.png
│   ├── 03-lag-lead-latest-policy.png
│   └── 04-execution-plan.png
├── src/
│   └── main/
│       └── scala/
│           └── Day17.scala
└── troubleshooting/
    └── README.md
```

## Input Data

### students.csv

Contains course, student and score values for three courses: Spark, SQL and Python.

### customer_policies.csv

Contains customer policy history with policy date and premium values. It is used to identify the latest policy and calculate premium changes.

## 1. Ranking Functions

The student data is partitioned by `course` and ordered by score descending.

The program calculates:

- `row_number()` — unique sequential number within each course.
- `rank()` — ranking with gaps when ties occur.
- `dense_rank()` — ranking without gaps when ties occur.

A separate score-only window is used to make the difference between the three ranking functions visible for tied scores.

## 2. Top 3 Students Per Course

The program filters:

```text
row_number <= 3
```

This produces the top three students for each course.

## 3. Latest Policy Per Customer

The policy data is partitioned by `customer_id` and ordered by `policy_date` descending.

`row_number() = 1` identifies the latest policy for each customer.

Expected latest policies:

| Customer | Latest Policy | Date | Premium |
|---|---|---|---:|
| C101 | P002 | 2026-03-10 | 15000 |
| C102 | P004 | 2026-02-12 | 21000 |
| C103 | P006 | 2026-04-18 | 11000 |
| C104 | P009 | 2026-01-30 | 16000 |

## 4. LAG and LEAD

The chronological policy window is ordered by `policy_date` ascending.

- `lag()` returns the previous premium.
- `lead()` returns the next premium.

This is used to inspect customer policy history.

## 5. Premium Change

The program calculates:

```text
premium_change = premium - previous_premium
```

For example:

| Customer | Policy | Premium | Previous Premium | Change |
|---|---|---:|---:|---:|
| C101 | P001 | 12000 | null | null |
| C101 | P002 | 15000 | 12000 | 3000 |
| C101 | P007 | 10000 | null | null |

The displayed policy-history order is chronological; the exact previous value follows the chronological window.

## 6. Spark SQL

The program creates temporary views:

- `ranked_students`
- `latest_policies`

It then executes Spark SQL to retrieve the top three students per course.

## 7. Execution Plan

The program calls:

```scala
top3StudentsDF.explain(true)
latestPolicyDF.explain(true)
```

This displays parsed, analyzed, optimized and physical plans.

Window operations can introduce partitioning and sorting work. The physical plan is included in the saved execution output and screenshot.

## 8. Transformations and Actions

Examples used in this exercise:

**Transformations**

- `select`
- `withColumn`
- `filter`
- `orderBy`
- Window expressions
- `createOrReplaceTempView`

**Actions**

- `show()`
- `count()`
- `explain()` for plan inspection

## 9. Verification

The solution was compiled and executed with Spark 4.2.0.

The saved output contains:

- Student input and schema
- Ranking results
- Top 3 students per course
- Tie-ranking comparison
- Customer policy history
- LAG/LEAD results
- Latest policy per customer
- Premium change results
- Spark SQL output
- Execution plans
- Successful completion marker

## 10. Screenshots

| Screenshot | Purpose |
|---|---|
| `01-compilation-success.png` | Successful SBT compilation |
| `02-ranking-result.png` | Window ranking and top-3 output |
| `03-lag-lead-latest-policy.png` | LAG/LEAD and latest-policy output |
| `04-execution-plan.png` | Spark physical execution plan |

## 11. Troubleshooting

See [troubleshooting/README.md](troubleshooting/README.md) for common Spark, window-ordering, ranking, input-file and execution-plan issues.

## Result

Day 17 Window Functions practice is implemented and tested with Scala + Apache Spark.
