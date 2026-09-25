# Day 17 — Commands

## 1. Enter the project

```bash
cd ~/scala-spark-30-day-practice/Day-17-Window-Functions
```

## 2. Check the structure

```bash
find . -maxdepth 3 -type f | sort
```

## 3. Check input files

```bash
ls -lh input/
head input/students.csv
head input/customer_policies.csv
```

## 4. Compile

```bash
sbt clean compile
```

Expected result:

```text
[success] Total time...
```

## 5. Run the Spark program

```bash
sbt run
```

## 6. Save execution output

```sbt run 2>&1 | tee output/day17-execution-output.txt```

## 7. Check important output sections

```bash
grep -n "ROW_NUMBER / RANK / DENSE_RANK" output/day17-execution-output.txt
grep -n "TOP 3 STUDENTS PER COURSE" output/day17-execution-output.txt
grep -n "RANKING WITH SCORE TIES" output/day17-execution-output.txt
grep -n "POLICY HISTORY WITH LAG / LEAD" output/day17-execution-output.txt
grep -n "LATEST POLICY PER CUSTOMER" output/day17-execution-output.txt
grep -n "PREMIUM CHANGE USING LAG" output/day17-execution-output.txt
grep -n "SPARK SQL" output/day17-execution-output.txt
grep -n "EXECUTION PLAN" output/day17-execution-output.txt
grep -n "DAY 17 COMPLETED SUCCESSFULLY" output/day17-execution-output.txt
```

## 8. Verify source copies

```diff
diff -u src/main/scala/Day17.scala code/Day17.scala
```

No output means the two source copies match.

## 9. Verify screenshots

```bash
ls -lh screenshots/
file screenshots/*.png
```

Expected screenshots:

- `01-compilation-success.png`
- `02-ranking-result.png`
- `03-lag-lead-latest-policy.png`
- `04-execution-plan.png`

## 10. Check Git status

Run from the repository root:

```bash
cd ~/scala-spark-30-day-practice
git status --short
```

## 11. Stage Day 17

```bash
git add Day-17-Window-Functions/
git status --short
```

## 12. Review staged changes

```bash
git diff --cached --stat
git diff --cached -- Day-17-Window-Functions/README.md
git diff --cached -- Day-17-Window-Functions/COMMANDS.md
```

## 13. Commit

```bash
git commit -m "Complete Day 17 window functions documentation"
```

## 14. Push

```bash
git push origin main
```

If Git reports that the remote is ahead:

```bash
git pull --rebase origin main
git push origin main
```

## 15. Final verification

```bash
git status
git log -1 --oneline
```

The working tree should be clean and the latest commit should contain the Day 17 documentation.
