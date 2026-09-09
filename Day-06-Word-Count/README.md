# Day 06 — Word Count

## Objective

Implement the classic Spark Word Count program and extend it into a small application-log analysis pipeline. The exercise focuses on RDD transformations, actions, lazy evaluation, shuffle operations, and basic optimization.

## Practice Requirements

- Implement classic Word Count.
- Explain `flatMap → map → reduceByKey`.
- Perform case-insensitive counting.
- Ignore punctuation and empty words.
- Find the top 10 most frequent words in application logs.

## Input

Input file:

`input/application.log`

The application log contains **20 records** with INFO, WARN, and ERROR messages. The input is processed using an RDD with **4 partitions**.

## Classic Word Count

The classic pipeline is:

```text
Lines
  ↓
flatMap
  ↓
words
  ↓
map(word => (word, 1))
  ↓
reduceByKey(_ + _)
  ↓
word counts
```

### `flatMap`

Splits each input line into individual words and flattens all resulting collections into one RDD.

### `map`

Converts every word into a key-value pair:

```text
(word, 1)
```

### `reduceByKey`

Combines values belonging to the same word key. It is an aggregation operation and can perform map-side combining before the shuffle.

## Case-Insensitive Processing

The extended pipeline normalizes words to lowercase so that variations such as `INFO` and `info` are counted as the same word.

Punctuation is removed before splitting, and empty strings are filtered out. This produces cleaner application-log statistics.

## Top 10 Frequent Words

The program sorts word counts by frequency in descending order and takes the first 10 results.

Observed top 10 results:

| Rank | Word | Count |
|---:|---|---:|
| 1 | info | 12 |
| 2 | request | 8 |
| 3 | processing | 6 |
| 4 | error | 5 |
| 5 | application | 5 |
| 6 | failed | 5 |
| 7 | connection | 4 |
| 8 | database | 4 |
| 9 | successfully | 4 |
| 10 | warn | 3 |

## Transformations and Actions

Transformations used include `flatMap`, `filter`, `map`, `reduceByKey`, and sorting operations. Actions such as `collect` and `take` trigger Spark execution and return results to the driver.

## Shuffle Boundary

`reduceByKey` is a wide transformation because values with the same key may originate from different partitions. Spark therefore needs to redistribute key-value data during the shuffle stage.

Conceptually:

```text
Input partitions
      ↓
map(word, 1)
      ↓
local combine
      ↓
   SHUFFLE
      ↓
reduceByKey
      ↓
word counts
```

## Performance Observations

- `flatMap`, `filter`, and `map` are generally narrow transformations.
- `reduceByKey` introduces a shuffle boundary.
- `reduceByKey` is generally preferable to manually grouping all values because it can combine values before transferring data across the network.
- Normalizing text before aggregation avoids duplicate keys caused by case differences.
- `take(10)` is suitable for returning a small top-results set to the driver.

## Project Structure

```text
Day-06-Word-Count/
├── .gitignore
├── .jvmopts
├── README.md
├── build.sbt
├── code/
│   └── Day06.scala
├── input/
│   └── application.log
├── output/
│   └── result.txt
├── screenshots/
│   ├── 01-spark-startup.png
│   ├── 02-classic-word-count.png
│   ├── 03-case-insensitive-word-count.png
│   └── 04-top-10-and-success.png
└── src/
    └── main/
        └── scala/
            └── Day06.scala
```

## How to Run

```bash
sbt compile
sbt run
```

Save execution output:

```bash
sbt run > output/result.txt 2>&1
```

## Learning Outcome

Day 6 demonstrates a complete Spark word-processing pipeline and introduces the important relationship between text normalization, key-value aggregation, shuffles, lazy execution, and result collection.