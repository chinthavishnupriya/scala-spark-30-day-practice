# Day 12 - Cache and Persist

## Objective
Practice Spark RDD caching and persistence by reusing a cleaned transaction dataset across multiple reports, comparing `cache()` with `persist()`, and observing different storage levels.

## Requirements Covered
1. Cache an RDD that is reused by multiple actions.
2. Compare `cache()` and `persist()`.
3. Experiment with different storage levels.
4. Explain when caching can hurt performance.
5. Reuse a cleaned transaction dataset in three reports.

## Environment
- Java: 17.0.20
- Scala: 2.13.18
- SBT: 1.10.11
- Apache Spark: 4.2.0
- Mode: local[4]

## Concepts
### Cache
`cache()` marks an RDD for reuse using Spark's default `MEMORY_ONLY` storage level. Caching is lazy: the RDD is materialized when an action runs.

### Persist
`persist()` is the configurable form of persistence. It allows an explicit storage level such as `MEMORY_ONLY` or `MEMORY_AND_DISK`.

### Storage Levels
- `MEMORY_ONLY`: keep deserialized partitions in memory; partitions that do not fit can be recomputed.
- `MEMORY_AND_DISK`: keep partitions in memory and spill partitions that do not fit to disk.

## Scenario
The application loads transaction records, cleans them, persists the cleaned RDD, and reuses it for three reports:
- Total revenue
- Revenue by category
- Customer transaction counts

## Execution Result
- Raw transaction count: 10
- Cleaned transaction count: 10
- Partitions: 4
- Total revenue: ₹35600.00
- Validated report reuse: YES
- `cache()` storage level: MEMORY_ONLY
- `persist(MEMORY_AND_DISK)`: MEMORY_AND_DISK

## When Caching Can Hurt
Caching can reduce performance when an RDD is used only once, when the dataset is too large for available memory, or when unnecessary cached data creates memory pressure and eviction.

## Project Structure
```text
Day-12-Cache-Persist/
├── .gitignore
├── .jvmopts
├── README.md
├── COMMANDS.md
├── build.sbt
├── code/Day12.scala
├── input/transactions.txt
├── output/result.txt
├── project/
│   ├── build.properties
│   └── README.md
├── screenshots/
├── src/main/scala/Day12.scala
└── troubleshooting/README.md
```

## Key Learning
Cache is convenient for repeated reuse, while persist provides explicit control over the storage level. The correct choice depends on reuse frequency, dataset size, and available memory.
