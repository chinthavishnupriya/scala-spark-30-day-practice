# Day 30 — Education Analytics Interview Questions

1. What is the purpose of the project?
   It analyzes student assessments in batch and attendance events in real time.

2. Why use education data?
   It provides student, course, assessment and attendance use cases for Spark analytics.

3. Where is a Pair RDD used?
   Course IDs are paired with marks and reduced with reduceByKey.

4. Where is broadcast used?
   The small course reference DataFrame is broadcast during enrichment.

5. Why use an accumulator?
   It counts valid assessment records and observed streaming events.

6. Why not update a normal driver variable from executors?
   Executor tasks run independently, so ordinary driver variables are not reliable distributed counters.

7. Why cache the cleaned assessments?
   The cleaned dataset is reused by several downstream operations.

8. Why use MEMORY_AND_DISK?
   It allows persisted partitions to spill to disk if memory is insufficient.

9. Why repartition by student_id?
   It explicitly tunes partition count and distribution for keyed processing.

10. What operations can cause a shuffle?
    Repartition, reduceByKey, grouping, windows and non-broadcast joins can cause shuffles.

11. What is a DAG?
    A Directed Acyclic Graph representing Spark transformations and dependencies.

12. What is lineage?
    The record of how a dataset was derived, allowing lost partitions to be recomputed.

13. What is a stage?
    A set of tasks separated from other stages by shuffle boundaries.

14. Why use Spark SQL?
    It provides declarative queries for student performance summaries.

15. Why use a window function?
    row_number ranks students inside each course while retaining individual assessment rows.

16. What does the UDF do?
    It maps score percentages to performance bands.

17. Why prefer built-in functions over UDFs when possible?
    Built-in functions are generally easier for Spark to optimize.

18. How does the streaming component work?
    A DStream reads socket events in five-second micro-batches and maintains running student counts.

19. How could the application run on YARN?
    Submit the Spark application to a YARN cluster with suitable resources and deploy configuration.

20. How would you productionize it?
    Use durable streaming storage, distributed checkpoints, schema validation, logging, monitoring and cluster resource tuning.
