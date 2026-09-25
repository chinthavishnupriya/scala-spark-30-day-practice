# Day 17 — Troubleshooting

## 1. Hostname loopback warning

Spark may display:

```text
Your hostname ... resolves to a loopback address
```

This can occur in WSL2. The program can still run successfully in local mode.

If required, set the local Spark IP before running:

```bash
export SPARK_LOCAL_IP=127.0.0.1
sbt run
```

## 2. Native Hadoop library warning

You may see:

```text
Unable to load native-hadoop library
```

For this local Spark practice project, Spark can use the built-in Java implementations.

## 3. Compilation failure

Check:

```bash
java -version
sbt --version
sbt clean compile
```

The project is configured for Scala 2.13.18, Spark 4.2.0 and Java 17.

## 4. Input file not found

The application expects:

```text
input/students.csv
input/customer_policies.csv
```

Confirm:

```bash
pwd
ls -lh input/
```

Run the application from the Day 17 project directory.

## 5. Incorrect ranking ties

The main ranking window orders by:

```text
score DESC, student ASC
```

The secondary student ordering makes the ordering deterministic.

A separate score-only window is intentionally used to demonstrate how `rank()` and `dense_rank()` behave when students have equal scores.

## 6. Incorrect LAG/LEAD results

The policy-history window must be chronological:

```scala
Window
  .partitionBy("customer_id")
  .orderBy(asc("policy_date"))
```

Then:

- `lag()` = previous policy premium
- `lead()` = next policy premium

Do not use the descending latest-policy window for the LAG/LEAD calculation.

## 7. Latest policy is wrong

The latest-policy window must use:

```scala
Window
  .partitionBy("customer_id")
  .orderBy(desc("policy_date"))
```

Then filter:

```scala
col("latest_row_number") === 1
```

Expected latest policy IDs:

- C101 → P002
- C102 → P004
- C103 → P006
- C104 → P009

## 8. Premium change

The calculation is:

```scala
col("premium") - col("previous_premium")
```

The first chronological policy for each customer has no previous premium, so its change is null.

## 9. Execution plan and shuffle

Window functions commonly require data to be partitioned by the window key and sorted by the window ordering.

Inspect the plans with:

```scala
top3StudentsDF.explain(true)
latestPolicyDF.explain(true)
```

Look for window, sort and exchange/partitioning stages in the physical plan.

## 10. Output verification

Run:

```bash
sbt run 2>&1 | tee output/day17-execution-output.txt
grep -n "DAY 17 COMPLETED SUCCESSFULLY" output/day17-execution-output.txt
```

The completion marker confirms that the application reached the end of the program.

## 11. Screenshot verification

Check:

```bash
ls -lh screenshots/
file screenshots/*.png
```

Required screenshots:

1. Compilation success
2. Ranking result
3. LAG/LEAD and latest policy
4. Execution plan

## 12. Generated build files

Do not commit generated SBT/Scala build directories.

The project ignores:

```text
target/
project/target/
metals/
.bloop/
.idea/
.vscode/
*.class
*.log
```

## 13. Final verification

From the repository root:

```bash
git status
```

Confirm the working tree is clean after committing and pushing.
