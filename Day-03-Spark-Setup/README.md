# Day 03 — Apache Spark Setup

## Objective

Set up a Scala Spark project using SBT and demonstrate:

- SparkSession and SparkContext
- Reading a text file
- Spark driver, executor, and cluster manager
- Local execution with 2 cores
- Local execution with 4 cores
- Basic Spark execution concepts such as actions, partitions, and stages

## Environment

- Java: 17.0.20
- Scala: 2.13.18
- SBT: 1.10.11
- Apache Spark: 4.2.0
- Platform: Ubuntu on WSL2

## Project Structure

```text
Day-03-Spark-Setup/
├── .gitignore
├── build.sbt
├── COMMANDS.md
├── README.md
├── code/
│   └── Day03.scala
├── input/
│   └── sample.txt
├── output/
│   ├── result_local2.txt
│   └── result_local4.txt
├── project/
│   └── build.properties
├── screenshots/
│   ├── 01_compile_success.png
│   ├── 02_day03_execution_local2.png
│   └── 03_day03_execution_local4.png
├── src/
│   └── main/
│       └── scala/
│           └── Day03.scala
└── troubleshooting/
