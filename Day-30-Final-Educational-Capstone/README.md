# Day 30 — Education Analytics Capstone

## 1. Project Overview

The Education Analytics Capstone is the final project of the Scala + Apache Spark 30-Day Daily Practice Problem Set.

This project models a practical education analytics platform that processes student assessment data in batch mode and attendance events in streaming mode.

The batch pipeline reads assessment, course and student-profile data, validates records, calculates percentage scores, classifies performance, joins reference data, and produces analytical results using Spark DataFrames, Pair RDDs, Spark SQL and window functions.

The streaming pipeline receives attendance events through a TCP socket and maintains running attendance-event counts for students using Spark Streaming stateful processing.

### Technology Stack

| Technology | Version / Configuration |
|---|---|
| Scala | 2.13.18 |
| Apache Spark | 4.2.0 |
| Java | 17 |
| sbt | 1.10.11 |
| Execution | local[4] |
| Batch API | DataFrame / Spark SQL / Pair RDD |
| Streaming API | Spark Streaming DStreams |
| Input | CSV + TCP socket |

### Main Concepts

- DataFrame transformations and actions
- Data validation and cleaning
- User-defined functions
- Pair RDD transformations
- Spark SQL
- Joins and aggregations
- Window functions
- Broadcast joins
- Accumulators
- Cache and persist
- Repartitioning
- Shuffle boundaries
- Physical-plan inspection
- Stateful Spark Streaming
- DAG, lineage, stages and executors

---

## 2. Objectives

The main objective is to combine the important Spark concepts from the 30-day practice set into one end-to-end application.

### Functional Objectives

1. Read education-related datasets from CSV files.
2. Validate assessment records and remove invalid data.
3. Calculate percentage scores.
4. Classify performance using a UDF.
5. Enrich assessments with course and student information.
6. Calculate course-level totals using Pair RDD operations.
7. Use Spark SQL for student-level performance analysis.
8. Use window functions to rank students within each course.
9. Process attendance events as a streaming workload.
10. Maintain running event counts for each student.

### Spark Optimization Objectives

The project demonstrates:

- Broadcast joins for small reference datasets.
- Accumulators for execution metrics.
- Cache and persist for reusable datasets.
- Repartitioning using student_id.
- Shuffle boundaries caused by data redistribution.
- Physical-plan inspection using formatted explain output.
- DAG, lineage, stages and executor concepts.

---

## 3. Pipeline Architecture

The project contains two processing paths: a batch analytics pipeline and a real-time streaming pipeline.

### 3.1 End-to-End Architecture

    EDUCATION ANALYTICS PLATFORM
                   |
          +--------+--------+
          |                 |
       BATCH            STREAMING
          |                 |
   CSV input          TCP :9998
          |                 |
   Data validation     Spark DStream
          |                 |
   Score percentage    Parse events
          |                 |
   Performance UDF     (student_id, 1)
          |                 |
   Cache / Persist      updateStateByKey
          |                 |
   Repartition         Running counts
          |
   +------+------+
   |             |
   v             v
Broadcast      Student
Course Join     Join
   |             |
   +------+------+
          |
          v
   Enriched Dataset
          |
   +------+------+------+
   |      |      |      |
 Pair RDD SQL  Window Physical
 Aggregate     Ranking  Plan

### 3.2 Batch Flow

    CSV files
       |
       v
    DataFrames
       |
       v
    Validation and cleaning
       |
       v
    Score percentage
       |
       v
    Performance UDF
       |
       v
    Cache
       |
       v
    Repartition to 4 partitions by student_id
       |
       +----------------------+
       |                      |
       v                      v
    Broadcast course       Student join
       |                      |
       +----------+-----------+
                  |
                  v
           Enriched dataset
                  |
          +-------+-------+--------+
          |       |       |        |
          v       v       v        v
       Pair RDD  SQL   Window   Physical
       aggregate      ranking    plan

### 3.3 Streaming Flow

    attendance-events.txt
             |
             v
        TCP / nc :9998
             |
             v
        Spark DStream
             |
             v
        Parse and filter
             |
             v
        (student_id, 1)
             |
             v
        updateStateByKey
             |
             v
        Running attendance counts

The batch and streaming paths can be executed independently using batch or streaming mode. The all mode executes both paths.

---

## 4. Dataset and Input Design

The batch workload uses three CSV datasets.

### 4.1 Assessment Dataset

