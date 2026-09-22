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

The implementation uses a typed employee payroll pipeline.

---

## Objective

The objectives of Day 14 are:

1. Create an `Employee` case class.
2. Read employee records from CSV.
3. Create a Spark DataFrame.
4. Convert the DataFrame to `Dataset[Employee]`.
5. Perform typed transformations on the Dataset.
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

The SBT project explicitly uses Scala 2.13.18, which matches Spark 4.2.0.

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
├── src/
│   └── main/
│       └── scala/
│           └── Day14.scala
└── troubleshooting/
    └── README.md
