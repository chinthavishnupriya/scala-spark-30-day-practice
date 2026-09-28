# Day 28 — Real-Time Booking Project

## Objective

Build a real-time hotel/flight/bus-style booking pipeline using Spark Streaming/DStreams. The project demonstrates:

- Booking and cancellation events.
- Cumulative booking/cancellation state.
- Occupancy and availability calculation.
- Pair RDD window operations.
- Broadcast reference data.
- Spark SQL reporting.

## Technology

- Scala 2.13.18
- Apache Spark 4.2.0
- Spark Streaming / DStreams
- Spark SQL
- sbt 1.10.11
- Java 17
- local[4]

## Event Schema

`bookingId,customerId,eventType,timestamp,routeId,seats`

Supported event types:

- `BOOK` — increases booked seats.
- `CANCEL` — increases cancelled seats and therefore reduces occupied seats.

## Reference Data

| Route | Name | Capacity | Mode |
|---|---|---:|---|
| R001 | Hyderabad-Warangal | 40 | BUS |
| R002 | Hyderabad-Vijayawada | 50 | BUS |
| R003 | Hyderabad-Tirupati | 45 | TRAIN |

The small reference map is broadcast to executors.

## Stateful Booking/Cancellation Tracking

The application uses `updateStateByKey` to maintain cumulative route state across micro-batches.

- BOOK → `(bookedSeats + seats, cancelledSeats)`
- CANCEL → `(bookedSeats, cancelledSeats + seats)`

Occupied seats:

`max(0, bookedSeats - cancelledSeats)`

Available seats:

`max(0, capacity - occupiedSeats)`

## Window Operations

BOOK events are converted to Pair RDDs keyed by route and processed with:

- Window: 20 seconds
- Slide: 10 seconds
- Operation: `reduceByKeyAndWindow`

This gives a rolling booking-event count independent of the cumulative occupancy state.

## Spark SQL Reporting

For each non-empty state RDD:

1. Enrich route state using broadcast reference data.
2. Convert rows to a DataFrame.
3. Register temporary view `booking_report`.
4. Run Spark SQL.
5. Calculate occupancy percentage.
6. Print the report.

Report columns:

- routeId
- routeName
- mode
- capacity
- bookedSeats
- cancelledSeats
- occupiedSeats
- availableSeats
- occupancyPercent

## Processing Flow

```text
TCP socket :9997
       |
       v
5-second micro-batches
       |
       v
Parse + validate events
       |
       +----------------------+----------------------+
       |                      |                      |
       v                      v                      v
Stateful state          Pair RDD window       Broadcast reference
BOOK/CANCEL state       20s / 10s counts       route metadata
       |                      |                      |
       +----------------------+----------------------+
                              |
                              v
                        Spark SQL report
```

## Transformations

- `flatMap` — parses and validates input.
- `map` — creates state updates and Pair RDD keys.
- `filter` — selects BOOK events for the rolling window.
- `reduceByKeyAndWindow` — computes route-level rolling booking counts.
- `updateStateByKey` — maintains cumulative state.
- RDD `map` — combines state with broadcast reference data.

## Output Boundaries

- `foreachRDD` processes each micro-batch result.
- `collect()` and DataFrame `show(false)` are used only for the tiny practice dataset.
- Production systems should avoid collecting large results to the driver.

## Shuffle and Performance

Potential shuffle work occurs at keyed window aggregation and stateful keyed processing. The current demonstration uses `local[4]`; partition counts should be tuned for actual workload size and key distribution.

The route reference is small and broadcast so the reference map does not need to be shipped repeatedly as an ordinary closure.

## Checkpointing

The StreamingContext uses:

`output/checkpoint`

The checkpoint directory is ignored by Git because it contains runtime state.

## YARN / Production Note

The practice implementation intentionally uses `local[4]` and a localhost socket. A cluster deployment should use a durable distributed input source such as Kafka rather than a localhost-only socket.

Example deployment shape:

```bash
spark-submit --master yarn --deploy-mode cluster --class Day28 path/to/day28.jar
```

## Sample Input

```text
BK001,C001,BOOK,10:00:01,R001,2
BK002,C002,BOOK,10:00:03,R001,1
BK003,C003,BOOK,10:00:05,R002,3
BK004,C004,BOOK,10:00:07,R003,2
BK005,C001,CANCEL,10:00:09,R001,1
BK006,C005,BOOK,10:00:11,R001,2
BK007,C006,BOOK,10:00:13,R002,1
```

Expected cumulative state:

| Route | Booked | Cancelled | Occupied | Available |
|---|---:|---:|---:|---:|
| R001 | 5 | 1 | 4 | 36 |
| R002 | 4 | 0 | 4 | 46 |
| R003 | 2 | 0 | 2 | 43 |

Expected rolling BOOK counts:

| Route | Booking events |
|---|---:|
| R001 | 3 |
| R002 | 2 |
| R003 | 1 |

