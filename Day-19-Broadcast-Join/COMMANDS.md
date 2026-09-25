# Day 19 - Commands

## 1. Navigate

```bash
cd ~/scala-spark-30-day-practice/Day-19-Broadcast-Join
```

## 2. Verify structure

```bash
find . -maxdepth 3 -type f | sort
```

## 3. Verify environment

```bash
java -version
scala -version
sbt --version
```

Expected project versions:

- Java: OpenJDK 17
- Scala: 2.13.18
- SBT: 1.10.11
- Spark: 4.2.0

## 4. Inspect input files

```bash
cat input/branch_master.csv
cat input/transactions.csv
```

Count data rows:

```bash
tail -n +2 input/branch_master.csv | wc -l
tail -n +2 input/transactions.csv | wc -l
```

Expected:

```text
5 branches
12 transactions
```

## 5. Compile

```bash
sbt clean compile
```

A successful compilation ends with a `[success]` message.

## 6. Run

```bash
sbt run
```

For a saved execution log:

```bash
sbt run 2>&1 | tee output/day19-execution-output.txt
```

## 7. Verify Spark version

```bash
grep -E "Running Spark version" output/day19-execution-output.txt
```

Expected Spark version:

```text
4.2.0
```

## 8. Verify broadcast physical plan

```bash
grep -E "BroadcastHashJoin|BroadcastExchange" output/day19-execution-output.txt
```

Expected:

```text
BroadcastHashJoin
BroadcastExchange
```

Inspect surrounding plan:

```bash
grep -A20 -B5 "BroadcastHashJoin" output/day19-execution-output.txt
```

## 9. Inspect complete execution output

```bash
less output/day19-execution-output.txt
```

Press `q` to exit.

## 10. Verify branch aggregation

```bash
grep -A10 "Branch-wise" output/day19-execution-output.txt
```

Expected local totals include:

```text
B001  4  22400  5600.0
B005  2  12800  6400.0
B004  2  12100  6050.0
B002  2   7000  3500.0
B003  2   4900  2450.0
```

## 11. Verify source contains broadcast

```bash
grep -n "broadcast" src/main/scala/Day19.scala
```

## 12. Verify source copies

The implementation is maintained in both:

```text
src/main/scala/Day19.scala
code/Day19.scala
```

Compare:

```bash
diff -u src/main/scala/Day19.scala code/Day19.scala
```

No output means the copies match.

## 13. Verify screenshots

```bash
ls -lh screenshots/
file screenshots/*.png
```

Required:

- `01-compilation-success.png`
- `02-broadcast-join-result.png`
- `03-broadcast-execution-plan.png`
- `04-branch-summary.png`

## 14. Verify ignored build files

```bash
git check-ignore -v target/scala-2.13/day-19-broadcast-join_2.13-1.0.jar
```

The result should show the `target/` rule from `.gitignore`.

## 15. Git status

From the repository root:

```bash
cd ~/scala-spark-30-day-practice
git status --short
```

## 16. Stage Day 19

```bash
git add Day-19-Broadcast-Join
git status --short
```

Review:

```bash
git diff --cached --stat
git diff --cached --name-only
```

Confirm `target/` files are not staged.

## 17. Commit

```bash
git commit -m "Add Day 19 Spark broadcast join practice"
```

## 18. Synchronize

```bash
git pull --rebase origin main
```

## 19. Push

```bash
git push origin main
```

## 20. Final Git verification

```bash
git status
git log --oneline -5
```

Expected final state:

```text
Your branch is up to date with 'origin/main'.
nothing to commit, working tree clean
```

## 21. Final project verification

```bash
find Day-19-Broadcast-Join -maxdepth 3 -type f | sort
```

Important tracked files:

```text
Day-19-Broadcast-Join/README.md
Day-19-Broadcast-Join/COMMANDS.md
Day-19-Broadcast-Join/build.sbt
Day-19-Broadcast-Join/code/Day19.scala
Day-19-Broadcast-Join/input/branch_master.csv
Day-19-Broadcast-Join/input/transactions.csv
Day-19-Broadcast-Join/output/day19-execution-output.txt
Day-19-Broadcast-Join/project/build.properties
Day-19-Broadcast-Join/screenshots/01-compilation-success.png
Day-19-Broadcast-Join/screenshots/02-broadcast-join-result.png
Day-19-Broadcast-Join/screenshots/03-broadcast-execution-plan.png
Day-19-Broadcast-Join/screenshots/04-branch-summary.png
Day-19-Broadcast-Join/src/main/scala/Day19.scala
Day-19-Broadcast-Join/troubleshooting/README.md
```
