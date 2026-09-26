# Day 25 — Window Operations

## Objective

Implement Spark Streaming window operations using DStreams and demonstrate:

- Batch interval
- Window size
- Sliding interval
- `countByWindow`
- `reduceByKeyAndWindow`
- Rolling sales totals
- Transaction-burst detection
- Windowed processing behavior
- Checkpoint configuration

The implementation follows the Day 25 practice requirement to explain window timing, use `countByWindow` and `reduceByKeyAndWindow`, calculate rolling sales totals, and demonstrate a transaction-increase detection scenario.

## Technology Stack

- Scala 2.13.18
- Apache Spark 4.2.0
- Spark Streaming / DStreams
- sbt 1.10.11
- Java 17
- Ubuntu 26.04 LTS / WSL2

## Project Structure

```text
Day-25-Window-Operations/
├── .gitignore
├── .jvmopts
├── build.sbt
├── COMMANDS.md
├── README.md
├── code/
│   └── Day25.scala
├── input/
├── output/
│   └── checkpoint/          # generated at runtime; ignored by Git
├── project/
│   └── build.properties
├── screenshots/
│   ├── 01-compilation-success.png
│   ├── 02-streaming-context-started.png
│   ├── 03-window-input-batch-1.png
│   ├── 04-count-by-window.png
│   ├── 05-rolling-sales-and-burst-alert.png
│   └── 06-complete-success.png
├── src/main/scala/
│   └── Day25.scala
└── troubleshooting/
```

## Window Concepts

### Batch interval

The batch interval is the amount of time Spark Streaming waits before creating the next micro-batch.

This project uses:

```text
Batch interval = 5 seconds
```

Therefore, incoming socket data is processed in 5-second micro-batches.

### Window size

The window size defines how much recent streaming data is included in each window calculation.

For the quick local demonstration:

```text
Window size = 20 seconds
```

Each calculation therefore considers transactions from the latest 20 seconds.

### Sliding interval

The slide interval determines how often the window moves forward.

This project uses:

```text
Slide interval = 10 seconds
```

So a new window result is generated every 10 seconds.

The values are multiples of the 5-second batch interval, which makes them suitable for this DStream window demonstration.

## Why the Test Uses 20 Seconds

The practice scenario asks for detection of a sudden increase in transactions during a **10-minute window**. A 10-minute window is appropriate for a real monitoring scenario, but it would make a small local demonstration slow.

Therefore, this implementation uses a scaled-down:

```text
20-second window
10-second slide
```

for observable testing.

The same design can be applied to a 10-minute production-style window by using an appropriate window duration such as `Seconds(600)), with a slide interval chosen as a valid multiple of the 5-second batch interval.

## Input Format

Transactions are sent through TCP port 9999:

```text
ACCOUNT_ID,AMOUNT
```

Example:

```text
ACC001,100
ACC002,250
ACC001,150
```

The application parses each line into:

```scala
(accountId, amount)
```

Invalid transaction lines are ignored when they do not contain two fields or when the amount cannot be parsed as a number.

## Processing Flow

```text
TCP socket
    |
    v
5-second micro-batches
    |
    v
Parse ACCOUNT_ID,AMOUNT
    |
    +----------------------+
    |                      |
    v                      v
countByWindow       reduceByKeyAndWindow
    |                      |
    v                      v
Transaction count    Rolling sales by account
    |
    v
Transaction burst detection
```

## 1. countByWindow

The project uses:

```scala
transactions.countByWindow(
  Seconds(20),
  Seconds(10)
)
```

This produces the total number of transactions inside the current 20-second window.

Example observed result:

```text
COUNT BY WINDOW - ...
Transactions in last 20 seconds: 3
```

## 2. reduceByKeyAndWindow

Rolling sales are calculated per bank account:

```scala
transactions.reduceByKeyAndWindow(
  (a: Double, b: Double) => a + b,
  Seconds(20),
  Seconds(10)
)
```

Example observed result:

```text
ACC001 -> rolling sales = 250.00
ACC002 -> rolling sales = 250.00
```

As older records leave the window and new records enter, the rolling totals change.

## 3. Transaction Burst Detection

The same window technique is used to count transactions per account:

```scala
transactions
  .map { case (account, _) => (account, 1) }
  .reduceByKeyAndWindow(
    (a: Int, b: Int) => a + b,
    Seconds(20),
    Seconds(10)
  )
