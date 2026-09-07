# Day 04 - RDD Creation and Operations

## Objective

Practice creating Spark RDDs from Scala collections and text files, and perform basic RDD transformations and actions.

Topics covered:

- RDD creation from Scala collections
- RDD creation from text files
- `map`
- `filter`
- `flatMap`
- `reduce`
- RDD partition inspection
- Default parallelism
- Basic sales calculation

## Environment

- Ubuntu / WSL
- Java 17
- Scala 2.13.18
- Apache Spark 4.2.0
- SBT 1.10.11
- Spark master: `local[4]`

## Input Data

### customers.txt

The customer dataset contains customer ID, name, city, and category.

12 customer records were processed.

### sales.txt

The sales dataset contains transaction ID, product, quantity, and price.

6 sales transactions were processed.

## RDD Creation

### RDD from Scala Collection

```scala
val numbers = sc.parallelize(1 to 10, 4)
