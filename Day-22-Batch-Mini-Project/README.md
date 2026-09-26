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


## 1. Overview
Day 22 implements an end-to-end Apache Spark batch pipeline for an e-commerce daily sales scenario. The project combines ingestion, validation, enrichment, analytics, partitioned Parquet output, verification, and physical-plan inspection.

## 2. Problem Statement
The pipeline must read raw transactions, clean invalid records, join customer and product reference data, calculate revenue, produce daily and customer analytics, write partitioned Parquet output, verify the result, and inspect Spark's physical execution plan.

## 3. Detailed Workflow
```text
Raw Transactions
      |
      v
Read CSV
      |
      v
Clean Invalid Records
      |
      v
Calculate Revenue
      |
      +-------------------+
      |                   |
      v                   v
Customer Join        Product Join
      |                   |
      +---------+---------+
                |
                v
       Enriched Transactions
                |
         +------+-------+
         |              |
         v              v
   Daily Sales     Customer Revenue
   Aggregation     Aggregation
         |
         v
 Partitioned Parquet
         |
         v
 Output Verification
         |
         v
 Physical Plan
```

## 4. Input Dataset Details
The transaction input contains 12 records. Two records are intentionally invalid so the cleaning stage can be demonstrated.

| Record | Condition | Cleaning result |
|---|---|---|
| T006 | CANCELLED status | Removed |
| T011 | Negative quantity | Removed |

After cleaning, 10 valid transactions remain.

## 5. Revenue Calculation
Revenue is calculated as:

```text
revenue = quantity × unit_price
```

The value is created with Spark's `withColumn` expression and is then available to both daily and customer-level aggregations.

## 6. Customer and Product Enrichment
The first inner join uses `customer_id` to add customer name, city, and segment. The second inner join uses `product_id` to add product name and category.

Because the reference datasets are small in this practice scenario, the tested physical plan uses broadcast joins.

## 7. Daily Sales Metrics
The daily result groups records by `transaction_date`, `city`, and `category` and calculates:

- transaction count
- units sold
- total revenue

The result is ordered by transaction date and descending revenue.

## 8. Customer Revenue Metrics
The customer-level result groups by customer ID, customer name, city, and segment and calculates transaction count and total revenue.

## 9. Partitioned Parquet Design
The daily result is written with `partitionBy("transaction_date")` to `output/daily-sales`.

Conceptually the output is organized as:

```text
output/daily-sales/
├── transaction_date=2026-09-20/
├── transaction_date=2026-09-21/
├── transaction_date=2026-09-22/
└── transaction_date=2026-09-23/
```

Partitioning by date is useful when downstream queries commonly filter by transaction date because Spark can avoid scanning unrelated partitions.

## 10. Transformations and Actions
### Transformations
- `filter` — removes invalid records.
- `withColumn` — creates revenue.
- `join` — enriches transactions.
- `groupBy` and `agg` — calculate analytics.
- `orderBy` — sorts the daily result.

### Actions
- `count()` — verifies record counts.
- `show()` — displays intermediate results.
- Parquet write — materializes the daily output.
- Parquet read — verifies persisted data.
- `explain("formatted")` — displays the physical plan.

## 11. Shuffle and Performance Analysis
Joins, grouped aggregations, and global ordering can introduce exchange/shuffle stages. The tested physical plan contains `Exchange` operators around these operations.

For larger workloads, performance can be improved by filtering early, selecting only required columns, avoiding unnecessary global sorting, using broadcast joins only for suitably small reference datasets, and monitoring shuffle volume and output-file counts.

## 12. Physical Execution Plan
The tested plan contains operators including:

```text
AdaptiveSparkPlan
BroadcastExchange
BroadcastHashJoin
HashAggregate
Exchange
Sort
```

`BroadcastHashJoin` indicates that Spark broadcast a small join side instead of requiring both sides to be shuffled for a conventional large join.

`HashAggregate` represents the aggregation stages, while `Exchange` indicates repartitioning between execution stages. `Sort` appears because the daily result is ordered.

## 13. Testing Summary
| Check | Result |
|---|---|
| Compilation | Successful |
| Raw transactions | 12 |
| Invalid records removed | 2 |
| Clean transactions | 10 |
| Customer join | Successful |
| Product join | Successful |
| Daily aggregation | Successful |
| Customer aggregation | Successful |
| Parquet output | Successful |
| Verified output records | 10 |
| Physical plan | Generated successfully |

Final application message:

```text
DAY 22 COMPLETED SUCCESSFULLY
```

## 14. Evidence
The complete execution log is stored in `output/day22-execution-output.txt`.

Seven screenshots document compilation, raw-data cleaning, enrichment and joins, daily aggregation, Parquet verification, the physical execution plan, and the final successful run.

## 15. Reproducibility
Run from the project directory:

```bash
sbt clean compile
rm -rf output/daily-sales output/spark-warehouse
sbt run 2>&1 | tee output/day22-execution-output.txt
```

Generated Parquet and Spark warehouse files are excluded from Git, while the execution log and screenshots are retained as evidence.

## 16. Learning Outcomes
This exercise demonstrates Spark DataFrame ingestion, filtering, derived columns, joins, aggregations, sorting, broadcast joins, shuffle boundaries, partitioned Parquet output, output verification, and physical execution-plan analysis.

## 17. Conclusion
Day 22 combines the DataFrame and Spark SQL concepts from the preceding exercises into one complete batch pipeline. The tested implementation successfully transforms raw e-commerce transactions into enriched daily sales analytics and stores the result as partitioned Parquet.