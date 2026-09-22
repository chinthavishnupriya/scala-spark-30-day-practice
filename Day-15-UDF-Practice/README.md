# Day 15 – UDF Practice

## Objective

Practice User Defined Functions (UDFs) in Apache Spark using Scala.

This practical covers:

- Creating a Scala UDF to classify salary bands
- Applying UDFs with `withColumn()`
- Creating a customer transaction-risk classification
- Comparing UDF results with Spark built-in functions
- Registering a UDF with the Spark session/catalog
- Calling the registered UDF through Spark SQL
- Inspecting the Spark execution plan
- Understanding UDF performance considerations

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
├── project/
│   └── build.properties
├── screenshots/
├── src/
│   └── main/
│       └── scala/
│           └── Day15.scala
└── troubleshooting/
