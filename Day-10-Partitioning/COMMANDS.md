# Day 10 — Commands Used

## Navigate
```bash
cd ~/scala-spark-30-day-practice/Day-10-Partitioning
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

## Inspect partitioning results
```bash
cat output/result.txt
tail -70 output/result.txt
grep -E "Original partitions|Repartition|Coalesce|Partition|HashPartitioner|Revenue|Optimization|DAY 10|success" output/result.txt
```

## Copy source
```bash
cp src/main/scala/Day10.scala code/Day10.scala
```

## Verify screenshots
```bash
ls -lh screenshots/
```

## Copy screenshots from Windows to WSL
```bash
cp "/mnt/c/Users/chint/OneDrive/图片/Screenshots/01-partition-count-repartition.png.png" screenshots/01-partition-count-repartition.png
cp "/mnt/c/Users/chint/OneDrive/图片/Screenshots/02-partition-distribution-partitionby.png.png" screenshots/02-partition-distribution-partitionby.png
cp "/mnt/c/Users/chint/OneDrive/图片/Screenshots/03-optimization-success.png.png" screenshots/03-optimization-success.png
```

## Git workflow
```bash
cd ~/scala-spark-30-day-practice
git status --short
git add Day-10-Partitioning
git commit -m "Complete Day 10 partitioning"
git push origin main
git status
git log --oneline -3
```

## If remote is ahead
```bash
git pull --rebase origin main
git push origin main
```
