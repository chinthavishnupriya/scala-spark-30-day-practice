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
- Comparing broadcast joins conceptually with Shuffle Sort-Merge Joins
- Running the same join through Spark SQL
- Aggregating transactions by branch
- Understanding when broadcast joins are appropriate
- Understanding memory considerations when broadcasting reference data

---

## Practice Scenario

The scenario represents a transaction system containing:

- A transaction fact table
- A small branch master/reference table

The transaction table represents the large side of the join, while the branch master represents the small reference side.

For this local practice exercise, the datasets are intentionally small so the job can run quickly on a local Spark installation.

The sample contains:

- 12 transactions
- 5 branches

The same architecture can be applied to a much larger transaction dataset in a distributed Spark environment.

---

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