The assessment dataset contains student assessment records with fields including:

- student_id
- course_id
- assessment_date
- assessment_type
- marks
- max_marks
- attendance_pct
- status

The expanded test dataset contains 2,400 assessment records. After validation, 2,365 records are processed as valid records.

The dataset contains multiple assessment types:

- MID1
- MID2
- LAB1
- LAB2
- QUIZ1
- QUIZ2
- FINAL
- PROJECT

Invalid records are intentionally included so that the validation stage can be demonstrated.

### 4.2 Course Reference Dataset

The course dataset provides reference information such as course ID, course name and department.

Eight courses are used in the workload.

### 4.3 Student Profile Dataset

The student profile dataset provides student-level information such as student ID, student name, semester, section and department.

The course and student datasets are used to enrich assessment records through joins.

### 4.4 Streaming Dataset

The streaming workload uses attendance-event records sent through TCP port 9998.

Each event is transformed into:

    (student_id, 1)

The value 1 represents one observed attendance event.

---

## 5. Batch Processing

The batch pipeline starts by reading the assessment, course and student-profile CSV files into Spark DataFrames.

### Step 1 — Read Data

Spark reads CSV files with headers and schema inference.

### Step 2 — Data Cleaning

The application:

- Converts assessment_date to a date.
- Converts marks and max_marks to numeric values.
- Normalizes assessment_type and status.
- Removes missing student IDs.
- Removes missing course IDs.
- Removes invalid dates.
- Removes records with invalid maximum marks.
- Removes marks outside the valid range.
- Keeps only records with status VALID.

### Step 3 — Score Percentage

The percentage is calculated as:

    score_pct = marks / max_marks × 100

The result is rounded to two decimal places.

### Step 4 — Performance UDF

The performanceBand UDF assigns a category:

| Score | Performance Band |
|---:|---|
| >= 85 | Excellent |
| >= 70 | Good |
| >= 50 | Average |
| < 50 | Needs Improvement |

This demonstrates how custom business logic can be applied to DataFrame columns.

---

## 6. Accumulator Demonstration

A LongAccumulator named valid-assessment-records counts valid assessment records processed by the application.

The runtime result is:

    Raw assessment records: 2400
    Valid assessment records: 2365
    Accumulator value: 2365

The accumulator is useful for counters and diagnostic metrics that executors can update while the driver reads the final value.

The accumulator does not transform the dataset. It provides an execution-side metric.

A second accumulator is used by the streaming component to record observed streaming output.

---

## 7. Cache and Persist

The cleaned assessment DataFrame is cached because it is reused by multiple downstream operations.

The enriched dataset is persisted using MEMORY_AND_DISK.

The enriched data is reused for:

- Counting records
- Pair RDD aggregation
- Spark SQL
- Window ranking
- Physical-plan analysis

Caching and persistence avoid recomputing the same lineage repeatedly when Spark can reuse stored partitions.

The physical plan contains InMemoryRelation and InMemoryTableScan, providing evidence that persisted data is being reused.

---

## 8. Partition Tuning

The cleaned assessment data is explicitly repartitioned using student_id into four partitions.

The runtime confirms:

    Enriched partitions: 4

The physical plan contains a hash partitioning step using student_id and four partitions.

Repartitioning is a shuffle operation because records can move between partitions.

This demonstrates that partition count and partitioning keys are part of Spark performance tuning.

Too few partitions can limit parallelism, while excessive partitions can add scheduling and shuffle overhead. The project uses four local partitions because the application runs with local[4].

---

## 9. Broadcast Join

The course reference dataset is small compared with the assessment dataset, so the application uses a broadcast join for the course reference.

The physical plan shows the broadcast path:

    BroadcastExchange
           |
    BroadcastQueryStage
           |
    BroadcastHashJoin

A broadcast join can avoid a large shuffle of the main assessment dataset when the reference dataset is small enough to distribute to executors.

The project therefore demonstrates both the programming API and the physical execution representation of broadcast.

---

## 10. Student Join and Enriched Dataset

After the course join, the assessment data is joined with student reference data.

The resulting enriched dataset contains assessment information together with:

- Course name
- Student name
- Semester
- Section
- Department
- Score percentage
- Performance band

The runtime confirms:

    Enriched records: 2365
    Enriched partitions: 4

This enriched dataset is used by the SQL, window, aggregation and physical-plan demonstrations.

---

