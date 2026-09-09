# Day 01 — Scala Essentials

## Objective

Build the Scala fundamentals required for Apache Spark programming. This day focuses on variables, immutability, collections, traits, `for`-comprehensions, and collection-based data processing.

## Practice Requirements

- Understand `val`, `var`, and `lazy val`.
- Work with immutable Scala collections.
- Use a `for`-comprehension with `yield`.
- Compare `List`, `Vector`, `Set`, and `Map`.
- Create a `Logger` trait with multiple implementations.
- Process student marks and generate grades using Scala collections.

## Concepts Covered

### `val`, `var`, and `lazy val`

- `val` creates a stable reference that cannot be reassigned.
- `var` allows reassignment and is useful when a changing value is required.
- `lazy val` delays initialization until the value is first accessed.

### Immutable Collections

The exercise uses immutable collections so that transformations create new values rather than changing the original collection. This style is useful in Spark because transformations naturally produce new datasets.

### `for`-Comprehension

A `for`-comprehension is used with `yield` to transform and combine student records and marks in readable Scala syntax.

### Collection Comparison

| Collection | Main characteristic | Example use |
|---|---|---|
| `List` | Ordered sequence | Sequential data |
| `Vector` | Indexed sequence | Indexed access |
| `Set` | Unique elements | Removing duplicates |
| `Map` | Key-value pairs | Fast key-based lookup |

## Logger Trait

A common `Logger` interface is defined as a Scala trait and implemented by more than one class. This demonstrates abstraction and polymorphism before moving to Spark application design.

## Student Grade Processing

The program stores student information and marks, filters and transforms the records, calculates grades, and produces a final processed collection. The processing demonstrates how Scala collection operations can be combined into a small data-processing pipeline.

Typical processing flow:

```text
Student records
      ↓
Filter valid records
      ↓
Transform marks
      ↓
Calculate grade
      ↓
Produce processed result
```

## Why This Matters for Spark

Spark applications are written using Scala collections concepts such as functions, immutable values, higher-order functions, and transformations. Understanding these fundamentals makes later RDD operations such as `map`, `filter`, and `reduce` easier to understand.

## Environment

- OS: Ubuntu / WSL
- Scala: 2.12.20
- SBT: 1.10.11
- Java: 17.0.20

## Commands Used

### Navigate to the project

```bash
cd ~/scala-spark-30-day-practice/Day-01-Scala-Essentials
```

### Inspect files

```bash
ls
find . -maxdepth 3 -type f | sort
```

### Compile

```bash
sbt compile
```

### Run

```bash
sbt run
```

### Save execution output

```bash
sbt run > output/result.txt 2>&1
```

### Inspect output

```bash
cat output/result.txt
```

```bash
tail -n 30 output/result.txt
```

### Git verification and upload

```bash
git status --short
git add Day-01-Scala-Essentials/
git commit -m "Complete Day 1 Scala essentials"
git push origin main
```

## Project Structure

```text
Day-01-Scala-Essentials/
├── README.md
├── build.sbt
├── project/
│   └── build.properties
├── code/
│   └── Day01.scala
├── src/
│   └── main/
│       └── scala/
│           └── Day01.scala
├── output/
│   └── result.txt
├── screenshots/
└── troubleshooting/
```

## How to Run

The main commands are:

```bash
sbt compile
sbt run
```

To save output:

```bash
sbt run > output/result.txt 2>&1
```

## Learning Outcome

Day 1 establishes the Scala programming foundation for the remaining Spark exercises: immutable data, collection transformations, functional-style programming, traits, and structured data processing.