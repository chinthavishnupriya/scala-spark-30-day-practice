# Day 03 — Commands Used

## Navigate
```bash
cd ~/scala-spark-30-day-practice/Day-03-Spark-Setup
```

## Create/check directories
```bash
mkdir -p code input output project screenshots src/main/scala troubleshooting
find . -maxdepth 5 -type f | sort
```

## Check configuration and source
```bash
cat build.sbt
cat project/build.properties
cat src/main/scala/Day03.scala
```

## Compile
```bash
sbt clean compile
```

## Run with 2 local cores
```bash
sbt "run 2"
sbt "run 2" > output/result_local2.txt 2>&1
```

## Run with 4 local cores
```bash
sbt "run 4"
sbt "run 4" > output/result_local4.txt 2>&1
```

## Verify Spark execution
```bash
grep -E "Spark version|Master|Default parallelism|Number of lines|DAY 03 APPLICATION COMPLETED|success" output/result_local2.txt
grep -E "Spark version|Master|Default parallelism|Number of lines|DAY 03 APPLICATION COMPLETED|success" output/result_local4.txt
```

## Copy source
```bash
cp src/main/scala/Day03.scala code/Day03.scala
```

## Screenshots
```bash
ls -lh screenshots/
cp "/mnt/c/Users/chint/OneDrive/图片/Screenshots/<file>.png.png" screenshots/<file>.png
```

## Git workflow
```bash
cd ~/scala-spark-30-day-practice
git status --short
git add Day-03-Spark-Setup
git commit -m "Complete Day 3 Spark Setup"
git push origin main
git status
git log --oneline -3
```
