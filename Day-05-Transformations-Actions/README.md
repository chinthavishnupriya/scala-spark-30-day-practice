# Day 05 — Transformations and Actions

## Objective

Practice Apache Spark RDD transformations and actions using Scala.

This day focuses on:

- `map`
- `filter`
- `flatMap`
- `distinct`
- `union`
- `count`
- `collect`
- `first`
- `take`
- `reduce`
- Transformations vs Actions
- Lazy evaluation
- Shuffle boundaries
- Basic log analysis

---

## Environment

| Component | Version |
|---|---|
| OS | Ubuntu / WSL2 |
| Java | 17.0.20 |
| Scala | 2.13.18 |
| Apache Spark | 4.2.0 |
| SBT | 1.10.11 |
| Spark Master | `local[4]` |

---

## Project Structure

```text
Day-05-Transformations-Actions/
├── README.md
├── COMMANDS.md
├── .jvmopts
├── build.sbt
├── code/
│   └── Day05.scala
├── input/
│   └── application.log
├── output/
│   └── day05_run.txt
├── project/
│   └── build.properties
├── screenshots/
├── src/
│   └── main/
│       └── scala/
│           └── Day05.scala
└── troubleshooting/