## Verification

Compile:

```bash
sbt clean compile
```

Terminal 1:

```bash
nc -lk 9997
```

Terminal 2:

```bash
mkdir -p output
rm -rf output/checkpoint
sbt run 2>&1 | tee output/day28-execution-output.txt
```

Then paste the sample events into the netcat terminal.

## Project Structure

```text
Day-28-Real-Time-Booking-Project/
├── .gitignore
├── .jvmopts
├── build.sbt
├── code/Day28.scala
├── input/sample-booking-events.txt
├── project/build.properties
├── README.md
├── src/main/scala/Day28.scala
└── troubleshooting/README.md
```

## Verification Results

The Day 28 implementation was compiled and executed successfully with the supplied sample events.

Verified during runtime:

- StreamingContext started with a 5-second micro-batch interval.
- 20-second booking window with a 10-second slide produced rolling booking counts.
- Stateful BOOK/CANCEL processing produced the expected cumulative route state.
- Broadcast route reference data was used for capacity and mode enrichment.
- Spark SQL produced occupancy and availability reports.
- Execution output was captured in `output/day28-execution-output.txt`.
- Six execution screenshots were captured under `screenshots/`.

Final cumulative state observed:

| Route | Booked | Cancelled | Occupied | Available | Occupancy |
|---|---:|---:|---:|---:|---:|
| R001 | 5 | 1 | 4 | 36 | 10.00% |
| R002 | 4 | 0 | 4 | 46 | 8.00% |
| R003 | 2 | 0 | 2 | 43 | 4.44% |

The rolling window reached:

```text
R001 -> 3 bookings
R002 -> 2 bookings
R003 -> 1 booking
```

The first observed window can contain fewer events when events arrive across micro-batch/window boundaries; this is expected for a live socket stream.

### Execution Evidence

```text
output/
└── day28-execution-output.txt

screenshots/
├── 01-compilation-success.png
├── 02-streaming-started.png
├── 03-rolling-booking-count.png
├── 04-stateful-booking-report.png
├── 05-spark-sql-report.png
└── 06-complete-execution.png
```

### Local Execution Notes

The project intentionally runs with `local[4]` and a localhost TCP socket for practice. Single-node Spark may print block-replication warnings because there are no peer executors; these warnings did not prevent the application from processing the sample events or producing the required reports.

For larger workloads, avoid `collect()` on large RDDs and `show(false)` on unbounded result sets. A production deployment should use a durable distributed streaming source and an appropriate state-management/recovery design.

## Status

**Day 28 complete — implementation, compilation, runtime verification, output capture, and execution screenshots completed.**

## Detailed Architecture

The project is organized as a small real-time booking pipeline:

```text
Booking Events
     |
     v
TCP Socket :9997
     |
     v
5-second DStream batches
     |
     +-------------------------+--------------------------+
     |                         |                          |
     v                         v                          v
Event validation        Stateful Pair RDD          BOOK-only Pair RDD
     |                         |                          |
     |                         v                          v
     |                  updateStateByKey          reduceByKeyAndWindow
     |                         |                          |
     |                         v                          v
     |                  Route state                 20s / 10s window
     |                         |                          |
     +-------------------------+--------------------------+
                               |
                               v
                    Broadcast route reference
                               |
                               v
                         Spark SQL report
```

## Event Processing Rules

Every input record is expected to contain:

```text
bookingId,customerId,eventType,timestamp,routeId,seats
```

The parser accepts only records with six fields, an event type of BOOK or CANCEL, and a positive integer seat count. Invalid records are ignored.

BOOK events contribute to both cumulative booking state and the rolling booking-event window. CANCEL events contribute to cumulative cancellation state but are excluded from the BOOK-only rolling count.

## State Calculation

For each route, the application maintains two cumulative values:

```text
bookedSeats
cancelledSeats
```

The derived values are:

```text
occupiedSeats = max(0, bookedSeats - cancelledSeats)
availableSeats = max(0, capacity - occupiedSeats)
```

Example:

```text
R001
bookedSeats    = 5
cancelledSeats = 1
capacity       = 40

occupiedSeats  = max(0, 5 - 1) = 4
availableSeats = max(0, 40 - 4) = 36
```

## Window Calculation

The rolling booking stream filters for BOOK events and maps each event to:

```text
(routeId, 1)
```

`reduceByKeyAndWindow` then aggregates the number of booking events per route over a 20-second window with a 10-second slide.

Important distinction:

- The rolling count measures booking events in the current window.
- The stateful report measures cumulative booked and cancelled seats.
- The two values therefore answer different questions and should not be expected to be identical.

## Broadcast Reference Data

The route metadata is small and read-only during the practice run, so it is placed in a Spark broadcast variable.

Broadcast data supplies:

- Route name
- Capacity
- Transport mode

