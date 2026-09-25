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
