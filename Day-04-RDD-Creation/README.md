# Day 04 — RDD Creation and Operations

## Objective

Practice creating Apache Spark RDDs from Scala collections and text files, then apply basic transformations and actions to real-looking customer and sales data.

## Practice Requirements

- Create RDDs from Scala collections.
- Create RDDs from text files.
- Use `map`, `filter`, and `flatMap`.
- Calculate total sales using an RDD action.
- Inspect partition counts and default parallelism.
- Demonstrate how a large customer file can be divided across multiple partitions.

## What is an RDD?

An RDD (Resilient Distributed Dataset) is Spark's distributed collection abstraction. Data is divided into partitions so Spark can process different portions in parallel.

Important RDD characteristics demonstrated here:

- Distributed across partitions.
- Immutable after creation.
- Lazily evaluated.
- Fault tolerant through lineage.
- Processed using transformations and actions.

## RDD Creation from a Scala Collection

The program creates an RDD using `parallelize`:

```scala
val numbers = sc.parallelize(1 to 10, 4)
```

The second argument requests 4 partitions, allowing the collection to be processed as multiple logical chunks.

## RDD Creation from Text Files

Customer and sales records are loaded from input files using Spark's text-file API. Each input line becomes an RDD element.

### customers.txt

The customer dataset contains customer ID, name, city, and category. **12 customer records** are processed.

### sales.txt

The sales dataset contains transaction ID, product, quantity, and price. **6 sales transactions** are processed.

## Transformations

### `map`

Transforms each RDD element into another value. It is useful for parsing records or calculating a derived value such as revenue.

### `filter`

Keeps only elements that satisfy a condition, such as selecting valid records or sales above a threshold.

### `flatMap`

Maps each input element to zero or more output elements and flattens the result. It is useful when one input record can produce multiple output values.

## Sales Calculation

The sales pipeline parses the input records and calculates transaction revenue using:

```text
Revenue = Quantity × Price
```

`reduce` is then used to combine the transaction revenues into a total.

## Partitions and Parallelism

The program prints the number of partitions and Spark's default parallelism. Partitions determine the basic units of parallel processing.

A dataset with multiple partitions can be processed by multiple tasks, subject to the available Spark resources.

For a large customer file, increasing the number of partitions can allow the file's data to be processed in smaller chunks. The appropriate number depends on data size, available CPU resources, and workload characteristics.

## Processing Flow

```text
Input collection / text file
          ↓
        RDD
          ↓
   map / filter / flatMap
          ↓
   Parsed / transformed data
          ↓
       Action
          ↓
       Result
```

## Transformations vs Actions

Transformations describe how a new RDD should be produced and are evaluated lazily. Actions request a result and trigger Spark execution.

Examples in this exercise include transformations such as `map` and `filter`, and actions such as `collect` and `reduce`.

## Environment

- Ubuntu / WSL
- Java 17
- Scala 2.13.18
- Apache Spark 4.2.0
- SBT 1.10.11
- Spark master: `local[4]`

## Commands Used

### Navigate and inspect

```bash
cd ~/scala-spark-30-day-practice/Day-04-RDD-Creation
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
git add Day-04-RDD-Creation/
git commit -m "Complete Day 4 RDD creation"
git push origin main
```

## Project Structure

```text
Day-04-RDD-Creation/
├── README.md
├── build.sbt
├── code/
│   └── Day04.scala
├── input/
│   ├── customers.txt
│   └── sales.txt
├── output/
│   └── result.txt
├── screenshots/
├── src/
│   └── main/
│       └── scala/
│           └── Day04.scala
└── troubleshooting/
```

## How to Run

```bash
sbt compile
sbt run
```

Save the output when required:

```bash
sbt run > output/result.txt 2>&1
```

## Learning Outcome

Day 4 moves from ordinary Scala collections to distributed Spark data. It introduces RDD creation, partitions, transformations, actions, and basic sales processing, providing the foundation for the more advanced RDD exercises that follow.