# Day 13 – Spark SQL Basics

## Objective

Practice Spark SQL fundamentals using a customer dataset.

Topics covered:

- Create a DataFrame from CSV
- Inspect the schema
- Select columns
- Filter records
- Create columns using `withColumn`
- Create a temporary SQL view
- Execute SQL queries
- Generate customer analytics
- Understand Spark SQL and Catalyst optimization

## Environment

- Apache Spark: 4.2.0
- Scala: 2.13.18
- SBT: 1.10.11
- Java: OpenJDK 17.0.20
- Master: `local[4]`
- OS: Ubuntu 26.04 / WSL2

## Input Dataset

```text
input/customers.csv
