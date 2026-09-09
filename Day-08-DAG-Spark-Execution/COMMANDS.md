# Day 08 — Commands Used

## Navigate
```bash
cd ~/scala-spark-30-day-practice/Day-08-DAG-Spark-Execution
```

## Check project and input
```bash
find . -maxdepth 5 -type f | sort
cat build.sbt
cat .jvmopts
cat input/sales.txt
```

## Compile
```bash
sbt clean compile
```

## Run and save output
```bash
sbt run
sbt run > output/result.txt 2>&1
```

## Inspect DAG/stage analysis and results
```bash
cat output/result.txt
tail -60 output/result.txt
grep -E "DAG|stage|shuffle|narrow|wide|partition|DAY 08|success" output/result.txt
```

## Copy source
```bash
cp src/main/scala/Day08.scala code/Day08.scala
```

## Screenshots
```bash
ls -lh screenshots/
```

## Git workflow
```bash
cd ~/scala-spark-30-day-practice
git status --short
git add Day-08-DAG-Spark-Execution
git commit -m "Complete Day 8 DAG and Spark execution"
git push origin main
git status
git log --oneline -3
```

## If remote is ahead
```bash
git pull --rebase origin main
git push origin main
```
