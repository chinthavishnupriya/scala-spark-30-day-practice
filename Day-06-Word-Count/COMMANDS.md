# Day 06 — Commands Used

## Navigate
```bash
cd ~/scala-spark-30-day-practice/Day-06-Word-Count
```

## Check files and input
```bash
find . -maxdepth 5 -type f | sort
cat input/application.log
sed -n '1,300p' src/main/scala/Day06.scala
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

## Inspect Word Count results
```bash
cat output/result.txt
tail -50 output/result.txt
grep -E "Classic|case-insensitive|Top 10|DAY 06|success" output/result.txt
```

## Copy source
```bash
cp src/main/scala/Day06.scala code/Day06.scala
```

## Screenshots
```bash
ls -lh screenshots/
```

## Git workflow
```bash
cd ~/scala-spark-30-day-practice
git status --short
git add Day-06-Word-Count
git commit -m "Complete Day 6 Word Count"
git push origin main
git status
git log --oneline -3
```

## If remote is ahead
```bash
git pull --rebase origin main
git push origin main
```
