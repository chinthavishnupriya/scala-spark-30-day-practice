# Day 19 - Broadcast Join

## Objective

Practice Spark broadcast joins using a large fact-table pattern and a small reference table.

This exercise demonstrates:

- Reading transaction and branch reference data
- Performing a normal inner join
- Performing an explicit Spark broadcast join
- Inspecting the physical execution plan
- Identifying `BroadcastHashJoin`
- Identifying `BroadcastExchange`
- Comparing broadcast joins with Shuffle Sort-Merge Joins
- Running the join through Spark SQL
- Aggregating transactions by branch
- Understanding when broadcast joins are appropriate
- Understanding broadcast memory considerations

## Practice Scenario

The scenario represents a transaction system containing:

- A transaction fact table
- A small branch master/reference table

The transaction table represents the large side of the join, while the branch master represents the small reference side.

For local practice, the datasets are intentionally small so the job can run quickly.

Sample data:

- 12 transactions
- 5 branches

The same architecture can be applied to a much larger transaction dataset in a distributed Spark environment. The local run does not claim to process millions of transactions.

## Environment

| Component | Version |
|---|---|
| Apache Spark | 4.2.0 |
| Scala | 2.13.18 |
| Java | OpenJDK 17 |
| SBT | 1.10.11 |
| Execution mode | local[4] |
| Platform | Ubuntu / WSL2 |

## Project Structure

```text
Day-19-Broadcast-Join/
├── README.md
├── COMMANDS.md
├── .gitignore
├── build.sbt
├── code/
│   └── Day19.scala
├── input/
│   ├── branch_master.csv
│   └── transactions.csv
├── output/
│   └── day19-execution-output.txt
├── project/
│   └── build.properties
├── screenshots/
│   ├── 01-compilation-success.png
│   ├── 02-broadcast-join-result.png
│   ├── 03-broadcast-execution-plan.png
│   └── 04-branch-summary.png
├── src/
│   └── main/
│       └── scala/
│           └── Day19.scala
└── troubleshooting/
    └── README.md
```

Generated `target/` files are excluded by `.gitignore`.

## Input Data

### Branch Master

File: `input/branch_master.csv`

Columns:

```text
branch_id
branch_name
city
region
```

The file contains 5 branch records.

### Transactions

File: `input/transactions.csv`

Columns:

```text
transaction_id
customer_id
branch_id
amount
transaction_type
```

The file contains 12 transaction records.

The common join key is:

```text
branch_id
```

## Normal Inner Join

The application first performs a normal inner join between the transaction DataFrame and branch master DataFrame.

The normal join provides a reference result for comparison with the explicitly broadcast join.

## Broadcast Join

The application explicitly broadcasts the small branch DataFrame using:

```scala
broadcast(branchesDF)
```

The physical execution plan confirms:

```text
BroadcastHashJoin
BroadcastExchange
```

The execution output contains both operators.

## Why Broadcast Join?

A broadcast join is useful when one side of a join is sufficiently small to distribute to executors.

In this scenario:

```text
Large transaction fact
        +
Small branch reference
        =
Broadcast Join
```

The strategy avoids shuffling the large transaction side for the broadcast join and instead distributes the small reference relation.

The small relation must still fit appropriately in executor memory.

## Broadcast Exchange

The physical plan contains:

```text
BroadcastExchange HashedRelationBroadcastMode(...)
```

This represents preparation and distribution of the broadcast relation.

## Broadcast Hash Join

The physical plan contains:

```text
BroadcastHashJoin [branch_id...]
```

The plan also identifies the small branch-side relation as the build side.

This is direct physical-plan evidence that the explicit broadcast strategy was used.

## Broadcast Join vs Shuffle Sort-Merge Join

### Broadcast Join

Typical pattern:

```text
Small table
    |
    v
Broadcast
    |
    v
Executors
    |
    v
Join with large table
```

Advantages and considerations:

- Avoids shuffling the large side for this strategy
- Useful for small dimension/reference tables
- Can be efficient for fact-to-dimension joins
- Uses `BroadcastHashJoin`
- The broadcast relation consumes executor memory
- Broadcasting an unsuitable large relation can create memory pressure

### Shuffle Sort-Merge Join

When both join sides are too large to broadcast, Spark can use a shuffle-based strategy.

Conceptually:

```text
Shuffle
   |
Partition
   |
Sort
   |
Merge
```

This can introduce network shuffle and sorting work.

This Day 19 implementation directly verifies the broadcast physical plan. The comparison with Shuffle Sort-Merge Join is explained conceptually; a separately forced Sort-Merge execution is not claimed as part of this run.

## Spark SQL

The application registers the DataFrames as temporary views and performs the join using Spark SQL.

This demonstrates that the same relational join can be expressed through SQL while Spark determines the physical execution strategy.

## Branch-Wise Aggregation

The joined data is aggregated by branch.

| Branch | Transactions | Total Amount | Average Amount |
|---|---:|---:|---:|
| B001 | 4 | 22400 | 5600.0 |
| B005 | 2 | 12800 | 6400.0 |
| B004 | 2 | 12100 | 6050.0 |
| B002 | 2 | 7000 | 3500.0 |
| B003 | 2 | 4900 | 2450.0 |

These values are from the local sample input.

## Transformations and Actions

The application uses transformations such as:

- `select`
- `filter`
- `join`
- `withColumn`
- `groupBy`
- `agg`

Actions include operations such as:

- `show`
- `count`

Transformations are lazily evaluated until an action requires execution.

## Execution Evidence

The saved output is:

`output/day19-execution-output.txt`

The execution successfully confirms Spark 4.2.0 and the broadcast physical operators:

```text
BroadcastHashJoin
BroadcastExchange
```

## Screenshots

- `screenshots/01-compilation-success.png` — compilation
- `screenshots/02-broadcast-join-result.png` — join result
- `screenshots/03-broadcast-execution-plan.png` — broadcast physical plan
- `screenshots/04-branch-summary.png` — branch aggregation

## Verification Commands

Compile:

```bash
sbt clean compile
```

Run:

```bash
sbt run
```

Verify broadcast operators:

```bash
grep -E "BroadcastHashJoin|BroadcastExchange" output/day19-execution-output.txt
```

Inspect the complete output:

```bash
less output/day19-execution-output.txt
```

## Troubleshooting

See:

```text
troubleshooting/README.md
```

It covers compilation, Spark warnings, missing inputs, join keys, broadcast-plan verification, memory considerations, screenshots, generated files, and Git operations.

## Completion Checklist

- [x] Spark project created
- [x] Branch master input created
- [x] Transaction input created
- [x] Normal inner join implemented
- [x] Explicit broadcast join implemented
- [x] Join results executed successfully
- [x] `BroadcastHashJoin` verified
- [x] `BroadcastExchange` verified
- [x] Spark SQL join implemented
- [x] Branch-wise aggregation implemented
- [x] Execution output saved
- [x] Compilation screenshot captured
- [x] Broadcast join screenshot captured
- [x] Execution-plan screenshot captured
- [x] Branch summary screenshot captured
- [x] README prepared
- [x] COMMANDS prepared
- [x] Troubleshooting guide prepared

## Conclusion

Day 19 demonstrates how Spark broadcast joins work when one side of a join is small enough to distribute to executors.

The local execution provides direct physical-plan evidence through:

```text
BroadcastHashJoin
BroadcastExchange
```

The exercise also connects the logical join operation with Spark's physical execution plan and explains the trade-off between broadcasting a small reference dataset and using shuffle-based strategies for larger relations.
