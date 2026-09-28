# Day 28 Troubleshooting

## Port 9997 already in use

```bash
ss -ltnp | grep 9997
```

Stop the old process and restart netcat.

## Spark starts but no events appear

Check that:

1. `nc -lk 9997` is running.
2. Spark is running in Terminal 2.
3. Events are pasted into the active netcat terminal.
4. Each event has six comma-separated fields.

## State totals are incorrect

For the supplied sample:

- R001: 5 booked, 1 cancelled → 4 occupied.
- R002: 4 booked → 4 occupied.
- R003: 2 booked → 2 occupied.

## No rolling window output

Send the BOOK events close together so they fall within the same 20-second window.

## No Spark SQL report

The SQL report is produced only when the state RDD is non-empty. Check that at least one valid event reached the stateful stream.

## Checkpoint reset

```bash
rm -rf output/checkpoint
```

## Large-data warning

The example uses `collect()` and `show(false)` for a tiny educational dataset. Do not collect large production results to the driver.

## Stateful-streaming note

`updateStateByKey` maintains state across batches. A production application should define state-retention and recovery requirements and use a durable streaming design appropriate to the workload.

## Environment Verification

Before running the project, verify the main tools:

    java -version
    scala -version
    sbt --version

The project is configured for Java 17, Scala 2.13.18, sbt 1.10.11, and Apache Spark 4.2.0.

If compilation fails, run:

    sbt clean compile

## Port and Socket Troubleshooting

### Port 9997 is already in use

Check the listener:

    ss -ltnp | grep 9997

Stop the old nc process before starting a new one. If the Spark application is still running, stop it with Ctrl+C.

### Spark starts but no events appear

Check that nc -lk 9997 is running, Spark is running, events are pasted into the active netcat terminal, each event has six comma-separated fields, the event type is BOOK or CANCEL, and the seat count is a positive integer.

Valid example:

    BK001,C001,BOOK,10:00:01,R001,2

Malformed records are discarded by the parser and therefore do not appear in the output.

## Checkpoint Troubleshooting

The application stores streaming checkpoint data in output/checkpoint.

For a fresh practice run:

    rm -rf output/checkpoint

Do not commit the checkpoint directory; it is excluded by .gitignore.

## Window Output Troubleshooting

The rolling booking count uses a 5-second batch interval, 20-second window, and 10-second slide.

A rolling window depends on actual event arrival time. Therefore, the first displayed window may contain fewer events than a later complete window.

For the supplied sample, a later complete window should reach:

    R001 -> 3 bookings
    R002 -> 2 bookings
    R003 -> 1 booking

This timing behavior is expected.

## Stateful Booking/Cancellation Troubleshooting

For the supplied sample:

    R001 -> booked=5, cancelled=1, occupied=4, available=36
    R002 -> booked=4, cancelled=0, occupied=4, available=46
    R003 -> booked=2, cancelled=0, occupied=2, available=43

If totals are higher after repeated testing, reset output/checkpoint before a clean test.

## Spark SQL Troubleshooting

If no SQL report appears, confirm that valid events were sent and check the execution log:

    grep -n "SPARK SQL BOOKING REPORT" output/day28-execution-output.txt

## Route Reference Troubleshooting

The broadcast reference contains:

    R001 -> Hyderabad-Warangal, capacity 40, BUS
    R002 -> Hyderabad-Vijayawada, capacity 50, BUS
    R003 -> Hyderabad-Tirupati, capacity 45, TRAIN

The supplied practice data should use these route IDs.

## Spark Warning Troubleshooting

Local execution may show RandomBlockReplicationPolicy warnings because Spark is running with a single local executor and has no peer executors for replication. These warnings did not prevent the verified application from producing the required reports.

WSL may also show a hostname/loopback warning. This is not, by itself, a Day 28 application failure.

## Output Verification

After a test run:

    ls -lh output/day28-execution-output.txt

Search important sections:

    grep -E "StreamingContext started|ROLLING BOOKING COUNT|SPARK SQL BOOKING REPORT|R001|R002|R003" output/day28-execution-output.txt

Check that the source copies match:

    diff -u src/main/scala/Day28.scala code/Day28.scala

No output from diff means the source copies match.

## Clean Re-run Procedure

Terminal 1:

    cd ~/scala-spark-30-day-practice/Day-28-Real-Time-Booking-Project
    rm -rf output/checkpoint
    nc -lk 9997

Terminal 2:

    cd ~/scala-spark-30-day-practice/Day-28-Real-Time-Booking-Project
    mkdir -p output
    rm -f output/day28-execution-output.txt
    sbt clean compile
    sbt run 2>&1 | tee output/day28-execution-output.txt

Then paste the sample events into Terminal 1 and stop both processes with Ctrl+C after verification.

## Evidence Checklist

- [x] Source compiles successfully.
- [x] StreamingContext starts.
- [x] Socket input receives events.
- [x] BOOK events appear in rolling windows.
- [x] CANCEL events affect cumulative occupancy.
- [x] Broadcast route data enriches the report.
- [x] Spark SQL report is generated.
- [x] Output log is saved.
- [x] Execution screenshots are saved.
- [x] Checkpoint directory is not committed.
- [x] Git working tree is clean after committing evidence.

## Production Considerations

This project is an educational DStreams implementation using a localhost socket. Production workloads should use a durable distributed input source, appropriate state retention and recovery, realistic partitioning, and should avoid collecting large datasets to the driver.

The current use of collect() and show(false) is intentionally limited to the small practice output.