# Day 30 Education Analytics — Commands

Run these commands from the `Day-30-Final-Educational-Capstone` directory.

## 1. Verify Environment

```bash
java -version
scala -version
sbt -version
spark-submit --version
```

Expected baseline: Java 17, Scala 2.13.x, sbt 1.10.x, Spark 4.2.x.

## 2. Compile

```bash
sbt clean compile
```

Or without cleaning:

```bash
sbt compile
```

## 3. Run Batch

```bash
sbt "run batch"
```

Save console output:

```bash
sbt "run batch" 2>&1 | tee batch-output.log
```

## 4. Run Streaming

Terminal 1:

```bash
sbt "run streaming"
```

Terminal 2:

```bash
cat input/attendance-events.txt | nc localhost 9998
```

Manual test event:

```bash
echo "S001,2026-09-01,C01,ATTENDANCE,PRESENT" | nc localhost 9998
```

## 5. Run Both Pipelines

```bash
sbt "run all"
```

The streaming phase still requires events on TCP port 9998.

## 6. Inspect Input

```bash
ls -lh input/
head -5 input/assessments.csv
head -5 input/courses.csv
head -5 input/student-profiles.csv
cat input/attendance-events.txt
```

## 7. Check Port 9998

```bash
ss -ltnp | grep 9998
lsof -i :9998
nc -vz localhost 9998
```

## 8. Check Spark/Java Processes

```bash
ps aux | grep -E "java|spark|sbt" | grep -v grep
```

## 9. Clean Runtime State

Build files:

```bash
rm -rf target project/target
```

Streaming checkpoint:

```bash
rm -rf output/checkpoint/day30
```

Full local cleanup:

```bash
rm -rf target project/target output
```

## 10. Inspect Important Spark Evidence

After `sbt "run batch"`, look for:

```text
Raw assessment records
Valid assessment records
Accumulator value
Enriched records
Enriched partitions
BroadcastHashJoin
BroadcastExchange
Exchange
ShuffleQueryStage
InMemoryRelation
InMemoryTableScan
```

Search a saved log:

```bash
grep -E "Raw assessment|Valid assessment|Accumulator|Enriched|Broadcast|Shuffle|InMemory|success" batch-output.log
```

## 11. Recommended Order

Batch:

```bash
sbt clean compile
sbt "run batch"
```

Streaming:

```bash
sbt "run streaming"
```

Then, from another terminal:

```bash
cat input/attendance-events.txt | nc localhost 9998
```

Full capstone:

```bash
sbt clean compile
sbt "run all"
```

## 12. Git Check

```bash
git status
```

The `output/` directory is intentionally ignored because Spark generates checkpoint/state data there.