## 11. Pair RDD Aggregation

The project converts assessment data into a Pair RDD containing:

    (course_id, marks)

It then uses map, reduceByKey, sortByKey and collect.

The resulting course totals are:

| Course | Total Marks |
|---|---:|
| C01 | 10766.00 |
| C02 | 10492.00 |
| C03 | 10495.00 |
| C04 | 10548.00 |
| C05 | 10525.00 |
| C06 | 10557.00 |
| C07 | 10299.00 |
| C08 | 10579.00 |

### Transformations and Actions

map, reduceByKey and sortByKey participate in the transformation pipeline.

collect is an action that brings the final result to the driver for display.

### Shuffle

reduceByKey and sortByKey can require records to move between partitions. They therefore provide practical examples of Spark shuffle behavior.

---

## 12. Spark SQL Analysis

The enriched dataset is registered as a temporary SQL view named education_assessments.

The SQL analysis groups records by:

- student_id
- student_name
- semester
- section

It calculates:

- Number of assessments
- Average score percentage

The results are ordered by average score in descending order.

This demonstrates that Spark SQL and the DataFrame API can be used together in the same application.

---

## 13. Window Function — Course Ranking

A window specification partitions records by course_id and orders them by:

1. Score percentage descending.
2. Student ID ascending.

The application uses row_number to generate a ranking within each course.

The displayed result contains:

- course_id
- course_name
- student_id
- student_name
- assessment_type
- score_pct
- performance_band
- course_rank

A normal groupBy collapses records into groups. A window function keeps the individual records while calculating a value relative to each group.

This makes window functions suitable for ranking and comparative analytics.

---

## 14. Physical Plan and Spark Execution Model

The application uses formatted physical-plan output to inspect how Spark executes the workload.

The observed execution plan contains:

- AdaptiveSparkPlan
- HashAggregate
- Exchange
- ShuffleQueryStage
- BroadcastExchange
- BroadcastHashJoin
- InMemoryRelation
- InMemoryTableScan

### DAG

Spark represents the execution as a directed acyclic graph of transformations and actions.

Example:

    Read
      |
    Filter
      |
    withColumn
      |
    Join
      |
    Aggregation
      |
    Action

### Lineage

Each transformation contributes to the lineage of the resulting dataset. Spark can reconstruct required partitions from this lineage when data needs to be recomputed.

### Stages

Shuffle boundaries divide execution into stages. Operations that redistribute data can introduce new stages.

### Driver

The driver creates the SparkSession, builds the transformation logic, requests actions and coordinates execution.

### Executors

Executors process partitions and execute Spark tasks.

This project uses local[4], so four local execution threads are available.

### YARN

The capstone runs locally and therefore does not require YARN. In a cluster environment, YARN can provide resource management for Spark applications.

---

## 15. Streaming Processing

The streaming component uses Spark Streaming DStreams and a TCP socket.

The streaming context uses a 5-second batch interval.

    StreamingContext
          |
          v
    TCP Socket :9998
          |
          v
    DStream
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
    Running Counts

### Stateful Processing

updateStateByKey maintains running state across batches.

Conceptually:

    new state = previous state + current batch values

The successful test produced running counts such as:

    student=S001 attendance-events=2
    student=S002 attendance-events=1
    student=S003 attendance-events=1

The next micro-batch retains the state, demonstrating stateful processing.

### Streaming Test

Start the TCP input first:

    nc -l 9998 < input/attendance-events.txt

Then start Spark in another terminal:

    sbt "run streaming"

The application connects to port 9998 and processes events in 5-second micro-batches.

---

## 16. Runtime Results

The successful batch execution produced:

    Raw assessment records: 2400
    Valid assessment records: 2365
    Accumulator value: 2365
    Enriched records: 2365
    Enriched partitions: 4

The Pair RDD section produced totals for all eight courses.

The Spark SQL section produced student-level performance results.

The window section produced course-level rankings.

The physical plan demonstrated repartitioning, broadcast exchange, broadcast hash joins, persisted data and shuffle-related execution nodes.

The successful execution completed with:

    [success] Total time: 36 s

A warning that the physical-plan string was truncated because it was large does not indicate a failed Spark job. The application completed successfully.

---

## 17. Evidence and Screenshots

The project contains six execution screenshots:

