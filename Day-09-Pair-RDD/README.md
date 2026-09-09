# Day 09 – Pair RDD

## Objective

Understand Pair RDDs and use key-value operations for aggregation and analysis.

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
