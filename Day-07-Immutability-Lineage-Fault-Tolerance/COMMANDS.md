# Day 07 — Commands Used

## Navigate
```bash
cd ~/scala-spark-30-day-practice/Day-07-Immutability-Lineage-Fault-Tolerance
```

## Check project
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

## Run
```bash
sbt run
```

## Save execution output
```bash
sbt run > output/result.txt 2>&1
```

## Inspect lineage, revenue and success
```bash
cat output/result.txt
grep -E "lineage|partitions|revenue|fault tolerance|DAY 07|success" output/result.txt
```

## Copy source
```bash
cp src/main/scala/Day07.scala code/Day07.scala
```

## Screenshots
```bash
ls -lh screenshots/
```

## Git workflow
```bash
cd ~/scala-spark-30-day-practice
git status --short
git add Day-07-Immutability-Lineage-Fault-Tolerance
git commit -m "Complete Day 7 immutability lineage and fault tolerance"
git push origin main
git status
git log --oneline -3
```

## If remote is ahead
```bash
git pull --rebase origin main
git push origin main
```
