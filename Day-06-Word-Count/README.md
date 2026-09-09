# Day 06 — Word Count

## Objective

Implement the classic Spark Word Count program and extend it to support:

- Case-insensitive word counting
- Punctuation removal
- Empty-word filtering
- Top 10 most frequent words
- Spark transformations and actions
- Lazy evaluation
- Shuffle operations
- Basic Spark optimization concepts

## Input

Input file:

`input/application.log`

The application log contains 20 records with INFO, WARN, and ERROR messages.

## Implementation

The application is implemented in:

`src/main/scala/Day06.scala`

### Classic Word Count

The classic pipeline is:

```text
flatMap → filter → map → reduceByKey
