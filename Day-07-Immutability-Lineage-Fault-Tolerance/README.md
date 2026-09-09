# Day 07 — Immutability, Lineage and Fault Tolerance

## Objective

Understand how Spark RDDs remain immutable, how Spark records the dependencies between RDDs, and how lineage helps Spark recover lost partition data.

## Practice Requirements

- Create a multi-step RDD transformation chain.
- Inspect and represent its lineage.
- Explain why RDDs are immutable.
- Explain how Spark recomputes lost partitions.
- Conceptually simulate executor loss and identify what Spark recomputes.

## Environment

- Scala: 2.13.18
- Apache Spark: 4.2.0
- Java: 17.0.20
- SBT: 1.10.11
- Master: `local[4]`

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

`filter` and `map` are narrow transformations. `reduceByKey` is a wide transformation and introduces a shuffle boundary.

## RDD Immutability

An RDD cannot be modified after it is created. Operations such as `filter` and `map` create new RDDs while preserving their parent RDDs.

The project therefore has a chain of separate datasets:

```text
salesRDD
   ↓
validSales
   ↓
parsedSales
   ↓
revenueByDay
   ↓
dailyRevenue
```

The original `salesRDD` remains unchanged even after later transformations are created.

## Lineage

Spark maintains dependency information describing how an RDD was derived from its parent RDDs. This dependency chain is called lineage.

The project represents the lineage as:

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
dailyRevenue
```

The program prints a manual lineage representation because direct debug-string inspection can be sensitive to the Java/Spark runtime configuration used in this environment.

## Fault Tolerance

If an executor becomes unavailable and a partition's computed result is lost, Spark can schedule the required work again and use the RDD lineage to reconstruct the missing data. The entire dataset does not have to be manually rebuilt.

Conceptual recovery:

```text
Original source
     ↓
filter
     ↓
parse
     ↓
calculate revenue
     ↓
aggregate
     ↓
missing partition rebuilt
```

Only the required missing work is recomputed; healthy partition results can remain available.

## Conceptual Executor-Loss Scenario

Assume an executor holding a `dailyRevenue` partition is lost:

1. Spark detects that the required task result is unavailable.
2. The task is scheduled again on an available executor.
3. Spark follows the lineage to determine the required parent data and transformations.
4. The missing partition is recomputed.
5. The application can continue without manually recreating the whole RDD.

This project treats executor loss as a conceptual simulation; it does not intentionally terminate a real executor.

## Actions Used

- `collect()` displays the daily revenue results.
- `count()` verifies the original and filtered RDD sizes.

## Performance Observations

- Transformations are lazy until an action is executed.
- Narrow transformations can be pipelined within a stage.
- `reduceByKey` causes a shuffle and creates a stage boundary.
- Lineage provides the dependency information needed for recomputation.
- Caching or persistence can reduce repeated recomputation when the same RDD is reused by multiple actions.

## Observed Result

The sample dataset contains 10 sales records. The completed pipeline produces daily revenue values for Monday through Friday and confirms the original and valid-record counts.

## Java 17 Runtime Configuration

The project uses forked SBT execution and Java module-opening options required by the Spark runtime in this development environment. These settings are kept in `build.sbt` and `.jvmopts`.

## Commands Used

### Navigate and inspect

```bash
cd ~/scala-spark-30-day-practice/Day-07-Immutability-Lineage-Fault-Tolerance
ls
find . -maxdepth 3 -type f | sort
```

### Compile and run

```bash
sbt compile
sbt run
```

### Save and inspect output

```bash
sbt run > output/result.txt 2>&1
cat output/result.txt
tail -n 40 output/result.txt
```

### Verify successful completion

```bash
grep -n "DAY 07 COMPLETED SUCCESSFULLY" output/result.txt
grep -n "\[success\]" output/result.txt
```

### Git workflow

```bash
git status --short
git add Day-07-Immutability-Lineage-Fault-Tolerance/
git commit -m "Complete Day 7 immutability lineage and fault tolerance"
git push origin main
```

## Project Structure

```text
Day-07-Immutability-Lineage-Fault-Tolerance/
├── .gitignore
├── .jvmopts
├── README.md
├── build.sbt
├── code/
│   └── Day07.scala
├── input/
│   └── sales.txt
├── output/
│   └── result.txt
├── screenshots/
│   ├── 01-transformation-lineage-revenue.png
│   └── 02-immutability-fault-tolerance-success.png
└── src/
    └── main/
        └── scala/
            └── Day07.scala
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

Day 7 connects three important Spark ideas: immutable RDDs form a dependency graph, lineage records how data was produced, and that lineage enables Spark to recompute missing partition data when failures occur.