# Day 27 — Commands

## Navigate

```bash
cd ~/scala-spark-30-day-practice/Day-27-Real-Time-Banking-Project
```

## Compile

```bash
sbt clean compile
```

## Start TCP server

Terminal 1:

```bash
nc -lk 9998
```

## Run Spark

Terminal 2:

```bash
mkdir -p output
rm -rf output/checkpoint
sbt run 2>&1 | tee output/day27-execution-output.txt
```

## Send sample events

Paste the records below into the active netcat terminal:

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

Expected burst:

```text
BURST ALERT: A001 -> 3 transactions in window
```

## Inspect captured output

```bash
ls -lh output/day27-execution-output.txt
cat output/day27-execution-output.txt
```

Focused verification:

```bash
grep -E "A001 ->|A002 ->|A003 ->|BURST ALERT|T001|T002|T003|T004|T005|T006" output/day27-execution-output.txt
```

The expected totals are:

- A001 → 13700.00
- A002 → 550.00
- A003 → 15000.00

The expected burst alert is A001 with 3 transactions in the window.

## Check port

```bash
ss -ltnp | grep 9998
```

## Fresh run

```bash
rm -rf output/checkpoint
mkdir -p output
sbt clean compile
```

Then start `nc -lk 9998`, run Spark with the `tee` command above, and send the sample events.

## Verify source copies

```bash
diff -u src/main/scala/Day27.scala code/Day27.scala
```

No output means both source copies are identical.

## Spark processing checklist

| Stage | Type | Notes |
|---|---|---|
| Socket input | Input DStream | Receives transaction lines |
| `flatMap` parsing | Transformation | Invalid rows are discarded |
| `persist(MEMORY_ONLY)` | Persistence | Reused by three downstream branches |
| `map` + `reduceByKey` | Transformations | Account-level aggregation; shuffle boundary |
| `reduceByKeyAndWindow` | Transformation | 20-second window, 10-second slide |
| `filter` | Transformation | Keeps counts >= 3 |
| `partitionBy(HashPartitioner(4))` | Transformation | Explicit four-partition layout |
| `join` | Transformation | Branch/risk enrichment |
| `foreachRDD` | Output/action boundary | Prints results for each micro-batch |
| `collect` | Action | Used only for small demonstration output |

## Partitioning and shuffle verification

The application explicitly uses:

```scala
new HashPartitioner(4)
```

for both the small branch/risk Pair RDD and incoming branch-keyed transaction pairs. Keyed aggregation and window aggregation can introduce shuffle work. The README explains the expected boundaries and why `collect()` is limited to this small demonstration.

## Checkpoint

The application sets:

```scala
ssc.checkpoint("output/checkpoint")
```

The checkpoint directory is intentionally ignored by Git because it contains runtime state.

## YARN note

The current exercise is intentionally configured for `local[4]` and localhost TCP input. A YARN deployment should package the application and use a distributed input source rather than a localhost socket.

Example shape:

```bash
spark-submit \
  --master yarn \
  --deploy-mode cluster \
  --class Day27 \
  path/to/day27.jar
```

The exact JAR path, resources, queue, and input source depend on the target cluster.

## Git

From repository root:

```bash
git status --short
git pull origin main
git add Day-27-Real-Time-Banking-Project
git commit -m "Update Day 27 documentation"
git push origin main
```

## Stop

Use `Ctrl+C` in the Spark terminal and netcat terminal.
