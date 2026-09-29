# Day 30 — Education Analytics Capstone

## 1. Project Overview
An Education Analytics platform built with Scala and Apache Spark. It combines batch analytics for student assessments with real-time attendance-event processing.

**Stack:** Scala 2.13.18, Apache Spark 4.2.0, Java 17, sbt 1.10.11, local[4].

## 2. Objectives
- Clean and validate assessment records.
- Calculate score percentages and classify performance with a UDF.
- Enrich assessments with course and student reference data.
- Demonstrate Pair RDD aggregation, Spark SQL and window functions.
- Demonstrate broadcast, accumulator, cache/persist and partition tuning.
- Inspect DAG, lineage, shuffle boundaries and the physical plan.
- Process attendance events with Spark Streaming.

## 3. Pipeline Architecture

### End-to-End Architecture

The project has two processing paths: a **batch analytics pipeline** for assessment data and a **streaming pipeline** for real-time attendance events.

```text
                    EDUCATION ANALYTICS PLATFORM
                               |
              +----------------+----------------+
              |                                 |
          BATCH PIPELINE                  STREAMING PIPELINE
              |                                 |
    +---------+---------+                    TCP :9998
    |         |         |                       |
Assessments Courses Students                    |
    |         |         |                       v
    +---------+---------+                  Spark DStream
              |                                 |
              v                                 v
       Data Validation                    Parse Events
              |                                 |
              v                                 v
       Score Percentage                  (student_id, 1)
              |                                 |
              v                                 v
       Performance UDF                  updateStateByKey
              |                                 |
              v                                 v
         Cache / Persist                  Running Counts
              |
              v
   Repartition by student_id
              |
       +------+------+
       |             |
       v             v
 Broadcast       Student
 Course Join       Join
       |             |
       +------+------+
              |
              v
       Enriched Dataset
              |
    +---------+----------+-------------+
    |         |          |             |
    v         v          v             v
 Pair RDD  Spark SQL  Window       Physical Plan
Aggregate             Ranking       Analysis
```

### Batch Flow

```text
CSV Files
   |
   v
Spark DataFrames
   |
   v
Validation + Cleaning
   |
   v
Score Percentage
   |
   v
Performance UDF
   |
   v
Cache
   |
   v
Repartition(4, student_id)
   |
   +----------------------+
   |                      |
   v                      v
Broadcast Course      Student Join
   |                      |
   +----------+-----------+
              |
              v
       Enriched Dataset
              |
      +-------+-------+--------+
      |       |       |        |
      v       v       v        v
   Pair RDD SQL   Window   Physical
 Aggregation      Ranking    Plan
```

### Streaming Flow

```text
attendance-events.txt
        |
        v
   TCP / nc :9998
        |
        v
   Spark DStream
        |
        v
   Parse + Filter
        |
        v
   (student_id, 1)
        |
        v
   updateStateByKey
        |
        v
 Running Attendance Counts
```

N