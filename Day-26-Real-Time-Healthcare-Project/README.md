# Day 26 — Real-Time Healthcare Project

## Objective

Implement a real-time healthcare monitoring project using Spark Streaming and demonstrate:

- Patient vital event schema
- Real-time vital stream processing
- Abnormal-vital detection
- Broadcast thresholds
- Accumulator usage
- Window/stateful processing
- Repeated abnormal-reading alerts
- Partitions
- DAG and lineage
- Checkpointing and fault tolerance
- Performance considerations

## Technology Stack

- Scala 2.13.18
- Apache Spark 4.2.0
- Spark Streaming / DStreams
- sbt 1.10.11
- Java 17
- Ubuntu 26.04 LTS / WSL2

## Project Structure

```text
Day-26-Real-Time-Healthcare-Project/
├── .gitignore
├── .jvmopts
├── build.sbt
├── COMMANDS.md
├── README.md
├── code/
│   └── Day26.scala
├── input/
│   └── sample-vitals.txt
├── output/
│   └── day26-execution-output.txt
├── project/
│   └── build.properties
├── screenshots/
├── src/main/scala/
│   └── Day26.scala
└── troubleshooting/
    └── README.md
```

## Patient Vital Schema

Each patient event contains five fields:

```text
patientId,timestamp,heartRate,temperature,spo2
```

Example:

```text
P001,10:00:04,130,38.1,91
```

The Scala model is:

```scala
case class PatientVital(
    patientId: String,
    timestamp: String,
    heartRate: Double,
    temperature: Double,
    spo2: Double
)
```

## Abnormal-Vital Thresholds

The demonstration uses:

| Vital | Abnormal condition |
|---|---|
| Heart rate | > 120 |
| Temperature | > 38.0 |
| SpO2 | < 94 |

Thresholds are stored in a `VitalThresholds` case class and distributed using a Spark broadcast variable.

```scala
val thresholds = VitalThresholds(
  maxHeartRate = 120.0,
  maxTemperature = 38.0,
  minSpo2 = 94.0
)

val broadcastThresholds =
  ssc.sparkContext.broadcast(thresholds)
```

Broadcasting avoids repeatedly shipping the same threshold configuration with every task.

## Streaming Configuration

The application creates a DStream using a TCP socket:

```scala
val lines =
  ssc.socketTextStream("localhost", 9999)
```

The micro-batch interval is:

```text
5 seconds
```

Window configuration:

```text
Window size    = 20 seconds
Slide interval = 10 seconds
```

The 20-second window is a scaled local demonstration that makes repeated abnormal readings observable quickly.

## Processing Flow

```text
TCP socket
    |
    v
5-second micro-batches
    |
    v
Parse patient vital events
    |
    v
Broadcast threshold comparison
    |
    +--------------------------+
    |                          |
    v                          v
Immediate alerts       20-sec / 10-sec window
                               |
                               v
                     Repeated abnormal readings
```

## Parsing the Stream

Each incoming line is split into five fields.

Invalid records are ignored when:

- The number of fields is not 5.
- A numeric vital value cannot be converted to `Double`.

The parsing transformation is implemented using `flatMap`.

## Abnormal-Vital Detection

The application checks:

```scala
vital.heartRate > limit.maxHeartRate ||
vital.temperature > limit.maxTemperature ||
vital.spo2 < limit.minSpo2
```

When a reading is abnormal, the accumulator is incremented.

The abnormal stream is persisted:

```scala
.persist(StorageLevel.MEMORY_ONLY)
```

This allows multiple downstream operations to reuse the computed abnormal stream.

## Accumulator

The application creates:

```scala
val abnormalVitalAccumulator =
  ssc.sparkContext.longAccumulator(
    "AbnormalVitalRecords"
  )
```

The accumulator counts abnormal vital records observed by the demonstration.

For the tested six-record input, four records are abnormal.

## Repeated Abnormal Readings

The abnormal stream is converted into patient/count pairs:

```scala
.map(vital => (vital.patientId, 1))
```

Then the application uses:

```scala
.reduceByKeyAndWindow(
  (a: Int, b: Int) => a + b,
  Seconds(20),
  Seconds(10)
)
```

Patients with at least two abnormal readings in the active window generate a repeated-reading alert.

## Test Input

The tested sample contains six records:

```text
P001,10:00:01,78,36.7,98
P002,10:00:02,125,37.2,94
P003,10:00:03,82,38.5,97
P001,10:00:04,130,38.1,91
P002,10:00:05,128,37.4,93
P004,10:00:06,75,36.6,99
```

Expected abnormal records:

