# Day 30 — Education Analytics Capstone

## Domain

Education Analytics.

The Day 30 requirements call for a selected domain, batch and streaming components where appropriate, broadcast, accumulator, cache/persist, partition tuning, Spark SQL, joins, aggregations, windows, at least one UDF, and Spark execution explanations.

## Project Goal

Process student assessments in batch and attendance events in real time using Scala and Apache Spark.

## Data

- assessments.csv — marks, assessment type, attendance and validity
- courses.csv — small course reference data
- student-profiles.csv — student master data
- attendance-events.txt — sample real-time attendance events

## Batch Flow

1. Read assessment, course and student-profile CSV files.
2. Parse dates and numeric columns.
3. Remove invalid records.
4. Calculate score percentage.
5. Apply the performance-band UDF.
6. Cache cleaned assessments.
7. Count valid records with an accumulator.
8. Repartition by student_id.
9. Broadcast the course reference.
10. Join course and student information.
11. Run Pair RDD aggregation.
12. Run Spark SQL.
13. Run window ranking.
14. Inspect the physical plan.

## Streaming Flow

Event format:

student_id,course_id,timestamp,event_type,value

A DStream listens on localhost:9998, creates student-keyed events and maintains running counts using updateStateByKey with a five-second micro-batch interval.

## Spark Concepts

Transformations: withColumn, filter, repartition, join, map and reduceByKey.

Actions: count, collect, show and foreach.

Potential shuffle boundaries: repartition, reduceByKey, grouping, window operations and non-broadcast joins.

Spark builds a DAG from lineage and divides execution into stages around shuffle boundaries. The driver coordinates the application; executors run tasks and store persisted data; YARN can manage a production cluster.

## Initial Dataset

There are 12 assessment records. One record is INVALID, so the clean batch contains 11 valid records.

## Run

    sbt clean compile
    sbt "run batch"

Streaming:

    sbt "run streaming"

Send sample events:

    cat input/attendance-events.txt | nc localhost 9998

## Status

- [x] Education domain
- [x] Project structure
- [x] Batch data
- [x] Streaming data
- [x] Batch implementation
- [x] Streaming implementation
- [ ] Local compilation and runtime verification
- [ ] Screenshots
- [ ] Architecture diagram
- [ ] 20 interview questions
- [ ] Final production documentation
