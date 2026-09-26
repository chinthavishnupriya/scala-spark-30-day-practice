# Day 27 — Real-Time Banking Project

## Objective

Implement a real-time banking pipeline using Spark Streaming/DStreams:

- Design a transaction event schema.
- Aggregate transactions by account.
- Detect suspicious bursts using windows.
- Join transactions with small branch/risk reference data.
- Use cache/persist and partitioning.
- Explain how the application would run on YARN.

These are the Day 27 requirements from the practice set. 

## Technology

- Scala 2.13.18
- Apache Spark 4.2.0
- Spark Streaming / DStreams
- sbt 1.10.11
- Java 17
- local[4]

## Event Schema

`transactionId,accountId,timestamp,amount,branchId,transactionType`

Example:

`T001,A001,10:00:01,5000,B001,ATM`

Scala model:

```scala
case class BankTransaction(
  transactionId: String,
  accountId: String,
  timestamp: String,
  amount: Double,
  branchId: String,
  transactionType: String
)
```

## Processing Pipeline

```text
TCP socket :9998
      |
      v
5-second micro-batches
      |
      v
Parse + persist transactions
      |
      +-------------------+----------------------+
      |                   |                      |
      v                   v                      v
Account totals       Burst detection       Branch/risk join
reduceByKey          20s / 10s window       partitioned join
```

## Account Aggregation

The account totals use:

```scala
transactions
  .map(t => (t.accountId, t.amount))
  .reduceByKey(_ + _)
```

For the sample data:

| Account | Total |
|---|---:|
| A001 | 13700 |
| A002 | 550 |
| A003 | 15000 |

`reduceByKey` is a keyed aggregation and can introduce shuffle work.

## Suspicious Bursts

A suspicious burst is defined for this local demonstration as **3 or more transactions for one account inside a 20-second window**, evaluated every 10 seconds.

```scala
.map(t => (t.accountId, 1))
.reduceByKeyAndWindow(
  (a, b) => a + b,
  Seconds(20),
  Seconds(10)
)
.filter { case (_, count) => count >= 3 }
```

With the supplied six records, A001 has three transactions inside the window and therefore produces the burst alert.

## Branch/Risk Reference

The small reference map contains:

| Branch | Name | Risk |
|---|---|---|
| B001 | Hyderabad Central | HIGH |
| B002 | Warangal | LOW |
| B003 | Vijayawada | MEDIUM |

The reference map is broadcast once. A partitioned Pair RDD is also created and persisted. Incoming transactions are partitioned with the same `HashPartitioner(4)` before the join.

This demonstrates how a small reference dataset can be reused while making partitioning explicit.

## Cache/Persist

The parsed transaction DStream is persisted with:

```scala
.persist(StorageLevel.MEMORY_ONLY)
```

It feeds three downstream computations:

1. Account aggregation.
2. Suspicious-window detection.
3. Branch/risk enrichment.

The small branch/risk Pair RDD is also persisted.

## Partitions and Shuffle

Important shuffle boundaries include:

- `reduceByKey` for account aggregation.
- `reduceByKeyAndWindow` for keyed window aggregation.
- The branch/risk join may require data movement, although matching partitioners are explicitly used in this demonstration.

`local[4]` provides four local execution threads. Partition counts should be tuned according to input size, available CPU, shuffle volume and task overhead.

## Sample Input

```text
T001,A001,10:00:01,5000,B001,ATM
T002,A001,10:00:03,4500,B001,ATM
T003,A001,10:00:05,4200,B001,ONLINE
T004,A002,10:00:06,300,B002,POS
T005,A003,10:00:08,15000,B003,ONLINE
T006,A002,10:00:12,250,B002,POS
```

Expected account totals:

```text
A001 -> 13700.00
A002 -> 550.00
A003 -> 15000.00
```

Expected suspicious burst:

```text
BURST ALERT: A001 -> 3 transactions in window
```

## DStreams and Micro-Batches

The application uses a 5-second batch interval. Incoming socket data is grouped into micro-batches, and the DStream transformations are evaluated for each generated RDD.

## DAG and Lineage

Conceptually:

```text
Socket DStream
    |
    v
flatMap
    |
    v
persisted transactions
    |
    +------------------+-------------------+
    |                  |                   |
    v                  v                   v
map/reduceByKey   map/window/reduce    transform/join
    |                  |                   |
    v                  v                   v
account totals     burst alerts       enriched records
```

Spark maintains lineage for the RDD computations and can recompute lost partitions where the source and transformations permit recovery.

## YARN Deployment Explanation

The code currently uses `local[4]` and a localhost socket for local practice.

For YARN, the application would be submitted with Spark's YARN master, for example:

```bash
spark-submit --master yarn --deploy-mode cluster ...
```

The driver would run according to the selected deploy mode, and YARN's ResourceManager/NodeManagers would manage cluster resources and executors.

A production streaming deployment should replace the localhost TCP source with a durable distributed source such as Kafka and externalize checkpointing/configuration. The local socket setup is intentionally for learning.

## Performance Notes

- Prefer `reduceByKey` over collecting all records to the driver.
- Persist a stream when multiple downstream branches reuse it.
- Avoid `collect()` for large production datasets.
- Tune partition counts for the workload.
- Monitor shuffle volume and data skew.
- Use broadcast only for genuinely small reference data.
- Use checkpointing for stateful/windowed streaming.
- For new production systems, evaluate Structured Streaming rather than building new systems on legacy DStreams.

## Verification

Compile:

```bash
sbt clean compile
```

Start socket:

```bash
nc -lk 9998
```

Run Spark:

```bash
rm -rf output/checkpoint
sbt run 2>&1 | tee output/day27-execution-output.txt
```

Send the records from `input/sample-transactions.txt` into the active netcat terminal.

## Project Structure

```text
Day-27-Real-Time-Banking-Project/
├── .gitignore
├── .jvmopts
├── build.sbt
├── COMMANDS.md
├── README.md
├── code/Day27.scala
├── input/sample-transactions.txt
├── output/day27-execution-output.txt
├── project/build.properties
├── screenshots/
├── src/main/scala/Day27.scala
└── troubleshooting/README.md
```

## Status

Implementation is prepared. Compilation and runtime screenshots should be captured locally after pulling the GitHub changes.
