# Day 24 — Stateless vs Stateful Streaming

## Objective

This practical demonstrates the difference between stateless and stateful processing with Apache Spark DStreams.

The implementation:
- Applies stateless transformations to streaming transaction data.
- Tracks running transaction counts by bank account.
- Compares counts from the current micro-batch with accumulated state.
- Uses checkpointing for stateful processing.
- Demonstrates how the state changes across multiple micro-batches.

## Technology Stack

| Component | Version / Configuration |
|---|---|
| Scala | 2.13.18 |
| Apache Spark | 4.2.0 |
| Spark Streaming | 4.2.0 |
| Java | 17 |
| sbt | 1.10.11 |
| Master | local[4] |
| Batch interval | 5 seconds |
| Input | TCP socket localhost:9999 |

## Project Structure

```text
Day-24-Stateless-vs-Stateful-Streaming/
├── .gitignore
├── .jvmopts
├── COMMANDS.md
├── README.md
├── build.sbt
├── code/
│   └── Day24.scala
├── input/
├── output/
├── project/
│   └── build.properties
├── screenshots/
│   ├── 01-compilation-success.png
│   ├── 02-streaming-context-started.png
│   ├── 03-batch-1-input.png
│   ├── 04-stateless-vs-stateful-batch-1.png
│   ├── 05-stateless-vs-stateful-batch-2.png
│   └── 06-complete-success.png
├── src/
│   └── main/
│       └── scala/
│           └── Day24.scala
└── troubleshooting/
```

## Streaming Input

Each input line follows this format:

```text
ACCOUNT_ID,AMOUNT
```

Example:

```text
ACC001,250
ACC002,500
ACC001,100
```

The amount is parsed as a transaction value, while the account ID is used as the key for counting.

## Processing Flow

```text
TCP Socket :9999
       |
       v
Parse account and amount
       |
       +----------------------+
       |                      |
       v                      v
Stateless branch         Stateful branch
       |                      |
       v                      v
map(account, 1)         map(account, 1)
       |                      |
       v                      v
reduceByKey              updateStateByKey
       |                      |
       v                      v
Current batch count     Accumulated count
```

## Stateless Processing

The stateless branch calculates transaction counts only from the current micro-batch:

```scala
val statelessCounts = transactions
  .map { case (account, _) => (account, 1) }
  .reduceByKey(_ + _)
```

If a batch contains:

```text
ACC001,250
ACC002,500
ACC001,100
```

the current-batch result is:

```text
ACC001 -> 2
ACC002 -> 1
```

When the next batch arrives, the stateless result starts from that new batch only.

## Stateful Processing

The stateful branch maintains a running count for each account:

```scala
val statefulCounts = transactions
  .map { case (account, _) => (account, 1) }
  .updateStateByKey[Int] {
    (newValues: Seq[Int], previousState: Option[Int]) =>
      Some(previousState.getOrElse(0) + newValues.sum)
  }
```

For the first batch:

```text
ACC001 -> 2
ACC002 -> 1
```

After another ACC001 transaction arrives, the accumulated state becomes:

```text
ACC001 -> 3
ACC002 -> 1
```

After another ACC002 transaction:

```text
ACC001 -> 3
ACC002 -> 2
```

This demonstrates the key difference: the stateful operation retains information from previous micro-batches.

## Stateless vs Stateful

| Feature | Stateless | Stateful |
|---|---|---|
| Scope | Current micro-batch | Current + previous batches |
| Example | `reduceByKey` | `updateStateByKey` |
| Remembers previous batches | No | Yes |
| Running count | No | Yes |
| State storage | Not required | Required |
| Checkpointing | Not required for this operation | Used for state recovery |
| Example result | Current transactions | Cumulative transactions |

## Micro-Batch Results

### Batch 1

The first batch produced:

