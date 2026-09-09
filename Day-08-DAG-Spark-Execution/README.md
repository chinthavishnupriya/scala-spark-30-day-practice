# Day 08 — DAG and Spark Execution

## Objective

Understand how Apache Spark converts an RDD transformation pipeline into a DAG and divides execution into jobs, stages, tasks, and partitions.

## Practice Requirements

- Create a Spark job with several transformations and actions.
- Identify stages and shuffle boundaries.
- Explain jobs, stages, tasks, and partitions.
- Compare narrow and wide transformations.
- Predict the number of stages for a `reduceByKey` pipeline.

## Input

Input file:

`input/sales.txt`

The dataset contains sales records in the format:

```text
Day,Product,Quantity,Price
```

The application processes **15 sales records**.

## Pipeline

The main RDD pipeline is:

```text
salesRDD
   ↓
filter
   ↓
map(parse)
   ↓
map(revenue)
   ↓
reduceByKey
   ↓
sortByKey
   ↓
collect
```

The program also executes multiple actions such as `collect`, `count`, and `first` to demonstrate that each action can create its own Spark job.

## DAG Concept

A DAG (Directed Acyclic Graph) represents the dependencies between transformations. Spark analyzes these dependencies and divides the computation into stages.

```text
        Stage 0
salesRDD → filter → map → map
                         |
                         v
                    SHUFFLE
                         |
                         v
        Stage 1
reduceByKey → sortByKey
                         |
                         v
                    final result
```

For the primary pipeline, the exercise predicts **3 stages** because there are two shuffle boundaries: one associated with `reduceByKey` and another with the key-sorting operation.

## Jobs, Stages, Tasks and Partitions

### Job

A job is created when an action such as `collect`, `count`, or `first` requests a result.

### Stage

A stage is a set of operations that can be executed without crossing a shuffle dependency. Shuffle boundaries divide stages.

### Task

A task is the unit of work that processes one partition within a stage.

### Partition

A partition is a logical chunk of the RDD. More partitions can provide more opportunities for parallel task execution, subject to available resources.

## Narrow vs Wide Transformations

| Type | Example | Data movement |
|---|---|---|
| Narrow | `filter`, `map` | No cross-partition redistribution required |
| Wide | `reduceByKey`, `sortByKey` | May require shuffle |

Narrow transformations can normally be pipelined in one stage. Wide transformations create dependencies that require Spark to redistribute data.

## Shuffle Boundaries

`reduceByKey` groups values by key, so records with the same key may need to move between partitions. `sortByKey` also requires data to be ordered across keys and can introduce another shuffle dependency.

Shuffle is important for performance because it can involve network transfer, serialization, disk I/O, and additional scheduling work.

## Observed Daily Revenue

The 15-record sample produced:

| Day | Revenue |
|---|---:|
| Friday | ₹69500.00 |
| Monday | ₹36100.00 |
| Thursday | ₹34600.00 |
| Tuesday | ₹23400.00 |
| Wednesday | ₹43100.00 |

## Multiple Actions

The application uses more than one action to demonstrate Spark job triggering. This is useful for understanding why repeated actions over an uncached RDD may recompute upstream work.

## Performance Observations

- Lazy transformations are assembled before execution.
- Narrow transformations can be pipelined within a stage.
- Wide transformations create shuffle boundaries.
- More partitions can increase parallelism when resources are available.
- Reusing an RDD for several actions may benefit from persistence when the dataset is expensive to recompute.
- Avoid unnecessary shuffles because they can become a major performance cost.

## Environment

- Scala: 2.13.18
- Apache Spark: 4.2.0
- Java: 17.0.20
- SBT: 1.10.11
- Master: `local[4]`

## Project Structure

```text
Day-08-DAG-Spark-Execution/
├── .gitignore
├── .jvmopts
├── README.md
├── build.sbt
├── code/
│   └── Day08.scala
├── input/
│   └── sales.txt
├── output/
│   └── result.txt
├── screenshots/
│   ├── 01-dag-stage-prediction.png
│   ├── 02-revenue-narrow-wide.png
│   └── 03-execution-summary-success.png
└── src/
    └── main/
        └── scala/
            └── Day08.scala
```

## How to Run

```bash
sbt compile
sbt run
```

Save output:

```bash
sbt run > output/result.txt 2>&1
```

## Learning Outcome

Day 8 explains how Spark turns a logical RDD pipeline into executable jobs and stages. The exercise makes shuffle boundaries, task parallelism, and the relationship between partitions and stages concrete.