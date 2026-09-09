# Day 10 — Partitioning

## Objective

Practice Apache Spark partitioning concepts using Scala and RDDs.

The exercise covers:

- Checking the number of partitions in an RDD
- Increasing partitions with `repartition`
- Decreasing partitions with `coalesce`
- Understanding when to increase or decrease partitions
- Using `partitionBy` with a Pair RDD
- Inspecting partition distribution
- Optimizing a dataset with too few partitions

## Technologies

- Scala 2.13.18
- Apache Spark 4.2.0
- SBT 2.0.7
- Java 17
- WSL2
- Local Spark mode with 4 cores

## Project Structure

```text
Day-10-Partitioning/
├── .gitignore
├── .jvmopts
├── README.md
├── build.sbt
├── code/
│   └── Day10.scala
├── input/
│   └── sales.txt
├── output/
│   └── result.txt
├── screenshots/
└── src/
    └── main/
        └── scala/
            └── Day10.scala
