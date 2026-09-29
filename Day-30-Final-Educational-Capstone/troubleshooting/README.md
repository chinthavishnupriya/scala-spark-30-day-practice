# Day 30 Troubleshooting

## 1. Java / JVM Errors

Check:

```bash
java -version
sbt -version
```

The project is configured for Java 17 and includes JVM module-opening options in `.jvmopts`.

If the active Java version is incompatible, switch to Java 17 and run:

```bash
sbt clean compile
```

## 2. `sbt: command not found`

Install/configure sbt, then verify:

```bash
sbt -version
```

Run sbt from the capstone directory.

## 3. Input File Not Found

The application uses relative paths such as `input/assessments.csv`.

Run:

```bash
pwd
ls -lh input/
```

Make sure the current directory is `Day-30-Final-Educational-Capstone`.

## 4. Compilation Failure

Try a clean rebuild:

```bash
rm -rf target project/target
sbt clean compile
```

For Java module errors, verify Java 17 first.

## 5. Port 9998 Connection Refused

Start the Spark streaming application first:

```bash
sbt "run streaming"
```

Then send events from another terminal:

```bash
cat input/attendance-events.txt | nc localhost 9998
```

## 6. Port 9998 Already in Use

Check:

```bash
ss -ltnp | grep 9998
lsof -i :9998
```

Stop the old process and restart streaming.

## 7. No Streaming Events

Follow this order:

1. Start `sbt "run streaming"`.
2. Wait for the streaming component startup message.
3. Send `input/attendance-events.txt`.
4. Wait for the 5-second micro-batch interval.

The application expects five comma-separated fields per event.

Test manually:

```bash
echo "S001,2026-09-01,C01,ATTENDANCE,PRESENT" | nc localhost 9998
```

## 8. Empty Streaming Output

Check:

```bash
cat input/attendance-events.txt
```

Blank lines are ignored, and malformed lines are filtered out.

## 9. Stale Checkpoint

For a fresh local streaming experiment:

```bash
rm -rf output/checkpoint/day30
```

Restart the streaming application.

## 10. Physical Plan Truncation Warning

A warning that the physical-plan string was truncated is an output-size warning. It does not by itself mean the Spark job failed.

Check that the expected results are printed and sbt finishes successfully.

## 11. Slow Batch

Inspect:
- partitions and repartitioning
- shuffle stages
- broadcast joins
- cached/persisted datasets
- physical plan
- data skew
- local CPU and memory

The project intentionally demonstrates shuffle and persistence, so these operations can add runtime.

## 12. Out of Memory

For a local educational run:
- Close unnecessary applications.
- Run batch and streaming separately.
- Avoid unnecessarily large input files.
- Verify Java 17.
- Remove stale build/checkpoint data if needed.

Do not delete the input datasets.

## 13. Missing or Unexpected Output

Capture the complete batch output:

```bash
sbt "run batch" 2>&1 | tee batch-output.log
```

The batch output should contain:
- record validation summary
- Pair RDD totals
- Spark SQL results
- window ranking
- physical plan

For streaming:

```bash
sbt "run streaming" 2>&1 | tee streaming-output.log
```

Then send the attendance input from another terminal.

## 14. Unknown Run Mode

Valid modes are:

```text
batch
streaming
all
```

Examples:

```bash
sbt "run batch"
sbt "run streaming"
sbt "run all"
```

## 15. Quick Recovery

```bash
rm -rf target project/target output
sbt clean compile
sbt "run batch"
```

For streaming, restart the application and resend the events.

## 16. Evidence Collection

Capture:
- batch summary
- Pair RDD aggregation
- Spark SQL output
- window ranking
- physical plan
- streaming counts

The `screenshots/` directory is intended for these execution screenshots.
