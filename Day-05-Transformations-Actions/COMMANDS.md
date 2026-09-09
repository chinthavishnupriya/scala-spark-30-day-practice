# Day 05 — Commands Used

## Navigate
```bash
cd ~/scala-spark-30-day-practice/Day-05-Transformations-Actions
```

## Check project
```bash
find . -maxdepth 5 -type f | sort
cat build.sbt
cat .jvmopts
cat .gitignore
```

## Check source and input
```bash
sed -n '1,300p' src/main/scala/Day05.scala
cat input/application.log
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

## Inspect execution
```bash
cat output/result.txt
tail -50 output/result.txt
grep -E "ERROR|count|distinct|DAY 05|success" output/result.txt
```

## Copy source
```bash
cp src/main/scala/Day05.scala code/Day05.scala
```

## Verify screenshots
```bash
ls -lh screenshots/
```

## Git workflow
```bash
cd ~/scala-spark-30-day-practice
git status --short
git add Day-05-Transformations-Actions
git commit -m "Complete Day 5 Transformations and Actions"
git push origin main
git status
git log --oneline -3
```

## If remote is ahead
```bash
git pull --rebase origin main
git push origin main
```
