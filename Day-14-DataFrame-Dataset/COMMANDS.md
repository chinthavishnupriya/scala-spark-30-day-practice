# Day 14 – Commands Reference

## 1. Navigate to the project

```bash
cd ~/scala-spark-30-day-practice/Day-14-DataFrame-Dataset
```

## 2. Check project files

```bash
find . -maxdepth 3 -type f | sort
```

## 3. Check Java

```bash
java -version
```

Expected: OpenJDK 17.0.20.

## 4. Check Scala

```bash
scala -version
```

The standalone Scala runner may show 2.12.20. The SBT project uses Scala 2.13.18.

## 5. Check SBT

```bash
sbt --version
```

## 6. Check Spark

```bash
spark-submit --version
```

Expected Spark version: 4.2.0.

## 7. Inspect build configuration

```bash
cat build.sbt
cat project/build.properties
cat .jvmopts
```

## 8. Inspect source and input

```bash
cat src/main/scala/Day14.scala
cat code/Day14.scala
cat input/employees.csv
```

## 9. Count input records

```bash
tail -n +2 input/employees.csv | wc -l
```

Expected:

```text
8
```

## 10. Compile

```bash
sbt compile
```

## 11. Clean and compile

```bash
sbt clean compile
```

## 12. Run

```bash
sbt run
```

## 13. Save execution output

```bash
sbt run > output/result.txt 2>&1
```

## 14. Read saved output

```bash
cat output/result.txt
```

## 15. Check important results

```bash
grep -E "DataFrame Record Count|Dataset Type|Department Payroll|Engineering|Finance|HR|Catalyst|SUCCESSFULLY" output/result.txt
```

## 16. Check output and screenshots

```bash
ls -lh output/
ls -lh screenshots/
```

Expected screenshots:

```text
01-compilation-success.png
02-dataframe-dataset-execution.png
03-payroll-catalyst-result.png
```

## 17. Check documentation size

```bash
wc -l README.md COMMANDS.md
```

## 18. Review documentation changes

```bash
git diff -- README.md COMMANDS.md
git diff --stat
```

## 19. Check Git status

```bash
git status
```

## 20. Stage documentation

```bash
git add README.md COMMANDS.md
```

## 21. Verify staged changes

```bash
git status
```

Only `README.md` and `COMMANDS.md` should be staged for the documentation correction.

## 22. Commit

```bash
git commit -m "Complete Day 14 documentation and commands"
```

## 23. Push

```bash
git push origin main
```

## 24. Final Git verification

```bash
git status
git log --oneline -5
```

Expected working-tree result:

```text
nothing to commit, working tree clean
```

## 25. Final Day 14 execution verification

```bash
sbt clean compile
sbt run > output/result.txt 2>&1
grep -E "DataFrame Record Count|Dataset Type|Engineering|Finance|HR|Catalyst|SUCCESSFULLY" output/result.txt
git status
```

## Complete Day 14 Workflow

```bash
cd ~/scala-spark-30-day-practice/Day-14-DataFrame-Dataset
java -version
scala -version
sbt --version
spark-submit --version
cat build.sbt
cat project/build.properties
cat .jvmopts
cat src/main/scala/Day14.scala
cat input/employees.csv
sbt clean compile
sbt run > output/result.txt 2>&1
cat output/result.txt
ls -lh output/
ls -lh screenshots/
git status
git diff --stat
git add README.md COMMANDS.md
git commit -m "Complete Day 14 documentation and commands"
git push origin main
git status
```
