# Day 10 — Partitioning

## Objective

Practice Apache Spark partitioning concepts using Scala and RDDs. This day focuses on controlling partition counts, understanding data distribution, and choosing appropriate partitioning strategies for parallel processing.

## Practice Requirements

- Check the number of partitions in an RDD.
- Increase partitions with `repartition`.
- Decrease partitions with `coalesce`.
- Explain when increasing or decreasing partitions helps.
- Use `partitionBy` with a Pair RDD.
- Inspect partition distribution.
- Optimize a dataset with too few partitions.

## What is a Partition?

A partition is a logical chunk of an RDD. Spark creates tasks to process partitions, so partition count affects the amount of parallel work available.

A useful mental model is:

```text
RDD
 ├── Partition 0 → Task
 ├── Partition 1 → Task
 ├── Partition 2 → Task
 └── Partition 3 → Task
```

The ideal partition count depends on dataset size, available CPU resources, and the workload. More partitions are not automatically better.

## Dataset

The input file `input/sales.txt` contains **20 sales records** across Electronics, Books, and Clothing.

Revenue is calculated as:

```text
Revenue = Quantity × Price
```

The application uses local Spark execution with 4 cores.

## Original Partition Count

The project intentionally creates the original RDD with **1 partition** to demonstrate the problem of having too little parallelism.

```text
Original dataset
      ↓
1 partition
      ↓
limited parallel work
```

With only one partition, only one task can process that particular stage's partition at a time, even when multiple local cores are available.

## `repartition`

`repartition(4)` increases the dataset from 1 partition to 4 partitions.

```text
1 partition
     ↓
repartition(4)
     ↓
4 partitions
```

`repartition` uses a shuffle to redistribute data. This adds data movement, but it can provide more parallelism when the original partition count is too small.

Observed result:

```text
Original partitions     : 1
After repartition(4)    : 4
```

## Partition Distribution

The program uses `mapPartitionsWithIndex` to inspect how records are distributed among partitions.

For the 20-record sample, the 4-partition result contains 5 records in each partition:

```text
Partition 0 → 5 records
Partition 1 → 5 records
Partition 2 → 5 records
Partition 3 → 5 records
```

This demonstrates that repartitioning changes how records are distributed across the available partitions.

## `coalesce`

`coalesce(2)` reduces the number of partitions from 4 to 2.

```text
4 partitions
     ↓
coalesce(2)
     ↓
2 partitions
```

`coalesce` is primarily intended for reducing partitions with less data movement than a full repartition in common cases.

## `repartition` vs `coalesce`

| Operation | Main purpose | Shuffle behavior |
|---|---|---|
| `repartition(n)` | Increase or redistribute partitions | Uses shuffle |
| `coalesce(n)` | Reduce partitions | Usually avoids a full shuffle |

If a dataset has too few partitions, `repartition` is appropriate. If a dataset has too many partitions and needs to be reduced, `coalesce` can often be more efficient.

## Pair RDD and `partitionBy`

The exercise creates a Pair RDD using product keys and then applies:

```scala
partitionBy(new HashPartitioner(4))
```

The partitioner is inspected after the operation.

Observed result:

```text
Pair RDD before partitionBy → 1 partition
Pair RDD after partitionBy  → 4 partitions
Partitioner                 → HashPartitioner(4)
```

`HashPartitioner` determines the target partition for each key using a hash-based strategy. This is useful when later operations benefit from consistent key distribution.

## Revenue by Product

The final product-level aggregation produced:

| Product | Revenue |
|---|---:|
| Books | ₹4250.00 |
| Clothing | ₹7250.00 |
| Electronics | ₹111000.00 |

## Optimization Scenario

### Problem

The original dataset has only one partition while the local Spark application has four available cores.

### Optimization

Increase the partition count to four using `repartition(4)`.

### Reason

More partitions create more independent units of work and can allow multiple tasks to execute concurrently when resources are available.

### Trade-off

Repartitioning introduces a shuffle, so it is not automatically beneficial for every dataset. The cost of redistribution should be justified by improved parallelism or later processing requirements.

## When to Increase Partitions

Increasing partitions can help when:

- The dataset is large.
- Existing partitions are too large.
- There are too few tasks to use available CPU resources effectively.
- A later operation benefits from a different partition distribution.

## When to Decrease Partitions

Reducing partitions can help when:

- The dataset becomes smaller after filtering or aggregation.
- There are many tiny partitions.
- Task scheduling overhead becomes significant.
- Fewer output files are desirable in an appropriate output workflow.

## Performance Observations

- Partition count controls available task-level parallelism.
- `repartition` redistributes data through a shuffle.
- `coalesce` is generally useful for reducing partitions with less movement.
- `partitionBy` gives a Pair RDD an explicit partitioning strategy.
- Too few partitions can underuse CPU resources.
- Too many partitions can increase scheduling overhead.
- Partition tuning should be based on data size and workload rather than using a fixed number blindly.

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
│   ├── 01-partition-count-repartition.png
│   ├── 02-partition-distribution-partitionby.png
│   └── 03-optimization-success.png
└── src/
    └── main/
        └── scala/
            └── Day10.scala
```

## Technologies

- Scala 2.13.18
- Apache Spark 4.2.0
- SBT 2.0.7
- Java 17
- WSL2
- Local Spark mode with 4 cores

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

Day 10 demonstrates how Spark partitions affect parallelism and data distribution. The exercise provides practical experience with `repartition`, `coalesce`, `mapPartitionsWithIndex`, `partitionBy`, and `HashPartitioner`, while highlighting the trade-off between parallelism and shuffle overhead.