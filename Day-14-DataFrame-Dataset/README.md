# Day 14 — DataFrame and Dataset

## Objective

Practice Spark's structured APIs by working with DataFrames and Datasets using a typed employee payroll pipeline.

## Topics Covered

- Create a Scala `case class`
- Read CSV data into a DataFrame
- Convert DataFrame to `Dataset[Employee]`
- Perform typed Dataset transformations
- Convert Dataset back to DataFrame
- Compare RDD, DataFrame, and Dataset
- Understand type safety
- Understand Catalyst optimization
- Perform employee payroll analysis
- Inspect the Spark execution plan

## Environment

| Component | Version |
|---|---|
| Ubuntu | WSL2 |
| Java | 17.0.20 |
| Spark | 4.2.0 |
| Spark Scala | 2.13.18 |
| SBT | 1.10.11 |
| Master | local[4] |

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
├── src/
│   └── main/
│       └── scala/
│           └── Day14.scala
└── troubleshooting/
    └── README.md