This avoids treating the small reference map as a normal repeated dependency for every state record.

## Spark SQL Reporting Flow

For each non-empty state RDD, the application:

1. Reads the current route state.
2. Looks up route metadata from the broadcast variable.
3. Builds report rows.
4. Converts the rows into a DataFrame.
5. Creates the temporary view `booking_report`.
6. Executes a SQL query.
7. Calculates `occupancyPercent`.
8. Orders the report by route ID.
9. Displays the result.

The occupancy formula is:

```text
occupancyPercent = (occupiedSeats * 100) / capacity
```

## Transformations and Actions

### Transformations

- `flatMap` parses and validates socket records.
- `map` converts events into state-update pairs.
- `filter` selects BOOK events for the rolling stream.
- `map` converts BOOK events into `(routeId, 1)` pairs.
- `reduceByKeyAndWindow` performs keyed window aggregation.
- `updateStateByKey` maintains cumulative state.
- RDD `map` enriches state with broadcast reference data.

### Actions / Output Operations

- `foreachRDD` processes each generated RDD.
- `isEmpty()` checks whether a result contains records.
- `collect()` retrieves the tiny rolling result for display.
- `show(false)` displays the small Spark SQL report.

`collect()` is appropriate here only because the demonstration dataset is intentionally small.

## Partitioning and Shuffle Boundaries

The project uses `local[4]`, giving Spark four local execution threads for the practice run.

Key-based operations such as `reduceByKeyAndWindow` and stateful keyed processing require data to be grouped by route key. These operations can therefore introduce shuffle or state-management work.

The project does not hard-code a production partition count because the correct number depends on input volume, key distribution, executor resources, and cluster configuration.

## Performance Considerations

For this educational project:

- The broadcast route map is small.
- The sample output is small enough for driver-side display.
- `local[4]` is sufficient for local testing.
- The checkpoint directory supports streaming recovery state.

For larger workloads:

- Avoid collecting large RDDs to the driver.
- Tune partitions according to workload and cluster resources.
- Use a durable distributed streaming source.
- Define state retention and recovery requirements.
- Monitor processing time, batch duration, input rate, and scheduling delay.

## Test Cases

### Test 1 — BOOK event

Input:

```text
BK001,C001,BOOK,10:00:01,R001,2
```

Expected effect:

```text
R001 bookedSeats increases by 2.
R001 cancelledSeats is unchanged.
```

### Test 2 — CANCEL event

Input:

```text
BK005,C001,CANCEL,10:00:09,R001,1
```

Expected effect:

```text
R001 cancelledSeats increases by 1.
Occupied seats are reduced through the derived state calculation.
```

### Test 3 — Invalid event type

Input:

```text
BK008,C007,RESERVE,10:00:15,R001,1
```

Expected effect:

```text
The record is ignored.
```

### Test 4 — Invalid seat count

Input:

```text
BK009,C008,BOOK,10:00:16,R001,-1
```

Expected effect:

```text
The record is ignored.
```

## Sample Execution Interpretation

The verified execution demonstrated that the application starts the streaming context, receives socket events, produces rolling counts, maintains cumulative state, and generates Spark SQL reports.

The first rolling window may show fewer events than a later window because the socket events are received according to actual micro-batch timing. A later complete window produced the expected sample counts.

## Reproducibility

To reproduce the practical from a clean state:

```bash
cd ~/scala-spark-30-day-practice/Day-28-Real-Time-Booking-Project
rm -rf output/checkpoint
sbt clean compile
```

Start the socket in one terminal:

```bash
nc -lk 9997
```

Start Spark in another terminal:

```bash
mkdir -p output
sbt run 2>&1 | tee output/day28-execution-output.txt
```

Paste the sample events into the socket terminal.

## Learning Outcomes

After completing Day 28, the practical demonstrates understanding of:

- Real-time DStream input.
- Micro-batch processing.
- Stateful stream aggregation.
- Pair RDD operations.
- Windowed aggregation.
- Broadcast variables.
- DataFrame creation from RDD data.
- Spark SQL temporary views.
- Occupancy and availability calculations.
- Checkpointing concepts.
- Local Spark execution and troubleshooting.

## Evidence Included in Repository

The completed project contains source code, sample input, build configuration, commands, troubleshooting notes, runtime output, and execution screenshots.

```text
Day-28-Real-Time-Booking-Project/
├── build.sbt
├── .jvmopts
├── project/build.properties
├── input/sample-booking-events.txt
├── src/main/scala/Day28.scala
├── code/Day28.scala
├── COMMANDS.md
├── README.md
├── troubleshooting/README.md
├── output/day28-execution-output.txt
└── screenshots/
    ├── 01-compilation-success.png
    ├── 02-streaming-started.png
    ├── 03-rolling-booking-count.png
    ├── 04-stateful-booking-report.png
    ├── 05-spark-sql-report.png
    └── 06-complete-execution.png
```