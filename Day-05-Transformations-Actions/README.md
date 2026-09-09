# Day 05 — Transformations and Actions

## Objective

Practice Apache Spark RDD transformations and actions using Scala, understand lazy evaluation, identify shuffle boundaries, and apply the concepts to a simple application log analyzer.

## Practice Requirements

- Use `map`, `filter`, `flatMap`, `distinct`, and `union`.
- Use `count`, `collect`, `first`, `take`, and `reduce`.
- Explain the difference between transformations and actions.
- Understand Spark's lazy evaluation model.
- Analyze application logs and count `ERROR` records.

## Environment

| Component | Version |
|---|---|
| OS | Ubuntu / WSL2 |
| Java | 17.0.20 |
| Scala | 2.13.18 |
| Apache Spark | 4.2.0 |
| SBT | 1.10.11 |
| Spark Master | `local[4]` |

## Transformations

Transformations create a new RDD from an existing RDD. They are evaluated lazily and do not immediately execute a Spark job.

| Transformation | Purpose |
|---|---|
| `map` | Transform every element |
| `filter` | Keep elements matching a condition |
| `flatMap` | Transform and flatten results |
| `distinct` | Remove duplicate values |
| `union` | Combine two RDDs |

## Actions

Actions request a result from an RDD and trigger Spark execution.

| Action | Purpose |
|---|---|
| `count` | Number of elements |
| `collect` | Return all elements to the driver |
| `first` | Return the first element |
| `take` | Return a limited number of elements |
| `reduce` | Aggregate elements into one value |

`collect()` should be used carefully with large datasets because it transfers all selected results to the driver.

## Lazy Evaluation

Spark does not immediately execute a transformation such as `filter` or `map`. Instead, Spark records the requested computation. When an action is called, Spark builds the execution plan and runs the required tasks.

Example:

```text
source RDD
    ↓
filter
    ↓
map
    ↓
action
    ↓
Spark execution
```

This allows Spark to optimize the execution pipeline before processing the data.

## Narrow and Wide Transformations

Most `map`, `filter`, and `flatMap` operations are narrow because each output partition depends on a limited set of input partitions.

`distinct` can require data movement across partitions because duplicate values must be combined. This creates a shuffle boundary in the execution plan.

## ERROR Log Analyzer

The input file contains application log messages. The program filters records containing `ERROR`, counts them, and displays relevant information.

The completed execution produced **5 ERROR records**.

Processing flow:

```text
application.log
      ↓
read as RDD
      ↓
filter ERROR lines
      ↓
count / collect
      ↓
ERROR analysis
```

## Union and Distinct

Two RDDs are combined with `union`. Duplicate values can then be removed using `distinct`. This demonstrates how a sequence of transformations can form a larger processing pipeline before an action triggers execution.

## Performance Observations

- Transformations are lazy.
- Actions trigger execution.
- Narrow transformations can generally be pipelined within a stage.
- Wide transformations may require a shuffle.
- `distinct` can introduce data movement.
- `collect` is appropriate for small demonstration results but should not be used to return an unbounded large dataset to the driver.

## Project Structure

```text
Day-05-Transformations-Actions/
├── README.md
├── COMMANDS.md
├── .jvmopts
├── build.sbt
├── code/
│   └── Day05.scala
├── input/
│   └── application.log
├── output/
│   └── day05_run.txt
├── project/
│   └── build.properties
├── screenshots/
├── src/
│   └── main/
│       └── scala/
│           └── Day05.scala
└── troubleshooting/
```

## How to Run

```bash
sbt compile
sbt run
```

Save output:

```bash
sbt run > output/day05_run.txt 2>&1
```

## Learning Outcome

Day 5 establishes the central Spark execution model: transformations describe a computation, actions trigger it, lazy evaluation delays work, and shuffle-producing operations can affect stage boundaries and performance.