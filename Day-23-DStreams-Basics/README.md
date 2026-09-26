# Day 23 — DStreams Basics

## Objective

Implement the Day 23 DStreams exercise using Apache Spark Streaming and Scala.

The practice requirements are to:

- Create a streaming context and define a batch interval.
- Read a socket/text stream.
- Apply `map`, `filter`, and `flatMap`.
- Explain micro-batch processing.
- Stream application logs and count `ERROR` messages during each interval.

## Technologies

| Component | Version |
|---|---|
| Scala | 2.13.18 |
| Apache Spark | 4.2.0 |
| Spark Streaming | 4.2.0 |
| Java | 17 |
| sbt | 1.10.11 |
| Environment | Ubuntu / WSL2 |

## Project Structure

```text
Day-23-DStreams-Basics/
├── .gitignore
├── .jvmopts
├── build.sbt
├── COMMANDS.md
├── code/
│   └── Day23.scala
├── input/
├── output/
├── project/
│   └── build.properties
├── screenshots/
│   ├── 01-compilation-success.png
│   ├── 02-streaming-context-started.png
│   ├── 03-input-log-stream.png
│   ├── 04-dstream-transformations.png
│   ├── 05-micro-batch-processing.png
│   └── 06-complete-success.png
├── src/
│   └── main/
│       └── scala/
│           └── Day23.scala
└── troubleshooting/
    └── README.md
```

## Implementation

The application creates a `StreamingContext` with a **5-second batch interval**:

```scala
val ssc = new StreamingContext(conf, Seconds(5))
```

The input is a socket stream:

```scala
val lines = ssc.socketTextStream("localhost", 9999)
```

### 1. flatMap

Each incoming log line is split into words:

```scala
val words = lines
  .flatMap(_.split("\\s+"))
  .filter(_.nonEmpty)
```

The resulting DStream is evaluated with `foreachRDD` so the number of generated words can be observed for each micro-batch.

### 2. filter

Only log lines containing `ERROR` are selected:

```scala
val errorLines = lines.filter(_.contains("ERROR"))
```

### 3. map

Each matching log line is converted into a key-value pair:

```scala
val errorPairs = errorLines.map(_ => ("ERROR", 1))
```

### 4. reduceByKey

The ERROR records are counted within each micro-batch:

```scala
val errorCounts = errorPairs.reduceByKey(_ + _)
```

### 5. Output

`foreachRDD` prints the result for every batch:

```text
Micro-batch time: ...
ERROR count: 3
```

The application also applies `map` to convert complete log lines to uppercase and reports how many log lines were processed.

## Micro-Batch Processing

DStreams represent a continuous stream as a sequence of small RDDs.

With a 5-second batch interval:

```text
Incoming socket data
        │
        ▼
   5-second window
        │
        ▼
   Micro-batch RDD
        │
        ├── flatMap
        ├── filter
        ├── map
        └── reduceByKey
                │
                ▼
          ERROR count
```

The application therefore does not process the entire stream as one permanent RDD. Spark periodically collects newly received records into a micro-batch and runs the DStream computation for that batch.

## Observed Test Result

The streaming application started successfully and produced repeated 5-second micro-batches.

A test batch processed **6 log lines** and reported:

```text
ERROR count: 3
Processed 6 log line(s) in micro-batch ...
```

Other intervals with no ERROR records reported:

```text
ERROR count: 0
```

This demonstrates per-interval processing rather than a single cumulative ERROR count.

## Transformations and Actions

### Transformations

- `flatMap`
- `filter`
- `map`
- `reduceByKey`

These define how incoming records are transformed.

### Output / Actions

- `foreachRDD`
- `rdd.count()`
- `rdd.collect()`
- `rdd.isEmpty()`

These cause the RDD computation for each micro-batch to be evaluated.

## Execution Flow

1. Create `SparkConf`.
2. Create `StreamingContext`.
3. Set the batch interval to 5 seconds.
4. Connect to `localhost:9999`.
5. Receive log lines.
6. Split lines with `flatMap`.
7. Filter ERROR records.
8. Convert ERROR records to key-value pairs with `map`.
9. Count ERROR records with `reduceByKey`.
10. Print each micro-batch result using `foreachRDD`.
11. Continue processing until the application is stopped.

## Compilation Verification

The final source compiled successfully:

```text
[info] compiling 1 Scala source ...
[warn] 1 deprecation (since Spark 3.4.0)
[warn] one warning found
[success] Total time: 12 s
```

The warning is a Spark deprecation warning and did not prevent compilation.

## Evidence

Six screenshots are included:

1. **Compilation success** — final Scala source compiles.
2. **StreamingContext started** — Spark Streaming application starts with a 5-second interval.
3. **Input log stream** — sample application log records are supplied to the socket.
4. **DStream transformations** — map/filter/flatMap processing is demonstrated.
5. **Micro-batch processing** — ERROR counts are printed for individual intervals.
6. **Complete success** — final execution evidence.

## Performance Notes

The Day 23 implementation is intentionally small and educational.

Important observations:

- A shorter batch interval can increase scheduling overhead.
- A longer interval can increase processing latency.
- `reduceByKey` performs aggregation by key and can involve data movement.
- Calling multiple actions independently on the same RDD can cause repeated computation unless persistence is appropriate.
- Production streaming applications require additional concerns such as fault tolerance, monitoring, checkpointing where applicable, and controlled resource usage.

## Result

**Day 23 — DStreams Basics completed successfully.**

The project demonstrates socket-based DStream input, 5-second micro-batches, `flatMap`, `filter`, `map`, `reduceByKey`, and per-batch ERROR counting.
