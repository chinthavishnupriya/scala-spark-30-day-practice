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

## Status

**Implementation prepared. Runtime verification pending.**
