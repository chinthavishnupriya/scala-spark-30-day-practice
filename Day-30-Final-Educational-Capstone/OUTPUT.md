# Day 30 — Expected Output

## 1. Batch Summary

Run:

```bash
sbt "run batch"
```

A successful run reports:

```text
DAY 30 - EDUCATION ANALYTICS CAPSTONE
Raw assessment records: 2400
Valid assessment records: 2365
Accumulator value: 2365
Enriched records: 2365
Enriched partitions: 4
```

## 2. Pair RDD Output

The course totals are:

```text
C01 -> 10766.00
C02 -> 10492.00
C03 -> 10495.00
C04 -> 10548.00
C05 -> 10525.00
C06 -> 10557.00
C07 -> 10299.00
C08 -> 10579.00
```

This demonstrates `map`, `reduceByKey`, `sortByKey` and `collect`.

## 3. Spark SQL Output

The SQL section displays:

```text
student_id
student_name
semester
section
assessments
average_score
```

Rows are ordered by average score in descending order. Exact rows are generated from the input datasets.

## 4. Window Function Output

The window section displays:

```text
course_id
course_name
student_id
student_name
assessment_type
score_pct
performance_band
course_rank
```

Students are ranked within each course by score percentage descending, with student ID used as the tie-breaker.

## 5. Physical Plan Output

The formatted physical plan can contain:

```text
AdaptiveSparkPlan
BroadcastExchange
BroadcastHashJoin
Exchange
ShuffleQueryStage
HashAggregate
InMemoryRelation
InMemoryTableScan
```

A plan-truncation warning does not necessarily indicate failure.

## 6. Streaming Output

Start:

```bash
sbt "run streaming"
```

Then:

```bash
cat input/attendance-events.txt | nc localhost 9998
```

Typical output is:

```text
--- Streaming component started on localhost:9998 ---
Batch interval: 5 seconds; state uses updateStateByKey.

STREAM <timestamp>
student=S001 attendance-events=2
student=S002 attendance-events=1
student=S003 attendance-events=1
```

The timestamp and exact counts can vary depending on the events received.

## 7. Streaming Accumulator

At the end:

```text
Streaming accumulator contribution: <value>
```

The value depends on the events observed during that run.

## 8. Successful Completion

A successful sbt execution can finish with:

```text
[success] Total time: <time>
```

Execution time varies by machine.

## 9. Screenshot Evidence

| Screenshot | Output demonstrated |
|---|---|
| 01-batch-data-processing-summary.png | Record counts and accumulator |
| 02-pair-rdd-course-aggregation.png | Course totals |
| 03-spark-sql-student-performance.png | Student SQL analysis |
| 04-window-function-course-ranking.png | Course ranking |
| 05-physical-plan-spark-optimization.png | Spark execution/optimization plan |
| 06-streaming-attendance-results.png | Stateful streaming counts |

## 10. Runtime Output vs Files

The analytical results are printed to the console. Spark Streaming uses `output/checkpoint/day30` for checkpoint/state information.

The `output/` directory is ignored by Git because it is generated runtime data. The `screenshots/` directory stores human-readable execution evidence.