```

An account is flagged by the demonstration when it reaches at least 3 transactions inside the current window:

```text
ALERT: ACC001 -> 3 transactions in window
```

This models the practice scenario of detecting a sudden increase in transaction activity.

## Observed Execution

The application successfully started with:

```text
DAY 25 - WINDOW OPERATIONS
Batch interval : 5 seconds
Window size    : 20 seconds
Slide interval : 10 seconds
Listening on   : localhost:9999
StreamingContext started.
```

The tested window calculation produced:

```text
Transactions in last 20 seconds: 3

ACC001 -> rolling sales = 250.00
ACC002 -> rolling sales = 250.00
```

Later window calculations produced:

```text
Transactions in last 20 seconds: 3

ACC001 -> rolling sales = 300.00
ACC002 -> rolling sales = 300.00
```

This confirms that the window contents and rolling totals change as the window slides.

## Spark Concepts

### Transformations

The main transformations are:

- `flatMap`
- `map`
- `filter`
- `reduceByKeyAndWindow`

### Output / actions

The application uses `foreachRDD` to process each generated RDD and uses operations such as:

- `isEmpty()`
- `collect()`

for the local demonstration.

### Shuffle

`reduceByKeyAndWindow` performs keyed aggregation and can involve a shuffle because records for the same key must be brought together.

With larger data volumes, shuffle cost can become significant.

### Partitions

Spark processes RDD data through partitions. More appropriate partitioning can improve parallelism, while excessive partitions can add scheduling overhead.

### Window state

Window operations retain enough recent data to calculate results for the configured window. Checkpointing is configured because Spark Streaming requires a checkpoint directory for this windowed computation.

## Checkpoint Configuration

The application contains:

```scala
ssc.checkpoint("output/checkpoint")
```

The checkpoint directory is intentionally ignored by Git:

```text
output/checkpoint/
```

Generated runtime state should not be committed to the repository.

## Performance Considerations

1. Use a window duration appropriate to the business requirement.
2. Keep the slide interval aligned with the batch interval.
3. Avoid unnecessary `collect()` calls for large production streams.
4. Avoid repeatedly triggering actions such as `isEmpty()` followed by `collect()` when a single computation can provide the required result.
5. Monitor shuffle volume for keyed window aggregations.
6. Choose partition counts according to input volume and available cores.
7. Checkpoint streaming computations that require checkpoint state.
8. For large-scale production workloads, consider newer Structured Streaming APIs rather than building new systems around legacy DStreams.

## Troubleshooting

### Checkpoint directory error

If the application fails with:

```text
The checkpoint directory has not been set.
Please set it by StreamingContext.checkpoint().
```

add:

```scala
ssc.checkpoint("output/checkpoint")
```

before starting the StreamingContext.

### Port 9999 already in use

Check:

```bash
ss -ltnp | grep 9999
```

Stop the old process or use another available port consistently in both the application and TCP client.

### Local Spark replication warnings

Warnings such as:

```text
Expecting 1 replicas with only 0 peer/s
replicated to only 0 peer(s) instead of 1 peers
```

can appear in a local single-node Spark environment. They did not prevent the Day 25 streaming computation from producing window results.

## Screenshots

1. **Compilation success** — successful `sbt clean compile`.
2. **Streaming context started** — Day 25 application and configured intervals.
3. **Window input batch** — transaction records sent to the socket.
4. **Count by window** — transaction count for the active window.
5. **Rolling sales and burst alert** — keyed rolling totals and transaction detection.
6. **Complete success** — final execution evidence.

## Commands

See [COMMANDS.md](COMMANDS.md) for setup, compilation, execution, input, verification, and Git commands.

## Result

Day 25 demonstrates how Spark Streaming windows combine recent micro-batches into time-based calculations. The implementation successfully demonstrates `countByWindow`, `reduceByKeyAndWindow`, rolling sales totals, sliding windows, and transaction-burst detection.
