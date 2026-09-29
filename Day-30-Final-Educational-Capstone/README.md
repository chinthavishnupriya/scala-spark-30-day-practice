# Day 30 — Final Capstone and Interview Practice

## Objective
Build the final capstone required by the 30-day practice set. The selected domain is e-commerce, continuing Day 29 so the project can demonstrate the requested Spark concepts in one domain.

The Day 30 specification requires a domain project with batch and streaming components where appropriate, broadcast, accumulator, cache/persist, partition tuning, Spark SQL, joins, aggregations, windows, at least one UDF, explanations of DAG/lineage/stages/executors/YARN, and 20 interview questions. fileciteturn431file0L194-L207

## Architecture
    BATCH
    orders.csv -> RAW -> CLEAN -> REPARTITION
                                  |
                         +--------+--------+
                         |                 |
                    broadcast(products) customers
                         |                 |
                         +--------+--------+
                                  v
                              ENRICHED
                                  |
                    +-------------+-------------+
                    |             |             |
                  Pair RDD    Spark SQL       Window
                    |             |             |
                 revenue       customer       ranking

    STREAMING
    stream-events.txt -> socketTextStream :9998 -> (customer_id,1)
                       -> updateStateByKey -> stateful customer counts

## Technology Stack
| Component | Version / Choice |
|---|---|
| Scala | 2.13.18 |
| Apache Spark | 4.2.0 |
| Java | 17 |
| sbt | 1.10.11 |
| Execution | local[4] |
| Streaming | Spark DStreams |

## Batch Component
1. Read orders, products, and customers.
2. Clean dates, numeric fields, payment/status values, and invalid rows.
3. Filter to completed orders.
4. Calculate revenue.
5. Normalize category labels with a targeted UDF.
6. Cache reusable order and product data.
7. Repartition enriched processing by customer ID.
8. Broadcast the small product reference.
9. Join customer reference data.
10. Calculate payment revenue with a Pair RDD.
11. Produce customer revenue with Spark SQL.
12. Rank orders within each customer using a window.
13. Print the formatted physical plan.

## Streaming Component
The streaming path listens on localhost:9998. Incoming eight-field CSV events are converted to customer/count pairs and updateStateByKey maintains cumulative customer event counts.

Local demonstration settings:
- Batch interval: 5 seconds
- Demonstration timeout: 30 seconds
- Checkpoint: output/checkpoint/day30

## Spark Features Demonstrated
- Broadcast: small product reference.
- Accumulator: valid completed batch rows and streaming observations.
- Cache/persist: reusable datasets.
- Partition tuning: repartition by customer ID into four local partitions.
- Spark SQL: customer revenue report.
- Joins: orders with product and customer references.
- Aggregations: Pair RDD reduceByKey and SQL GROUP BY.
- Window: row_number ranking within each customer.
- UDF: custom category normalization.

## DAG, Lineage, Stages, and Shuffle
Spark transformations are lazy and form a DAG. Important shuffle-producing or shuffle-prone operations include repartition, Pair RDD reduceByKey, SQL aggregation, window partitioning/sorting, and joins when a shuffle strategy is selected.

Lineage is the transformation history Spark can use to recompute lost partitions. Stages are separated by shuffle dependencies. Executors run tasks and hold cached/persisted data.

The formatted physical plan printed by explain("formatted") is the evidence to use when identifying actual Exchange, broadcast, aggregation, and sorting operators.

## YARN Deployment
The project runs locally for practice. A YARN deployment would use spark-submit; YARN ResourceManager allocates resources, the driver coordinates the application, and executors run tasks. Application, stage, shuffle, and executor metrics should be monitored.

## Expected Batch Results
Raw orders: 12
Clean completed orders: 11

Payment revenue:
    CARD -> ₹7100.00
    CASH -> ₹800.00
    UPI  -> ₹4600.00

Total completed revenue:
    ₹12500.00

The accumulator should report 11 valid completed rows.

## Streaming Test
Terminal 1:
    nc -lk 9998

Terminal 2:
    sbt "run streaming"

Then in Terminal 1:
    cat input/stream-events.txt | nc localhost 9998

The Spark terminal should print stateful customer event counts.

## Build and Run
    sbt clean compile
    sbt "run batch"
    sbt "run streaming"
    sbt "run all"

Capture batch evidence:
    mkdir -p output
    sbt "run batch" 2>&1 | tee output/day30-batch-output.txt

## Production Considerations
- Use durable storage instead of local CSV files.
- Define explicit schemas.
- Validate malformed records and nulls.
- Use durable streaming checkpoints.
- Monitor state growth.
- Tune partitions using observed volume and skew.
- Avoid collecting large datasets to the driver.
- Broadcast only genuinely small reference data.
- Add structured logging, monitoring, alerts, retries, access controls, and externalized configuration.
- Evaluate Structured Streaming for modern production streaming workloads where appropriate.

## Project Structure
    Day-30-Final-E-Commerce-Capstone/
    ├── .gitignore
    ├── .jvmopts
    ├── build.sbt
    ├── project/build.properties
    ├── input/
    │   ├── orders.csv
    │   ├── products.csv
    │   ├── customers.csv
    │   └── stream-events.txt
    ├── src/main/scala/Day30.scala
    ├── code/Day30.scala
    ├── COMMANDS.md
    ├── INTERVIEW-QUESTIONS.md
    ├── README.md
    ├── screenshots/
    └── troubleshooting/README.md

## Status
- [x] Project structure created.
- [x] Batch component implemented.
- [x] Streaming component implemented.
- [x] Required Spark features included.
- [x] Interview preparation document created.
- [ ] Local compilation and runtime evidence.
