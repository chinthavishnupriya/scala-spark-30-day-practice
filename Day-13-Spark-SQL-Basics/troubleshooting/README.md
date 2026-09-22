# Day 13 – Troubleshooting

This document records common problems encountered while setting up, compiling, running, and documenting Day 13.

## 1. SBT Version Check

Check the configured version:

```bash
cat project/build.properties
```

Expected:

```text
sbt.version=1.10.11
```

Check installed SBT:

```bash
sbt --version
```

## 2. Wrong Directory

If SBT reports that it cannot find `build.sbt` or the `project` directory, check the current directory:

```bash
pwd
ls -la
```

Move into Day 13:

```bash
cd ~/scala-spark-30-day-practice/Day-13-Spark-SQL-Basics
```

Do not run the Day 13 SBT commands from the repository root unless the root itself contains the required SBT project.

## 3. Missing build.sbt

Check:

```bash
ls -l build.sbt
```

If it is missing, verify that you are in:

```text
Day-13-Spark-SQL-Basics
```

The project build file defines Scala 2.13.18 and Spark 4.2.0 dependencies.

## 4. Missing Input File

If Spark reports that `input/customers.csv` cannot be found:

```bash
ls -l input/customers.csv
```

Run the application from the Day 13 project directory:

```bash
cd ~/scala-spark-30-day-practice/Day-13-Spark-SQL-Basics
sbt "runMain Day13"
```

The source uses the relative path:

```text
input/customers.csv
```

## 5. Java Compatibility

Check Java:

```bash
java -version
```

Day 13 was tested with OpenJDK 17.0.20.

The SBT build also includes Java module `--add-opens` options required by the configured Spark runtime.

If Java changes, re-check the project configuration before changing dependencies.

## 6. Compilation Problems

Run:

```bash
sbt clean compile
```

Read the first actual compiler error rather than the final summary line.

Useful checks:

```bash
cat build.sbt
cat project/build.properties
cat src/main/scala/Day13.scala
```

## 7. Spark Startup Warnings

Spark may print warnings during startup. In the successful Day 13 run, startup warnings were non-fatal and the application continued to execute normally.

Verify the application result rather than treating every warning as an error.

## 8. CSV Schema Issues

The application reads the CSV using:

```scala
.option("header", "true")
.option("inferSchema", "true")
```

If columns are not inferred as expected:

```bash
head input/customers.csv
```

Confirm that the first row contains the expected column names and that values are comma-separated.

## 9. SQL View Problems

The program registers the DataFrame with:

```scala
categorizedCustomers.createOrReplaceTempView("customers")
```

SQL queries then use:

```sql
FROM customers
```

If the view is unavailable, make sure the view creation occurs before the SQL statements and that the same SparkSession is being used.

## 10. Unexpected Analytics Results

Verify the source data first:

```bash
cat input/customers.csv
```

Then inspect the DataFrame:

```bash
sbt "runMain Day13"
```

Pay particular attention to:

- `total_spend`
- `spending_category`
- `loyalty_points`
- `GROUP BY city`
- `ORDER BY total_spend DESC`
- `LIMIT 3`

## 11. Output File Problems

Create the output directory:

```bash
mkdir -p output
```

Save the run again:

```bash
sbt "runMain Day13" | tee output/result.txt
```

Verify:

```bash
ls -lh output/result.txt
tail -n 20 output/result.txt
```

## 12. Screenshot Copy Problems

Create the destination:

```bash
mkdir -p screenshots
```

Check the Windows source:

```bash
ls -lh "/mnt/c/Users/chint/OneDrive/图片/Screenshots/"
```

Then copy each required image and verify:

```bash
ls -lh screenshots/
```

## 13. Git Shows target/ Directories

Generated SBT files should not be committed.

Check ignored files:

```bash
git status --ignored --short | grep -E 'target/|project/target/'
```

The project's `.gitignore` contains:

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

If `target/` appears as untracked instead of ignored, inspect `.gitignore`:

```bash
cat .gitignore
```

## 14. Accidental Root-Level project/ or troubleshooting/

If directories were accidentally created in the repository root, first verify their contents:

```bash
cd ~/scala-spark-30-day-practice
ls -la
```

Only remove an accidentally created directory after confirming it is not the intended Day 13 directory.

The intended locations are:

```text
Day-13-Spark-SQL-Basics/project/
Day-13-Spark-SQL-Basics/troubleshooting/
```

## 15. Git Working Tree Verification

Before committing:

```bash
git status --short
git diff --cached --stat
```

After pushing:

```bash
git status
```

The expected final state is a clean working tree.

## 16. Quick Recovery Sequence

For a normal rebuild and rerun:

```bash
cd ~/scala-spark-30-day-practice/Day-13-Spark-SQL-Basics
sbt clean compile
sbt "runMain Day13" | tee output/result.txt
cat output/result.txt
```

If this completes successfully, the main Day 13 execution path is working.
