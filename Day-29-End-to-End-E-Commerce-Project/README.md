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

## Technology Stack

| Component | Version / Choice |
|---|---|
| Scala | 2.13.18 |
| Apache Spark | 4.2.0 |
| Java | 17 |
| sbt | 1.10.11 |
| Execution mode | local[4] |
| Data format | CSV |
| API style | Spark Core + Spark SQL/DataFrame |

## Input Data

The project uses three small reference/input datasets:

- `input/orders.csv` — transactional order events, including one cancelled order.
- `input/products.csv` — product ID, product name, and category.
- `input/customers.csv` — customer ID, customer name, and region.

The sample is intentionally small so the complete DAG, shuffle behavior, and output can be inspected locally.

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

## End-to-End Data Flow

1. **Read** the three CSV datasets.
2. **Clean** order dates, numeric columns, payment/status values, and invalid rows.
3. **Filter** to completed orders with positive quantity and price.
4. **Calculate** row-level revenue.
5. **Normalize** legacy product categories with the targeted UDF.
6. **Repartition** enriched processing by customer ID.
7. **Join** orders with product and customer reference data.
8. **Persist** reused DataFrames.
9. **Aggregate** with Pair RDD and DataFrame operations.
10. **Rank** customer orders with a window function.
11. **Inspect** the optimized physical plan.

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

## Transformation vs Action Classification

### Transformations

- CSV DataFrame loading creates the input logical plans.
- `withColumn`, `filter`, and `dropDuplicates` build lazy transformations.
- `repartition(4, customer_id)` creates a repartitioning boundary.
- `join`, `groupBy`, and `agg` remain lazy until an action requires execution.
- `rdd.map`, `reduceByKey`, and `sortByKey` build the Pair RDD pipeline.
- The window expression builds an analytical transformation.

### Actions

- `count()` materializes the raw, clean, and enriched datasets.
- `collect()` retrieves only the three reduced payment-method rows.
- `show(false)` materializes the customer, window, and category reports.
- `explain("formatted")` prints the physical plan for inspection.

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

## DAG, Stages, and Shuffle Boundaries

Spark constructs a DAG from the lazy transformations and separates execution into stages around shuffle boundaries.

Important boundaries in this project include:

- **Pair RDD:** `reduceByKey` requires records with the same payment method to be brought together.
- **Explicit repartition:** `repartition(4, customer_id)` reshuffles data using customer ID.
- **Aggregations:** customer/category `groupBy` and aggregation can introduce Exchange operators.
- **Window:** partitioning by customer ID can require data movement and sorting.
- **Joins:** join strategy determines whether Spark performs a shuffle join or uses a broadcast exchange.
- **Adaptive execution:** the successful physical plan showed AQE shuffle reads/coalescing in the local run.

The formatted physical plan is the authoritative evidence for the actual strategy chosen by this Spark run.

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

## Performance Notes

This sample is deliberately small, so execution time is not a production benchmark. The purpose is to make Spark execution concepts observable.

For a larger workload:

- Prefer built-in Spark SQL expressions over UDFs where possible.
- Select partition counts from measured data volume and cluster resources.
- Watch for skewed customer IDs.
- Persist only datasets reused enough to justify memory/storage cost.
- Avoid driver-side `collect()` except for genuinely small results.
- Inspect Spark UI and formatted physical plans before changing join or partition settings.
- Compare shuffle read/write and stage duration before and after an optimization.

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

## Result Interpretation

The completed-order total is ₹12,500.00. The cancelled O004 transaction is intentionally absent from all completed-order revenue calculations.

The category normalization changes:

    Legacy-Stationery -> Stationery
    Legacy-Electronics -> Electronics

The successful run therefore reports:

    Electronics -> 6 orders, ₹7400.00
    Stationery  -> 5 orders, ₹5100.00

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

## Build and Run Commands

### Standard compile

    sbt clean compile

### Run the application

    sbt run

### Capture a complete runtime log

    mkdir -p output
    sbt run 2>&1 | tee output/day29-execution-output.txt

### Inspect the runtime log

    head -20 output/day29-execution-output.txt
    grep -E "Pair RDD|Customer Revenue Summary|Window:|Category Summary|Physical Plan|Pipeline Summary" output/day29-execution-output.txt

### Verify source copies

    diff -u src/main/scala/Day29.scala code/Day29.scala

### Check repository state

    git status --short

## Run

    cd ~/scala-spark-30-day-practice/Day-29-End-to-End-E-Commerce-Project
    sbt clean compile
    sbt run

Evidence capture:

    mkdir -p output
    sbt run 2>&1 | tee output/day29-execution-output.txt

## Evidence Files

The committed evidence consists of six screenshots:

1. `01-compilation-success.png` — compilation evidence
2. `02-runtime-started.png` — Spark startup and application header
3. `03-pair-rdd-revenue.png` — Pair RDD payment aggregation
4. `04-dataframe-summary.png` — customer DataFrame aggregation
5. `05-window-and-category.png` — window ranking and category summary
6. `06-physical-plan-complete.png` — physical plan and completion evidence

The generated `output/day29-execution-output.txt` is intentionally ignored by Git because it is reproducible runtime output.

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