- P001 → 1 abnormal reading
- P002 → 2 abnormal readings
- P003 → 1 abnormal reading
- P004 → 0 abnormal readings

Total:

```text
4 abnormal records
```

## Observed Execution

The tested application successfully started the StreamingContext with the configured batch and window intervals.

The abnormal alerts produced were:

```text
ALERT: P001 | HR=130.0 | Temp=38.1 | SpO2=91.0
ALERT: P002 | HR=125.0 | Temp=37.2 | SpO2=94.0
ALERT: P002 | HR=128.0 | Temp=37.4 | SpO2=93.0
ALERT: P003 | HR=82.0 | Temp=38.5 | SpO2=97.0
```

The repeated-reading logic detected:

```text
REPEATED ALERT: P002 -> 2 abnormal readings in window
```

The stream statistics reported:

```text
Patients processed: 6
Total abnormal records: 4
```

The complete execution output is stored in:

```text
output/day26-execution-output.txt
```

## Spark Transformations

The main transformations are:

- `flatMap` — parses input records.
- `filter` — detects abnormal readings.
- `map` — converts abnormal readings to patient/count pairs.
- `reduceByKeyAndWindow` — aggregates repeated abnormal readings over a window.

## Spark Actions / Output Operations

The application uses `foreachRDD` to process each generated RDD.

Within these output operations it uses:

- `isEmpty()`
- `collect()`
- `count()`

For this small local demonstration, `collect()` is acceptable. It should not be used indiscriminately with large production datasets because it brings data to the driver.

## Shuffle

`reduceByKeyAndWindow` performs keyed aggregation.

Records belonging to the same patient key must be grouped for aggregation, so this operation can involve shuffle processing.

Shuffle can become a significant performance cost as data volume increases.

## Partitions

The application uses:

```scala
.setMaster("local[4]")
```

This provides four local execution threads for the Spark application.

RDDs are divided into partitions so Spark can process data in parallel.

Partition count should be selected according to:

- Input volume
- Available CPU cores
- Task scheduling overhead
- Shuffle workload

## DAG and Lineage

The streaming computation creates a lineage similar to:

```text
Socket DStream
      |
      v
flatMap
      |
      v
PatientVital records
      |
      v
filter
      |
      v
AbnormalVitals
      |
      +----------------------+
      |                      |
      v                      v
Immediate alerts      map + reduceByKeyAndWindow
                             |
                             v
                     Repeated alerts
```

Spark uses lineage information to understand how RDD data was derived and to recompute lost partitions when possible.

## Checkpointing and Fault Tolerance

The application configures:

```scala
ssc.checkpoint("output/checkpoint")
```

Checkpointing is important for the stateful/windowed streaming computation.

The checkpoint directory is generated during execution and ignored by Git:

```text
output/checkpoint/
```

Spark Streaming can use checkpoint information to recover streaming state and metadata after failures.

Checkpointing does not mean that every application state is magically persisted forever; the application still depends on appropriate streaming recovery and external system design for production reliability.

## Performance Considerations

1. Persist reusable DStreams when multiple downstream operations depend on the same computation.
2. Avoid unnecessary `collect()` operations for large streams.
3. Monitor shuffle volume caused by keyed aggregations.
4. Select partition counts according to workload and available cores.
5. Keep window and slide durations appropriate for the monitoring requirement.
6. Use checkpointing for stateful streaming computations.
7. Avoid unnecessary repeated actions on the same RDD.
8. For new production systems, evaluate Structured Streaming rather than designing new applications around legacy DStreams.

## Local Spark Warnings

A local single-node Spark environment may show warnings related to:

- Loopback hostname resolution
- Native Hadoop libraries
- Block replication with zero peers

These warnings can occur in local WSL execution and do not necessarily indicate application failure. The successful processing output is the relevant runtime evidence.

## Troubleshooting

See:

```text
troubleshooting/README.md
```

for socket connection, input-format, repeated-window, accumulator, and local Spark warning troubleshooting.

## Verification Commands

Compile:

```bash
sbt clean compile
```

Start socket server:

```bash
nc -lk 9999
```

Start Spark:

```bash
rm -rf output/checkpoint
sbt run 2>&1 | tee output/day26-execution-output.txt
```

Inspect output:

```bash
cat output/day26-execution-output.txt
```

Inspect sample input:

```bash
cat input/sample-vitals.txt
```

## Result

Day 26 successfully demonstrates a real-time healthcare monitoring workflow using Spark Streaming. The implementation processes patient vital events, broadcasts threshold configuration, detects abnormal readings, counts abnormal records with an accumulator, and identifies repeated abnormal readings using a sliding window.
