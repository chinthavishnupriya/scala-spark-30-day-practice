# Day 30 — 20 Interview Questions and Answers

1. What is the architecture of this capstone?
   It has a batch analytics path and a stateful streaming path.

2. Why use a Pair RDD?
   Payment-method revenue is a compact key-value processing example using reduceByKey.

3. What is broadcast?
   Broadcast distributes a small read-only reference dataset to executors.

4. What is an accumulator?
   An accumulator allows executor-side updates whose aggregate value is read by the driver.

5. Why use cache and persist?
   They keep reusable computed data so repeated actions can avoid recomputation.

6. What is partition tuning here?
   The enriched dataset is repartitioned into four partitions by customer ID.

7. What is a shuffle?
   A shuffle redistributes records across partitions for operations such as reduceByKey, aggregation, repartition, windows, and some joins.

8. What is a DAG?
   A DAG is Spark's directed acyclic graph of transformations.

9. What is lineage?
   Lineage is the transformation history used to reconstruct lost partitions.

10. What is a stage?
    A stage is a group of tasks separated by shuffle dependencies.

11. What does an executor do?
    Executors execute tasks, hold cached data, and report results/metrics to the driver.

12. What is YARN?
    YARN is a cluster resource manager that allocates resources and manages containers.

13. Why use Spark SQL?
    Spark SQL provides declarative queries and Catalyst optimization.

14. Why use a window function?
    It performs calculations within a partition while retaining individual rows.

15. Why use a UDF?
    The UDF handles the custom category normalization rule; built-ins are preferred otherwise.

16. What happens in reduceByKey?
    Spark combines values by key and redistributes records so equal keys can be aggregated.

17. Why is collect used?
    Only the small, already-aggregated payment-method result is collected.

18. How does streaming differ from batch?
    Batch processes stored data to completion; streaming processes incoming events in micro-batches and maintains state.

19. What should be monitored in production?
    Input rate, latency, shuffle, task duration, skew, executor memory, state size, checkpoints, failures, and resource utilization.

20. How would you improve the project for production?
    Use durable storage/checkpoints, explicit schemas, monitoring, externalized configuration, data-quality validation, alerting, and an appropriate production streaming architecture.
