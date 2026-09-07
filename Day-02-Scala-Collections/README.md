# Day 2 — Scala Collections

## Objective
Practice Scala collection operations and apply them to a small daily sales-processing problem.

## Concepts Covered
- map
- filter
- flatMap
- reduce
- Vector
- Map
- for-comprehension with yield
- Immutable collections
- Daily sales aggregation

## Environment
- OS: Ubuntu / WSL
- Java: 17.0.20
- Scala: 2.12.20
- SBT: 1.10.11

## Results

### map
Transforms every element of a collection.
Result: List(1, 4, 9, 16, 25)

### filter
Selects elements satisfying a condition.
Result: List(2, 4)

### flatMap
Flattens nested collections.
Result: List(1, 2, 3, 4, 5, 6)

### reduce
Combines collection elements into one result.
Result: 15

### Vector
Demonstrated using product names and indexed access.
Result: Vector(Laptop, Mouse, Keyboard, Monitor)

### Map
Demonstrated using product-price pairs and key-based lookup.

### For-Comprehension
Selected students scoring 70 or above: Anu, Bala and Charan.

## Daily Sales Processing
The program processes 9 sales records.

Revenue = quantity × price

| Day | Revenue |
|---|---:|
| Monday | ₹128500.00 |
| Tuesday | ₹87200.00 |
| Wednesday | ₹79500.00 |
| **Total** | **₹295200.00** |

High-value sales with revenue greater than or equal to ₹10,000 are selected using filter.

## Performance Observations
- map transforms every element.
- filter retains elements satisfying a condition.
- flatMap maps and flattens collections.
- reduce combines elements into one result.
- Vector provides efficient indexed access.
- Map provides key-based lookup.
- groupBy is useful for aggregation but requires additional memory.
- Immutable collections do not modify the original collection.
- Multiple collection operations may create intermediate collections.

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
Compilation and execution completed successfully.

Final result: 9 sales records, 4 products, total revenue ₹295200.00.

## Learning Outcome
Day 2 demonstrates how Scala collections can transform, filter, flatten, aggregate and summarize data before moving to distributed processing with Apache Spark.
