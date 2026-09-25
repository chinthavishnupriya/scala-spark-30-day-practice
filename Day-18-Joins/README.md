# Day 18 - Spark Joins

## Objective

Practice Spark DataFrame joins using Scala and Apache Spark.

This exercise covers:

- Inner Join
- Left Join
- Right Join
- Full Outer Join
- Table aliases for ambiguous columns
- NULL handling after joins
- Joining multiple DataFrames
- Spark SQL joins
- Shuffle Sort-Merge Join
- Execution-plan analysis

## Environment

- Apache Spark: 4.2.0
- Scala: 2.13.18
- Java: 17
- SBT: 1.10.11
- Master: `local[4]`
- Platform: Ubuntu / WSL2

## Input Data

Three CSV files are used:

### customers.csv

Contains:

- customer_id
- customer_name
- city

### orders.csv

Contains:

- order_id
- customer_id
- product
- amount

### payments.csv

Contains:

- payment_id
- order_id
- payment_method
- payment_status

The sample data intentionally contains unmatched records so that outer joins and NULL handling can be demonstrated.

## Join Operations

### 1. Inner Join

Customers and orders are joined using:

```scala
c.customer_id === o.customer_id
