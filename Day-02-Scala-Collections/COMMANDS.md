# Day 02 — Commands Used

## Navigate
```bash
cd ~/scala-spark-30-day-practice/Day-02-Scala-Collections
```

## Check project
```bash
find . -maxdepth 5 -type f | sort
cat build.sbt
cat project/build.properties
```

## Compile and run
```bash
sbt clean compile
sbt run
sbt run > output/result.txt 2>&1
```

## Inspect results
```bash
cat output/result.txt
tail -40 output/result.txt
```

## Copy source
```bash
cp src/main/scala/Day02.scala code/Day02.scala
```

## Git workflow
```bash
cd ~/scala-spark-30-day-practice
git status --short
git add Day-02-Scala-Collections
git commit -m "Complete Day 2 Scala Collections"
git push origin main
git status
git log --oneline -3
```

## If remote has newer commits
```bash
git pull --rebase origin main
git push origin main
```

## Screenshot copy from Windows to WSL
```bash
cp "/mnt/c/Users/chint/OneDrive/图片/Screenshots/<file>.png.png" Day-02-Scala-Collections/screenshots/<file>.png
```
