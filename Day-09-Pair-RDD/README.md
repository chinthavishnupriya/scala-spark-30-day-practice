# Day 09 — Pair RDD

## Objective

Understand Pair RDDs and use key-value operations for aggregation, grouping, transformation, and account-level analysis.

## Practice Requirements

- Create key-value Pair RDDs.
- Use `reduceByKey`, `groupByKey`, and `mapValues`.
- Calculate revenue by product and department.
- Compare `reduceByKey` and `groupByKey`.
- Aggregate bank transactions by account ID.

## Input

Input file:

`input/transactions.txt`

Each record contains:

```text
TransactionID,AccountID,Product,Quantity,Price
```

The sample contains 15 transactions across four accounts and three product categories.

## What is a Pair RDD?

A Pair RDD is an RDD whose elements are key-value pairs such as:

```text
(product, revenue)
(accountId, amount)
```

Key-value RDDs make operations such as grouping, aggregation, and lookup-oriented processing straightforward.

## `reduceByKey`

`reduceByKey` combines values belonging to the same key.

Example:

```text
(Electronics, 30000)
(Electronics, 18000)
(Electronics, 36000)
        ↓
(Electronics, 84000)
```

It can perform local combining before shuffle, which can reduce the amount of data transferred during aggregation.

## `groupByKey`

`groupByKey` groups all values belonging to the same key:

```text
Books → [2500, 2400, 2700, 2100, 1600]
```

It is useful when the individual values themselves are needed, but it can require more memory and shuffle data than an aggregation that combines values directly.

## `mapValues`

`mapValues` transforms the value portion of each key-value pair while preserving the keys and partitioning characteristics.

The project uses it to transform product quantities while keeping the product key unchanged.

## Revenue by Product / Department

The completed aggregation produced:

| Product | Revenue |
|---|---:|
| Books | ₹11300.00 |
| Clothing | ₹19400.00 |
| Electronics | ₹176000.00 |

The revenue calculation is:

```text
Revenue = Quantity × Price
```

## `reduceByKey` vs `groupByKey`

| Feature | `reduceByKey` | `groupByKey` |
|---|---|---|
| Input | Key-value pairs | Key-value pairs |
| Result | One aggregated value per key | Iterable of values per key |
| Local combining | Yes, where applicable | No equivalent pre-aggregation |
| Typical use | Sum/count/maximum | Need individual grouped values |
| Data efficiency | Usually better for aggregation | Can move more data |

For a simple sum, `reduceByKey` is generally the better choice because the values can be combined before the shuffle.

## Account-Level Aggregation

The program creates an `(accountId, amount)` Pair RDD and aggregates transaction amounts by account.

Observed totals:

```text
A100 → ₹100300.00
A101 → ₹34500.00
A102 → ₹60000.00
A103 → ₹11900.00
```

Transaction counts are also calculated:

```text
A100 → 4 transactions
A101 → 4 transactions
A102 → 4 transactions
A103 → 3 transactions
```

## Processing Flow

```text
Transaction records
        ↓
Parse fields
        ↓
Create Pair RDDs
        ↓
reduceByKey / groupByKey / mapValues
        ↓
Product and account aggregation
        ↓
Results
```

## Shuffle and Performance

Both `reduceByKey` and `groupByKey` can require a shuffle because records with the same key may be distributed across partitions. The difference is how values are handled during aggregation.

For large datasets, minimizing unnecessary data movement and using an aggregation that combines values early can significantly improve performance.

## Project Structure

```text
Day-09-Pair-RDD/
├── .gitignore
├── .jvmopts
├── README.md
├── build.sbt
├── code/
│   └── Day09.scala
├── input/
│   └── transactions.txt
├── output/
│   └── result.txt
├── screenshots/
│   ├── 01-pair-rdd-revenue.png
│   ├── 02-groupbykey-comparison.png
│   └── 03-account-aggregation-success.png
└── src/
    └── main/
        └── scala/
            └── Day09.scala
```

## Environment

- Scala: 2.13.18
- Apache Spark: 4.2.0
- Java: 17
- SBT: 1.10.11
- Master: `local[4]`

## How to Run

```bash
sbt compile
sbt run
```

Save output:

```bash
sbt run > output/result.txt 2>&1
```

## Learning Outcome

Day 9 introduces the key-value programming model used extensively in Spark. It demonstrates how Pair RDD operations support aggregation and why choosing `reduceByKey` instead of unnecessary `groupByKey` can improve scalability.