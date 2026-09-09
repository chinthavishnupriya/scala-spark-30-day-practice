# Day 7 — Immutability, Lineage and Fault Tolerance

## Objective
Understand how Spark RDDs remain immutable, how Spark records RDD lineage, and how lineage helps recover lost partitions.

## Practice Requirements
- Create a multi-step RDD transformation chain.
- Inspect and draw its lineage.
- Explain why RDDs are immutable.
- Explain how Spark recomputes lost partitions.
- Conceptually simulate executor loss and identify what Spark recomputes.

## Environment
- Scala: 2.13.18
- Apache Spark: 4.2.0
- Java: 17.0.20
- SBT: 1.10.11
- Master: local[4]

## Transformation Chain

```text
salesRDD
   |
   v
filter(valid records)
   |
   v
map(parse records)
   |
   v
map(calculate revenue)
   |
   v
reduceByKey(sum by day)
   |
   v
 dailyRevenue
```

`filter` and `map` are narrow transformations. `reduceByKey` is a wide transformation that introduces a shuffle boundary.

## RDD Immutability

An RDD cannot be modified after it is created. Operations such as `filter` and `map` create new RDDs instead of changing the original RDD.

In this project:

```text
salesRDD -> validSales -> parsedSales -> revenueByDay -> dailyRevenue
```

The original `salesRDD` remains unchanged throughout the pipeline.

## Lineage

Spark maintains information about how each RDD was derived from its parent RDDs. `toDebugString` is used to inspect this lineage.

The lineage can be represented as:

```text
salesRDD
   |
 filter
   |
 map(parse)
   |
 map(revenue)
   |
 reduceByKey
   |
 dailyRevenue
```

Lineage is important for fault tolerance because Spark can use it to recompute missing partitions.

## Fault Tolerance

If a partition is lost because an executor becomes unavailable, Spark does not need to rebuild the entire application manually. It can schedule the missing task elsewhere and recompute the lost partition by replaying the required transformations from the lineage.

For this pipeline, the conceptual recovery path is:

```text
salesRDD
   -> filter
   -> map(parse)
   -> map(revenue)
   -> reduceByKey
   -> lost partition recomputed
```

Healthy partitions remain available and do not need to be recomputed.

## Conceptual Executor-Loss Scenario

Assume one executor holding a `dailyRevenue` partition is lost.

1. Spark detects that the task/partition result is unavailable.
2. Spark schedules the required task on another executor.
3. Spark follows the RDD lineage to determine the required parent data and transformations.
4. The missing partition is recomputed.
5. The application can continue without manually rebuilding the dataset.

This is a conceptual simulation; no real executor is intentionally terminated.

## Performance Observations

- Transformations are lazy until an action is executed.
- `filter` is a narrow transformation.
- `map` is a narrow transformation.
- `reduceByKey` is a wide transformation and causes a shuffle.
- Lineage provides the information needed for recomputation.
- Persistence/caching can reduce repeated recomputation when an RDD is reused.

## Actions Used

- `collect()` to display daily revenue.
- `count()` to verify the original and filtered RDDs.

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

Day 7 demonstrates the relationship between RDD immutability, lineage and fault tolerance. The exercise shows why Spark can recover lost partition data by recomputing only the required portion of the RDD transformation chain.
