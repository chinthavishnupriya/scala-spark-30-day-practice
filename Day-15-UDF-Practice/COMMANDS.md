# Day 15 – UDF Practice Commands

## 1. Navigate to the project

```bash
cd ~/scala-spark-30-day-practice/Day-15-UDF-Practice
```

Verify:

```bash
pwd
```

---

## 2. Inspect the project structure

```bash
find . -maxdepth 3 -type f | sort
```

---

## 3. Check the input CSV

```bash
cat input/customers.csv
```

View the first rows:

```bash
head input/customers.csv
```

Count data records:

```bash
tail -n +2 input/customers.csv | wc -l
```

Expected:

```text
8
```

---

## 4. Inspect the Scala source

```bash
cat src/main/scala/Day15.scala
```

Also verify the source copy:

```bash
cat code/Day15.scala
```

---

## 5. Compile the project

```bash
sbt compile
```

Expected:

```text
[success]
```

---

## 6. Run the application

```bash
sbt run
```

The application performs CSV loading, UDF classification, built-in comparison, SQL UDF execution, aggregation and execution-plan inspection.

---

## 7. Save the complete execution output

```bash
sbt run 2>&1 | tee output/day15-execution-output.txt
```

This displays the output and saves a copy to:

```text
output/day15-execution-output.txt
```

---

## 8. Read the saved output

```bash
cat output/day15-execution-output.txt
```

View the last 50 lines:

```bash
tail -n 50 output/day15-execution-output.txt
```

---

## 9. Verify UDF comparison

```bash
grep -n "Comparison mismatches" output/day15-execution-output.txt
```

Expected:

```text
Comparison mismatches: 0
```

Verify the success message:

```bash
grep -n "UDF and built-in results match" output/day15-execution-output.txt
```

---

## 10. Verify SQL UDF execution

```bash
grep -n "REGISTERED SQL UDF RESULT" output/day15-execution-output.txt
```

---

## 11. Verify risk summary

```bash
grep -n "RISK CATEGORY SUMMARY" output/day15-execution-output.txt
```

Expected categories:

```text
High Risk
Medium Risk
Low Risk
```

---

## 12. Verify the execution plan

```bash
grep -n "EXECUTION PLAN" output/day15-execution-output.txt
```

The application also calls:

```scala
result.explain(true)
```

---

## 13. Verify successful completion

```bash
grep -n "DAY 15 COMPLETED SUCCESSFULLY" output/day15-execution-output.txt
```

---

## 14. Check screenshots

```bash
ls -lh screenshots/
```

Expected:

```text
01-compilation-success.png
02-udf-result.png
03-udf-vs-builtin.png
04-sql-execution-plan.png
```

---

## 15. Check troubleshooting documentation

```bash
cat troubleshooting/README.md
```

---

## 16. Check ignored build artifacts

```bash
git status --ignored --short
```

Expected ignored directories include:

```text
!! target/
!! project/target/
```

These generated files should not be committed.

---

## 17. Check Git status

```bash
git status --short
```

---

## 18. Stage the documentation

```bash
git add README.md COMMANDS.md
```

If other Day 15 files were modified, stage them separately:

```bash
git add output/day15-execution-output.txt screenshots troubleshooting
```

---

## 19. Review staged changes

```bash
git status --short
```

Review the documentation diff:

```bash
git diff --cached -- README.md COMMANDS.md
```

---

## 20. Commit the expanded documentation

```bash
git commit -m "Expand Day 15 UDF documentation"
```

---

## 21. Push to GitHub

```bash
git push origin main
```

---

## 22. Verify repository status

```bash
git status
```

Expected:

```text
Your branch is up to date with 'origin/main'.
nothing to commit, working tree clean
```

---

## 23. Verify the latest commit

```bash
git log -1 --oneline
```

---

## 24. Verify tracked Day 15 files

```bash
git ls-files Day-15-UDF-Practice
```

---

## 25. Final Day 15 verification sequence

```bash
cd ~/scala-spark-30-day-practice/Day-15-UDF-Practice
sbt compile
sbt run 2>&1 | tee output/day15-execution-output.txt
grep -n "Comparison mismatches" output/day15-execution-output.txt
grep -n "DAY 15 COMPLETED SUCCESSFULLY" output/day15-execution-output.txt
ls -lh screenshots/
git status --short
```

Expected key results:

```text
Comparison mismatches: 0
DAY 15 COMPLETED SUCCESSFULLY
```

---

## 26. GitHub documentation update sequence

After verifying the files:

```bash
git add README.md COMMANDS.md
git commit -m "Expand Day 15 UDF documentation"
git push origin main
git status
```

The final working tree should be clean.
