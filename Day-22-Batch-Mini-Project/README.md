# Day 22 — Batch Mini Project

## Objective

Build an end-to-end Apache Spark batch pipeline for an e-commerce daily sales scenario.

The pipeline reads raw transactions, cleans invalid records, joins customer and product data, aggregates revenue, writes partitioned Parquet output, verifies the output, and inspects the physical execution plan.

## Technology

- Scala 2.13.18
- Apache Spark 4.2.0
- Spark SQL
- SBT 1.10.11
- Java 17
- Spark local mode: `local[4]`

## Project Structure

```text
Day-22-Batch-Mini-Project/
├── README.md
├── COMMANDS.md
├── .gitignore
├── .jvmopts
├── build.sbt
├── code/Day22.scala
├── input/
│   ├── transactions.csv
│   ├── customers.csv
│   └── products.csv
├── output/day22-execution-output.txt
├── project/build.properties
├── screenshots/
└── troubleshooting/README.md
```

## Pipeline

1. Read 12 raw transactions.
2. Read customer and product reference data.
3. Remove invalid records.
4. Calculate `revenue = quantity × unit_price`.
5. Join transactions with customers using `customer_id`.
6. Join with products using `product_id`.
7. Aggregate daily sales by date, city, and category.
8. Aggregate customer revenue.
9. Write daily sales as partitioned Parquet by `transaction_date`.
10. Read the Parquet output back and verify it.
11. Inspect the physical execution plan.

### Cleaning

Two deliberately invalid records are present:

- T006 — `CANCELLED`
- T011 — negative quantity

Expected result:

```text
Raw transaction count: 12
Invalid records removed: 2
Clean transaction count: 10
```

### Spark Execution

Important transformations:

- `filter`
- `withColumn`
- `join`
- `groupBy`
- `agg`
- `orderBy`

Important actions:

- `count`
- `show`
- Parquet write/read
- `explain`

The physical plan contains `BroadcastHashJoin` / `BroadcastExchange` for the small customer and product reference datasets. It also contains `HashAggregate`, `Exchange`, and `Sort` stages.

## Output

Partitioned Parquet is written to:

```text
output/daily-sales
```

Generated Parquet output is excluded by `.gitignore`; the execution log is retained.

Verified result:

```text
Output record count: 10
DAY 22 COMPLETED SUCCESSFULLY
```

## Evidence

1. `01-compilation-success.png`
2. `02-raw-data-and-cleaning.png`
3. `03-enrichment-and-joins.png`
4. `04-daily-sales-aggregation.png`
5. `05-parquet-output-verification.png`
6. `06-execution-plan.png`
7. `07-complete-success.png`

The complete terminal output is stored in `output/day22-execution-output.txt`.

## Learning Outcomes

This exercise demonstrates an end-to-end Spark batch workflow using DataFrames, filtering, calculated columns, joins, aggregations, partitioned Parquet output, broadcast joins, shuffle boundaries, and physical-plan inspection.