1. 01-batch-data-processing-summary.png — batch record counts and accumulator result.
2. 02-pair-rdd-course-aggregation.png — Pair RDD course aggregation.
3. 03-spark-sql-student-performance.png — Spark SQL student-performance output.
4. 04-window-function-course-ranking.png — window-function course ranking.
5. 05-physical-plan-spark-optimization.png — physical plan and optimization evidence.
6. 06-streaming-attendance-results.png — streaming attendance results.

These screenshots provide execution evidence for the major capstone requirements.

---

## 18. Requirement Coverage

| Requirement | Implementation |
|---|---|
| Education domain | Education Analytics Capstone |
| Batch processing | Assessment analytics |
| Streaming | Attendance-event processing |
| Broadcast | Course reference broadcast join |
| Accumulator | Valid-record and streaming counters |
| Cache / Persist | Clean and enriched datasets |
| Partition tuning | Four partitions by student_id |
| Spark SQL | Student performance query |
| Joins | Course and student enrichment |
| Aggregation | Pair RDD and SQL aggregation |
| Window | Course-level ranking |
| UDF | Performance-band classification |
| Shuffle | Repartition, reduceByKey and sorting |
| Physical plan | Formatted explain output |
| DAG / lineage | Execution model documented |
| Executors | local[4] execution model |
| YARN | Cluster deployment concept documented |
| Interview preparation | 20 questions in INTERVIEW-QUESTIONS.md |
| Evidence | Six execution screenshots |

---

## 19. Project Structure

    Day-30-Final-Educational-Capstone/
    ├── input/
    │   ├── assessments.csv
    │   ├── courses.csv
    │   ├── student-profiles.csv
    │   └── attendance-events.txt
    ├── output/
    │   └── checkpoint/
    │       └── day30/
    ├── screenshots/
    │   ├── 01-batch-data-processing-summary.png
    │   ├── 02-pair-rdd-course-aggregation.png
    │   ├── 03-spark-sql-student-performance.png
    │   ├── 04-window-function-course-ranking.png
    │   ├── 05-physical-plan-spark-optimization.png
    │   └── 06-streaming-attendance-results.png
    ├── src/
    │   └── main/
    │       └── scala/
    │           └── Day30.scala
    ├── INTERVIEW-QUESTIONS.md
    ├── README.md
    ├── build.sbt
    ├── project/
    │   └── build.properties
    └── .jvmopts

---

## 20. How to Run

### Compile

    sbt clean compile

### Run Batch

    sbt "run batch"

### Run Streaming

Terminal 1:

    nc -l 9998 < input/attendance-events.txt

Terminal 2:

    sbt "run streaming"

### Run Both

    sbt "run all"

---

## 21. Troubleshooting

### Port 9998 Connection Refused

If streaming reports Connection refused, the TCP listener is not running.

Start:

    nc -l 9998 < input/attendance-events.txt

Then run:

    sbt "run streaming"

### Scala Callback Type Inference

The state-update callback uses explicit Scala types for Seq[Int] and Option[Int]. This avoids the type-inference problem encountered with Scala 2.13 and the Spark Streaming API.

### Physical Plan Warning

Spark may truncate a very large physical-plan string. This is an output-size warning, not an execution failure. The application can still complete successfully.

---

## 22. Interview Preparation

The capstone includes 20 interview questions in INTERVIEW-QUESTIONS.md.

The questions cover:

- Spark transformations and actions
- Lazy evaluation
- DAG and stages
- Shuffle
- Partitions
- Repartition and coalesce
- Broadcast joins
- Accumulators
- Cache and persist
- Spark SQL
- Window functions
- UDFs
- Pair RDDs
- Spark Streaming
- Stateful processing
- Driver and executors
- YARN
- Performance optimization
- Physical plans
- End-to-end project architecture

The interview file is intended to support the final viva and technical explanation of the project.

---

## 23. Conclusion

This capstone combines the major Spark concepts from the 30-day practice set into one practical education analytics application.

The batch workload demonstrates data cleaning, UDFs, Pair RDD processing, Spark SQL, joins, aggregations, window functions, broadcast joins, accumulators, caching, persistence and partition tuning.

The streaming workload demonstrates DStreams, TCP input, five-second micro-batches and stateful updateStateByKey processing.

The project also connects Spark programming APIs with the execution model by examining lineage, DAGs, stages, shuffle boundaries, broadcast exchanges, persisted data and physical plans.

The final implementation is supported by runtime output, six screenshots, source code, configuration files and 20 interview questions.
