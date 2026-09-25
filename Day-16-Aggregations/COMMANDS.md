# Day 16 - Commands

## 1. Navigate to Repository

```bash
cd ~/scala-spark-30-day-practice
cd Day-16-Aggregations
```

## 2. Verify Project Structure

```bash
find . -maxdepth 3 -type f | sort
```

## 3. Check Source Code

```bash
cat src/main/scala/Day16.scala
cat code/Day16.scala
cmp src/main/scala/Day16.scala code/Day16.scala
```

No output from `cmp` means the two source files are identical.

## 4. Check Input Dataset

```bash
cat input/hospital_revenue.csv
wc -l input/hospital_revenue.csv
```

Expected line count: 15 (header plus 14 data records).

## 5. Check Build Configuration

```bash
cat build.sbt
cat .jvmopts
cat project/build.properties
```

## 6. Compile

```bash
sbt compile
```

Expected result includes:

```text
[success]
```

## 7. Run

```bash
sbt run
```

## 8. Save Execution Output

```bash
sbt run 2>&1 | tee output/day16-execution-output.txt
```

## 9. Verify Completion

```bash
grep -n "DAY 16 COMPLETED SUCCESSFULLY" output/day16-execution-output.txt
```

Expected:

```text
197:[info] DAY 16 COMPLETED SUCCESSFULLY
```

## 10. Verify Basic Aggregations

```bash
grep -n -A8 "BASIC AGGREGATIONS" output/day16-execution-output.txt
```

Expected values include 14 records, total revenue 2847000, average revenue 203357.14285714287, minimum 115000, and maximum 336000.

## 11. Verify Department Aggregation

```bash
grep -n -A15 "DEPARTMENT-WISE REVENUE STATISTICS" output/day16-execution-output.txt
```

Expected departments include Cardiology, Orthopedics, Neurology, Pediatrics, and Dermatology.

## 12. Verify Multiple-Column Grouping

```bash
grep -n -A25 "DEPARTMENT + CITY AGGREGATION" output/day16-execution-output.txt
```

## 13. Verify HAVING-Like Filter

```bash
grep -n -A10 "HAVING-LIKE FILTER" output/day16-execution-output.txt
```

The filter condition is `department_revenue > 300000`.

## 14. Verify Patient Statistics

```bash
grep -n -A12 "PATIENT STATISTICS BY DEPARTMENT" output/day16-execution-output.txt
```

## 15. Verify Highest Revenue Department

```bash
grep -n -A6 "HIGHEST REVENUE DEPARTMENT" output/day16-execution-output.txt
```

Expected result: Cardiology with 840000.

## 16. Verify Spark SQL

```bash
grep -n -A12 "SPARK SQL AGGREGATION" output/day16-execution-output.txt
```

## 17. Verify Execution Plan

```bash
grep -n -A30 "EXECUTION PLAN" output/day16-execution-output.txt
```

## 18. Verify Shuffle Exchanges

```bash
grep -n "Exchange" output/day16-execution-output.txt
```

Expected plan entries include `Exchange hashpartitioning(department, 200)` and a rangepartitioning exchange.

## 19. Verify Documentation

```bash
wc -l README.md COMMANDS.md troubleshooting/README.md
```

## 20. Verify Project Files

```bash
find . -maxdepth 3 -type f | sort
```

## 21. Check Git Status

```bash
git status --short
```

## 22. Check Ignored Target Directory

```bash
git status --short --ignored | grep target
```

Generated `target/` files should be ignored.

## 23. Move to Repository Root

```bash
cd ~/scala-spark-30-day-practice
```

## 24. Add Day 16

```bash
git add Day-16-Aggregations
```

## 25. Review Staged Files

```bash
git status
```

Confirm that generated `target/` files are not staged.

## 26. Commit

```bash
git commit -m "Add Day 16 Spark aggregations practice"
```

## 27. Push

```bash
git push origin main
```

## 28. Final Verification

```bash
git status
git log -1 --oneline
```

Expected status:

```text
On branch main
Your branch is up to date with 'origin/main'.
nothing to commit, working tree clean
```

Expected latest commit message:

```text
Add Day 16 Spark aggregations practice
```
