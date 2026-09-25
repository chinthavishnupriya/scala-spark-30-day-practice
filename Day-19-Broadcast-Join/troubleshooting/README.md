# Day 19 - Troubleshooting

## 1. SBT compilation failure

Run:

```bash
sbt clean compile
```

Check:

```bash
java -version
scala -version
sbt --version
```

The project uses Java 17, Scala 2.13.18, Spark 4.2.0, and SBT 1.10.11.

## 2. Spark startup warnings

Spark may display warnings about hostname resolution or native Hadoop libraries during local execution.

These are not automatically failures. Check for the final successful completion and the expected application output.

## 3. Missing input file

Check:

```bash
ls -lh input/
head input/branch_master.csv
head input/transactions.csv
```

Expected files:

```text
branch_master.csv
transactions.csv
```

## 4. Incorrect join key

Both DataFrames must use:

```text
branch_id
```

for the transaction-to-branch join.

A typo or mismatched key can produce an empty or incorrect join result.

## 5. BroadcastHashJoin not appearing

Verify the explicit broadcast call:

```bash
grep -n "broadcast" src/main/scala/Day19.scala
```

Then run:

```bash
sbt run
```

Check:

```bash
grep -E "BroadcastHashJoin|BroadcastExchange" output/day19-execution-output.txt
```

Expected operators:

```text
BroadcastHashJoin
BroadcastExchange
```

## 6. Broadcast memory considerations

Broadcast is intended for a sufficiently small relation.

Broadcasting an excessively large DataFrame can create executor memory pressure. For cases where both relations are large, a shuffle-based join strategy may be more suitable.

## 7. Normal and broadcast results differ

Check the join key and input data:

```bash
cat input/transactions.csv
cat input/branch_master.csv
```

The same join condition should be used for both joins.

## 8. Physical-plan verification

Use:

```bash
grep -A20 -B5 "BroadcastHashJoin" output/day19-execution-output.txt
```

The plan should include `BroadcastHashJoin` and `BroadcastExchange`.

## 9. Spark SQL join problem

Verify that the required temporary views are registered before the SQL query executes.

Inspect the output:

```bash
less output/day19-execution-output.txt
```

## 10. Branch aggregation problem

The aggregation uses `branch_id` and `amount`.

Expected local totals:

```text
B001 = 22400
B005 = 12800
B004 = 12100
B002 = 7000
B003 = 4900
```

## 11. Screenshot verification

Check:

```bash
ls -lh screenshots/
file screenshots/*.png
```

Required screenshots:

```text
01-compilation-success.png
02-broadcast-join-result.png
03-broadcast-execution-plan.png
04-branch-summary.png
```

## 12. Output file

The saved execution output is:

```text
output/day19-execution-output.txt
```

Inspect it with:

```bash
less output/day19-execution-output.txt
```

## 13. Generated target files

SBT creates generated files under `target/`.

Verify that the JAR is ignored:

```bash
git check-ignore -v target/scala-2.13/day-19-broadcast-join_2.13-1.0.jar
```

The project's `.gitignore` contains:

```text
target/
```

## 14. Source synchronization

Compare the two source copies:

```bash
diff -u src/main/scala/Day19.scala code/Day19.scala
```

No output means they are identical.

## 15. Git push failure

Check:

```bash
git status
git pull --rebase origin main
git push origin main
```

If a rebase conflict occurs, resolve the conflicting file, stage it, and continue:

```bash
git add <file>
git rebase --continue
```

Then push again.

## 16. Final verification

Run:

```bash
sbt clean compile
sbt run
grep -E "BroadcastHashJoin|BroadcastExchange" output/day19-execution-output.txt
git status
```

The final repository should contain the source, input data, saved output, screenshots, README, commands, and troubleshooting guide while generated build files remain ignored.
