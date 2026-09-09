# Day 02 — Scala Collections

## Objective

Practice Scala collection operations and apply them to customer, order, product, and daily sales data. The day builds the collection-processing skills needed before distributed Spark processing.

## Practice Requirements

- Process a sales `List` using `map`, `filter`, `flatMap`, and `reduce`.
- Use `Vector` for indexed customer records.
- Use `Map` to calculate product quantities and prices.
- Write a `for`-comprehension combining customers and orders.
- Produce a daily sales summary without Spark.

## Concepts Covered

`map` • `filter` • `flatMap` • `reduce` • `Vector` • `Map` • `for`-comprehension • immutable collections • aggregation • customer/order processing.

## Collection Operations

### `map`

Transforms every element into another value.

Result: `List(1, 4, 9, 16, 25)`

### `filter`

Keeps only elements satisfying a condition.

Result: `List(2, 4)`

### `flatMap`

Maps each element and then flattens nested collections.

Result: `List(1, 2, 3, 4, 5, 6)`

### `reduce`

Combines collection elements into one result.

Result: `15`

## Vector — Indexed Customer Records

A `Vector[Customer]` stores customer records and demonstrates indexed access such as `customers(0)` and `customers(2)`.

Customers used in the exercise include:

- 101 — Anu
- 102 — Bala
- 103 — Charan
- 104 — Divya

## Map — Product Quantities and Prices

Two immutable maps are used:

- `productQuantities` stores product quantities.
- `productPrices` stores product prices.

Example: Laptop quantity = 4 and Laptop price = ₹60000.0.

## Customer and Order Combination

The program combines customer and order collections with a `for`-comprehension and matches records through `customerId`.

Example results include:

```text
Anu   → Laptop   → 1
Anu   → Mouse     → 2
Bala  → Keyboard  → 1
Charan→ Monitor   → 1
Divya → Mouse     → 3
```

This demonstrates readable collection joins without Spark.

## Daily Sales Processing

The program processes 9 sales records without Spark. Revenue is calculated as:

```text
Revenue = Quantity × Price
```

| Day | Revenue |
|---|---:|
| Monday | ₹128500.00 |
| Tuesday | ₹87200.00 |
| Wednesday | ₹79500.00 |
| **Total** | **₹295200.00** |

Sales with revenue greater than or equal to ₹10,000 are selected using `filter`.

## Processing Flow

```text
Raw Scala collections
        ↓
map / filter / flatMap
        ↓
Customer + order matching
        ↓
Revenue calculation
        ↓
Daily aggregation
```

## Performance Observations

- `map`, `filter`, and `flatMap` are useful for element-level transformations.
- `reduce` combines values into a single result.
- `Vector` is suitable when indexed access is needed.
- `Map` provides key-based lookup for quantities and prices.
- Immutable collections preserve the original data.
- Multiple collection operations can create intermediate collections and therefore consume additional memory.
- This local collection processing provides a useful comparison point for later distributed RDD processing.

## Environment

- OS: Ubuntu / WSL
- Java: 17.0.20
- Scala: 2.12.20
- SBT: 1.10.11

## Commands Used

### Navigate and inspect

```bash
cd ~/scala-spark-30-day-practice/Day-02-Scala-Collections
ls
find . -maxdepth 3 -type f | sort
```

### Compile and run

```bash
sbt compile
sbt run
```

### Save and inspect output

```bash
sbt run > output/result.txt 2>&1
cat output/result.txt
tail -n 30 output/result.txt
```

### Git workflow

```bash
git status --short
git add Day-02-Scala-Collections/
git commit -m "Complete Day 2 Scala collections"
git push origin main
```

If the remote contains newer commits:

```bash
git pull --rebase origin main
git push origin main
```

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

```bash
sbt compile
sbt run
```

Save output:

```bash
sbt run > output/result.txt 2>&1
```

## Learning Outcome

Day 2 demonstrates how Scala collections can transform, filter, flatten, aggregate, index, and combine related datasets. These concepts directly prepare for RDD transformations and pair operations in Spark.