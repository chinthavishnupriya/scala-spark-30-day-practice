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
- Production-style documentation and architecture

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
- [ ] Compilation succeeds.
- [ ] Raw counts appear.
- [ ] Clean order count appears.
- [ ] Pair RDD payment revenue appears.
- [ ] Customer DataFrame summary appears.
- [ ] Window ranking appears.
- [ ] Category summary appears.
- [ ] Physical plan appears.
- [ ] Runtime output is saved.
- [ ] Execution screenshots are captured.
- [ ] Git working tree is clean after committing evidence.

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