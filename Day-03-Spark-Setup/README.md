# Day 03 — Apache Spark Setup

## Objective

Set up a Scala Apache Spark project using SBT and understand the basic components involved when a Spark application runs.

## Practice Requirements

- Create a Scala Spark project with SBT.
- Create and inspect a `SparkSession` and `SparkContext`.
- Read and display a text file.
- Explain the driver, executors, and cluster manager.
- Run the same application in local mode using 2 and 4 cores.

## Spark Application Architecture

```text
User Spark Application
        |
        v
     Driver
        |
        +------------------+
        |                  |
        v                  v
    Executor 1         Executor 2
        |
        v
    Tasks / Data
```

### Driver

The driver runs the main application program. It creates the Spark context/session, builds the execution plan, and coordinates the work.

### Executor

Executors perform tasks assigned by the driver and hold data needed during execution.

### Cluster Manager

A cluster manager is responsible for allocating resources to Spark applications. In this exercise the application runs in local mode, so Spark uses the local machine rather than a separate cluster manager deployment.

## SparkSession and SparkContext

`SparkSession` is the modern entry point for Spark applications. `SparkContext` provides access to Spark's core RDD functionality and configuration.

The application prints basic runtime information such as Spark version, application name, master, and default parallelism.

## Text File Processing

The sample input is read as a Spark RDD. The program displays the input lines and performs simple actions to verify that the Spark environment is working correctly.

## Local Mode Comparison

The application is designed to run with different local core counts:

```text
local[2] → use 2 local worker threads
local[4] → use 4 local worker threads
```

Using more local cores can allow more tasks to execute concurrently when enough parallel work is available. It does not automatically guarantee faster execution for a small dataset because startup and scheduling overhead can dominate.

## Key Spark Concepts Introduced

- **RDD:** distributed collection of data.
- **Transformation:** operation that creates a new dataset.
- **Action:** operation that triggers execution and returns a result or writes output.
- **Partition:** logical chunk of distributed data processed by tasks.
- **Task:** unit of work sent to an executor.
- **Stage:** group of tasks separated by shuffle boundaries.

## Environment

- Java: 17.0.20
- Scala: 2.13.18
- SBT: 1.10.11
- Apache Spark: 4.2.0
- Platform: Ubuntu on WSL2

## Commands Used

### Navigate and inspect

```bash
cd ~/scala-spark-30-day-practice/Day-03-Spark-Setup
ls
find . -maxdepth 3 -type f | sort
```

### Compile

```bash
sbt compile
```

### Run with different local cores

```bash
sbt "run 2"
sbt "run 4"
```

### Save execution results

```bash
sbt "run 2" > output/result_local2.txt 2>&1
sbt "run 4" > output/result_local4.txt 2>&1
```

### Inspect results

```bash
cat output/result_local2.txt
cat output/result_local4.txt
tail -n 30 output/result_local4.txt
```

### Git workflow

```bash
git status --short
git add Day-03-Spark-Setup/
git commit -m "Complete Day 3 Spark setup"
git push origin main
```

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
```

## How to Run

```bash
sbt compile
sbt run
```

The local-core configuration can be changed in the application arguments/configuration as implemented in `Day03.scala`.

## Learning Outcome

Day 3 establishes the Spark runtime foundation: project setup, SparkSession/SparkContext, file input, local execution, and the roles of drivers, executors, partitions, tasks, and stages. Later exercises build directly on this setup.