```text
STATELESS RESULT
ACC001 -> 2 transaction(s) in current batch
ACC002 -> 1 transaction(s) in current batch

STATEFUL RESULT
ACC001 -> 2 transaction(s) accumulated
ACC002 -> 1 transaction(s) accumulated
```

At this point both results are equal because there is no earlier transaction state.

### Batch 2

A later batch containing an additional ACC001 transaction produced:

```text
STATELESS RESULT
ACC001 -> 1 transaction(s) in current batch

STATEFUL RESULT
ACC001 -> 3 transaction(s) accumulated
ACC002 -> 1 transaction(s) accumulated
```

This is the main demonstration: the stateless result shows only the new transaction, while the stateful result includes the previous count.

A subsequent ACC002 transaction produced:

```text
STATELESS RESULT
ACC002 -> 1 transaction(s) in current batch

STATEFUL RESULT
ACC001 -> 3 transaction(s) accumulated
ACC002 -> 2 transaction(s) accumulated
```

## Empty Micro-Batches

When no transactions arrive, the stateless branch reports no transactions for that batch.

The stateful branch continues to display the previously accumulated state:

```text
STATELESS: No transactions in this batch.

STATEFUL:
ACC001 -> 2 transaction(s) accumulated
ACC002 -> 1 transaction(s) accumulated
```

This further demonstrates that state is maintained across micro-batches.

## Important Spark Concepts

### DStream

A DStream represents a continuous stream of data divided into small time-based micro-batches.

### Transformation

Transformations define how streaming data is processed. This project uses:

- `flatMap`
- `map`
- `filter`
- `reduceByKey`
- `updateStateByKey`

### Action / Output Operation

The application uses `foreachRDD` to inspect and print each micro-batch result.

### Shuffle

Key-based operations such as `reduceByKey` require data with the same key to be brought together. This can introduce a shuffle and network/data movement.

Stateful processing additionally maintains state for keys across batches.

### Checkpointing

The application configures:

```text
output/checkpoint
```

Checkpointing is used by the stateful DStream operation to support recovery of streaming state.

## Performance Considerations

1. Use an appropriate batch interval for the workload.
2. Avoid unnecessary repeated actions on the same RDD.
3. Persist reused RDDs when repeated computation becomes expensive.
4. Keep the number of state keys under control.
5. Monitor state size as the number of accounts grows.
6. Use an appropriate checkpoint location.
7. For production streaming applications, evaluate modern Structured Streaming APIs rather than relying on legacy DStreams.

## Troubleshooting Notes

### Connection refused

The application expects a TCP producer on port 9999. Start a listener before sending test records:

```bash
nc -l 9999
```

### Local block replication warnings

Local Spark execution can display block replication warnings because there are no peer workers in a single local process setup. These warnings did not prevent the test from producing correct results.

### Deprecation warning

Spark 4.2 reports a deprecation warning for the legacy DStreams stateful API. The API is used here because the Day 24 exercise specifically focuses on DStreams and stateful processing.

## Verification

Compilation completed successfully with:

```text
sbt clean compile
[success] Total time: 16 s
```

The streaming test successfully demonstrated:
- Spark Streaming startup.
- 5-second micro-batches.
- Current-batch stateless counts.
- Accumulated stateful counts.
- Persistence of state across empty batches.
- Updated cumulative counts after later transactions.

## Screenshots

1. `01-compilation-success.png` — successful compilation.
2. `02-streaming-context-started.png` — StreamingContext startup.
3. `03-batch-1-input.png` — first transaction batch sent to the socket.
4. `04-stateless-vs-stateful-batch-1.png` — first stateless/stateful comparison.
5. `05-stateless-vs-stateful-batch-2.png` — later batch showing accumulated state.
6. `06-complete-success.png` — completed practical execution.

## Result

Day 24 successfully demonstrates the difference between stateless and stateful Spark Streaming processing. Stateless processing reports results for the current micro-batch, while stateful processing maintains running per-account counts across micro-batches.
