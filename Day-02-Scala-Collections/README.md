# Day 2 — Scala Collections

## Objective

Practice Scala collection operations and apply them to a small daily sales-processing problem.

## Practice Set Requirements

- Process a sales `List` using `map`, `filter`, `flatMap` and `reduce`.
- Use `Vector` for indexed customer records.
- Use `Map` to calculate product quantities and prices.
- Write a `for`-comprehension combining customers and orders.
- Produce a daily sales summary without Spark.

## Concepts Covered

- `map`
- `filter`
- `flatMap`
- `reduce`
- `Vector`
- `Map`
- `for`-comprehension with `yield`
- Immutable collections
- Customer and order processing
- Daily sales aggregation

## Environment

- OS: Ubuntu / WSL
- Java: 17.0.20
- Scala: 2.12.20
- SBT: 1.10.11

## Results

### map
Transforms every element of a collection.

Result: `List(1, 4, 9, 16, 25)`

### filter
Selects elements satisfying a condition.

Result: `List(2, 4)`

### flatMap
Flattens nested collections.

Result: `List(1, 2, 3, 4, 5, 6)`

### reduce
Combines collection elements into one result.

Result: `15`

### Vector — Indexed Customer Records

A `Vector[Customer]` stores customer records and demonstrates indexed access using positions such as `customers(0)` and `customers(2)`.

Customers:

- 101 — Anu
- 102 — Bala
- 103 — Charan
- 104 — Divya

### Map — Product Quantities and Prices

Two immutable maps are used:

- `productQuantities` for product quantities.
- `productPrices` for product prices.

Example:

- Laptop quantity: 4
- Laptop price: ₹60000.0

### For-Comprehension — Customers and Orders

The program combines the customer and order collections using a `for`-comprehension and matches records through `customerId`.

Example results include:

- Anu → Laptop → 1
- Anu → Mouse → 2
- Bala → Keyboard → 1
- Charan → Monitor → 1
- Divya → Mouse → 3

## Daily Sales Processing

The program processes 9 sales records without Spark.

Revenue = quantity × price

| Day | Revenue |
|---|---:|
| Monday | ₹128500.00 |
| Tuesday | ₹87200.00 |
| Wednesday | ₹79500.00 |
| **Total** | **₹295200.00** |

High-value sales with revenue greater than or equal to ₹10,000 are selected using `filter`.

## Performance Observations

- `map` transforms every element.
- `filter` retains elements satisfying a condition.
- `flatMap` maps and flattens collections.
- `reduce` combines elements into one result.
- `Vector` provides efficient indexed access and is suitable for indexed customer records.
- `Map` provides key-based lookup for quantities and prices.
- A `for`-comprehension provides readable combination/filtering of customers and orders.
- `groupBy` is useful for aggregation but requires additional memory.
- Immutable collections do not modify the original collection.
- Multiple collection operations may create intermediate collections.

## Project Structure

```text
Day-02-Scala-Collections/
├── README.md
├── build.sbt
├── code/
│   └── Day02.scala
├── output/
│   └── result.txt
├── screenshots/
├── src/
│   └── main/
│       └── scala/
│           └── Day02.scala
├── project/
│   └── build.properties
└── troubleshooting/
```

## How to Run

Compile:

```bash
sbt compile
```

Run:

```bash
sbt run
```

Save output:

```bash
sbt run > output/result.txt 2>&1
```

## Verification

The implementation has been updated to match all Day 2 requirements from the practice set. Run `sbt compile` and `sbt run` locally to regenerate the output file and verify the corrected customer/order section.

## Learning Outcome

Day 2 demonstrates how Scala collections can transform, filter, flatten, aggregate, index and combine related datasets before moving to distributed processing with Apache Spark.
