# Day 13 – Commands

This file records the commands used to build, run, verify, document, and push the Day 13 Spark SQL practical.

## 1. Navigate to the Project

```bash
cd ~/scala-spark-30-day-practice/Day-13-Spark-SQL-Basics
pwd
```

Expected directory:

```text
~/scala-spark-30-day-practice/Day-13-Spark-SQL-Basics
```

## 2. Check Project Files

```bash
ls -la
find . -maxdepth 3 -type f | sort
```

## 3. Check Java, Scala, SBT, and Spark Versions

```bash
java -version
scala -version
sbt --version
spark-submit --version
```

Day 13 was developed with Java 17, Scala 2.13.18, SBT 1.10.11, and Spark 4.2.0.

## 4. Verify the SBT Configuration

```bash
cat project/build.properties
cat build.sbt
```

Expected SBT configuration:

```text
sbt.version=1.10.11
```

## 5. Verify the Source Code

```bash
ls -lh code/
cat code/Day13.scala
```

The same application source is also available at:

```text
src/main/scala/Day13.scala
```

## 6. Verify the Input CSV

```bash
ls -lh input/
cat input/customers.csv
```

The input file is:

```text
input/customers.csv
```

## 7. Compile the Application

```bash
sbt clean compile
```

A successful build ends with:

```text
[success] Total time: ...
[success] ...
```

## 8. Run the Application

```bash
sbt "runMain Day13"
```

The application:

- Creates a SparkSession.
- Reads the CSV as a DataFrame.
- Displays the records.
- Prints the schema.
- Selects columns.
- Filters customers.
- Creates spending categories.
- Calculates loyalty points.
- Creates a temporary SQL view.
- Runs SQL analytics.
- Stops Spark.

## 9. Save Execution Output

```bash
sbt "runMain Day13" | tee output/result.txt
```

Check the file:

```bash
ls -lh output/result.txt
cat output/result.txt
```

## 10. Inspect Screenshots

Create the screenshots directory if required:

```bash
mkdir -p screenshots
```

Copy the screenshots from Windows/WSL:

```bash
cp "/mnt/c/Users/chint/OneDrive/图片/Screenshots/01-compilation-success.png.png" screenshots/01-compilation-success.png
cp "/mnt/c/Users/chint/OneDrive/图片/Screenshots/02-spark-sql-execution.png.png" screenshots/02-spark-sql-execution.png
cp "/mnt/c/Users/chint/OneDrive/图片/Screenshots/03-sql-analytics-result.png.png" screenshots/03-sql-analytics-result.png
```

Verify:

```bash
ls -lh screenshots/
```

## 11. Check Git Status

From the repository root:

```bash
cd ~/scala-spark-30-day-practice
git status --short
```

## 12. Confirm Build Artifacts Are Ignored

```bash
git status --ignored --short | grep -E 'target/|project/target/'
```

Entries beginning with `!!` are ignored files/directories and should not be committed.

## 13. Stage Day 13

```bash
git add Day-13-Spark-SQL-Basics
```

Review staged files:

```bash
git status --short
git diff --cached --stat
```

## 14. Commit

```bash
git commit -m "Add Day 13 Spark SQL Basics"
```

For a documentation-only update, use a message such as:

```bash
git commit -m "Complete Day 13 documentation"
```

## 15. Push

```bash
git push origin main
```

## 16. Final Verification

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

## 17. Useful Inspection Commands

Check the README:

```bash
cat README.md
```

Check troubleshooting:

```bash
cat troubleshooting/README.md
```

Check the project documentation:

```bash
cat project/README.md
```

Check tracked Day 13 files:

```bash
git ls-files Day-13-Spark-SQL-Basics
```
