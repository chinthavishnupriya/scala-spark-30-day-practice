# Day 29 — End-to-End E-Commerce Project

## Objective
Build an end-to-end Spark batch pipeline using the required flow: Raw → Clean → Enrich → Aggregate.

## Requirements Covered
- Raw → clean → enrich → aggregate pipeline
- RDD/Pair RDD component
- DataFrame component
- Targeted UDF for legacy category normalization
- Joins and analytical window function
- Partitioning and persistence
- Shuffle, stage, DAG, and optimization discussion
- Production considerations
- Production-style documentation and architecture
- Execution evidence and verification

## Architecture

    orders.csv -> RAW -> CLEAN -> ENRICH -> AGGREGATE
                              |          |
                              |          +--> Customer summary
                              |          +--> Category summary
                              |          +--> Customer window ranking
                              |
                              +--> Pair RDD payment revenue

Reference datasets:
    products.csv  -> product/category enrichment
    customers.csv -> customer/region enrichment

## Pipeline Stages
### 1. Raw
Three CSV DataFrames are loaded: orders, products, and customers.

### 2. Clean
Orders are parsed, numeric/date fields are cast, payment/status values are normalized, invalid records are removed, CANCELLED orders are excluded, and revenue is calculated as quantity × unit price.

### 3. Enrich
Clean orders are joined with product and customer reference data. A small UDF maps legacy product categories to the standard category names.

### 4. Aggregate
The Pair RDD path calculates revenue by payment method with reduceByKey. The DataFrame path calculates customer and category summaries.

### 5. Window
A window partitions by customer_id and ranks each customer's completed orders by revenue descending, with order_date as a tie-breaker.

## Pair RDD Component
Clean orders are converted to (payment_method, revenue) pairs and aggregated with reduceByKey. collect is used only after the data has been reduced to three payment-method rows.

## DataFrame Component
The DataFrame path demonstrates joins, groupBy aggregation, countDistinct, sum, ordering, and window functions.

## UDF Design
The UDF is intentionally narrow. It normalizes Legacy-Electronics to Electronics and Legacy-Stationery to Stationery. Built-in Spark SQL functions are used for parsing, filtering, arithmetic, and aggregation.

## Persistence
cleanOrders, normalizedProducts, and enriched are persisted because multiple downstream operations reuse them. This avoids unnecessary recomputation in the practice workload.

## Partitioning
The enriched DataFrame uses repartition(4, customer_id). Four partitions match the local[4] practice environment. Production partition counts should be selected from workload size, key distribution, executor resources, and observed performance.

## Shuffle and Stage Explanation
Important shuffle-producing or shuffle-prone operations are reduceByKey, repartition, joins, groupBy aggregations, and window partitioning. Spark divides the DAG around shuffle boundaries. The exact physical plan is printed with customerSummary.explain("formatted") and should be used as the evidence during a viva.

## Optimization Choices
- Persistence is used for reused datasets.
- Explicit customer partitioning makes the intended key distribution visible.
- The UDF is limited to a custom legacy-category rule.
- No forced broadcast join is used; Spark can choose a join strategy from statistics and configuration.
- Driver-side collect is restricted to a tiny already-aggregated result.

## Production Considerations

For production deployment, replace local CSV paths with durable object storage or HDFS, define explicit schemas instead of relying on inferSchema, validate malformed records and nulls, and configure partitions from measured data volume and cluster resources. Avoid collecting non-trivial datasets to the driver. Monitor Spark UI stages, shuffle read/write, task skew, executor memory, and cache usage. For genuinely small reference data, a broadcast join can reduce shuffle. Add data-quality checks, structured logging, retry/alerting, access controls, and externalized configuration before deployment.

## Execution Evidence

The successful Day 29 run verified:
- Raw orders: 12
- Products: 5
- Customers: 4
- Clean completed orders: 11
- Enriched rows: 11
- Pair RDD revenue: CARD ₹7100.00, CASH ₹800.00, UPI ₹4600.00
- Customer revenue: C001 ₹4600.00, C002 ₹3800.00, C003 ₹1200.00, C004 ₹2900.00
- Window ranking completed for all 11 completed orders
- Category summary: Electronics ₹7400.00 and Stationery ₹5100.00
- Formatted physical plan printed with Exchange, broadcast, persistence, and partitioning evidence
- Successful runtime completed in approximately 32 seconds in the local WSL practice environment

Execution screenshots are stored under the screenshots/ directory.

## Expected Sample Results
Completed-order revenue by payment method:

    CARD -> ₹7100.00
    CASH -> ₹800.00
    UPI  -> ₹4600.00

Total completed revenue: ₹12500.00

Customer revenue:

    C001 -> ₹4600.00
    C002 -> ₹3800.00
    C003 -> ₹1200.00
    C004 -> ₹2900.00

The cancelled O004 order is excluded from revenue calculations.

## Run

    cd ~/scala-spark-30-day-practice/Day-29-End-to-End-E-Commerce-Project
    sbt clean compile
    sbt run

Evidence capture:

    mkdir -p output
    sbt run 2>&1 | tee output/day29-execution-output.txt

## Verification Checklist
- [x] Compilation succeeds.
- [x] Raw counts appear.
- [x] Clean order count appears.
- [x] Pair RDD payment revenue appears.
- [x] Customer DataFrame summary appears.
- [x] Window ranking appears.
- [x] Category summary appears.
- [x] Physical plan appears.
- [x] Runtime output was generated and inspected locally.
- [x] Execution screenshots are captured.
- [x] Git working tree is clean after committing evidence.

## Project Structure

    Day-29-End-to-End-E-Commerce-Project/
    ├── .gitignore
    ├── .jvmopts
    ├── build.sbt
    ├── project/build.properties
    ├── input/
    │   ├── orders.csv
    │   ├── products.csv
    │   └── customers.csv
    ├── src/main/scala/Day29.scala
    ├── code/Day29.scala
    ├── COMMANDS.md
    ├── README.md
    └── troubleshooting/README.md