# Day 09 — Commands Used

## Navigate
```bash
cd ~/scala-spark-30-day-practice/Day-09-Pair-RDD
```

## Check project and input
```bash
find . -maxdepth 5 -type f | sort
cat build.sbt
cat .jvmopts
cat input/transactions.txt
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

## Inspect Pair RDD results
```bash
cat output/result.txt
tail -60 output/result.txt
grep -E "reduceByKey|groupByKey|mapValues|Account|Books|Clothing|Electronics|DAY 09|success" output/result.txt
```

## Copy source
```bash
cp src/main/scala/Day09.scala code/Day09.scala
```

## Screenshots
```bash
ls -lh screenshots/
```

## Git workflow
```bash
cd ~/scala-spark-30-day-practice
git status --short
git add Day-09-Pair-RDD
git commit -m "Complete Day 9 Pair RDD"
git push origin main
git status
git log --oneline -3
```

## If remote is ahead
```bash
git pull --rebase origin main
git push origin main
```
