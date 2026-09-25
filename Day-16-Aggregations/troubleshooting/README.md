# Day 16 - Troubleshooting

## 1. Spark Hostname Warning

Warning:

```text
WARN Utils: Your hostname, vishnupriya, resolves to a loopback address
```

This can occur in WSL during local Spark execution. It did not prevent the application from completing successfully.

## 2. Native Hadoop Library Warning

Warning:

```text
Unable to load native-hadoop library
```

Spark can use built-in Java implementations when the native Hadoop library is unavailable. This is common in local WSL development.

## 3. Compilation

Run:

```bash
sbt compile
java -version
scala -version
sbt --version
```

Expected environment:

- Java 17
- Scala 2.13.x
- SBT 1.10.11

## 4. Missing Input File

If Spark reports that the input path does not exist:

```bash
ls -l input/hospital_revenue.csv
cd ~/scala-spark-30-day-practice/Day-16-Aggregations
sbt run
```

The program expects `input/hospital_revenue.csv`.

## 5. Incorrect Aggregation Results

Check the input:

```bash
cat input/hospital_revenue.csv
```

The calculated revenue is:

```text
total_revenue = consultation_revenue + procedure_revenue
```

Expected record count is 14 and total revenue is 2847000.

## 6. HAVING-Like Filter

The DataFrame API applies the filter after aggregation:

```scala
.filter(col("department_revenue") > 300000)
```

Expected departments:

- Cardiology
- Orthopedics
- Neurology
- Pediatrics

Dermatology is excluded because its revenue is 245000.

## 7. Spark SQL Result

The SQL implementation uses:

```sql
GROUP BY department
HAVING SUM(total_revenue) > 300000
```

The SQL result should match the DataFrame HAVING-like result.

## 8. Shuffle in groupBy

The execution plan contains:

```text
Exchange hashpartitioning(department, 200)
```

This indicates redistribution of data for the department grouping.

## 9. Sorting and Exchange

Ordering the aggregated result can produce:

```text
Exchange rangepartitioning(...)
```

This represents another redistribution associated with sorting.

## 10. Execution Plan Verification

Run:

```bash
grep -n -A30 "EXECUTION PLAN" output/day16-execution-output.txt
```

Look for operations such as:

```text
FileScan
Project
HashAggregate
Exchange hashpartitioning
Exchange rangepartitioning
Sort
```

## 11. Output File Missing

Create the output directory and save a new run:

```bash
mkdir -p output
sbt run 2>&1 | tee output/day16-execution-output.txt
ls -lh output/day16-execution-output.txt
```

## 12. Target Directory Appears

SBT generates files under `target/`. These are build artifacts and should not be committed.

Check:

```bash
git status --short --ignored | grep target
```

The project `.gitignore` contains:

```text
target/
project/target/
```

## 13. Source Copy Verification

The project maintains:

```text
src/main/scala/Day16.scala
code/Day16.scala
```

Compare them:

```bash
cmp src/main/scala/Day16.scala code/Day16.scala
```

No output indicates that the files are identical.

## 14. Final Verification

Run:

```bash
sbt compile
sbt run 2>&1 | tee output/day16-execution-output.txt
grep -n "DAY 16 COMPLETED SUCCESSFULLY" output/day16-execution-output.txt
```

Expected completion marker:

```text
197:[info] DAY 16 COMPLETED SUCCESSFULLY
```

## 15. Git Verification

From the repository root:

```bash
cd ~/scala-spark-30-day-practice
git status --short
```

Before committing, verify that only intended Day 16 files are staged.

## Troubleshooting Summary

| Issue | Check |
|---|---|
| Hostname warning | Safe for local execution |
| Native Hadoop warning | Safe for local Spark |
| Compilation failure | Check Java/Scala/SBT versions |
| Input error | Check `input/hospital_revenue.csv` |
| Wrong totals | Check revenue columns |
| HAVING result | Check `> 300000` |
| Shuffle | Inspect `Exchange` |
| Missing output | Run with `tee` |
| Target files | Confirm `.gitignore` |
| Source mismatch | Run `cmp` |
