# Day 01 — Commands Used

## Navigate to project
```bash
cd ~/scala-spark-30-day-practice/Day-01-Scala-Essentials
```

## Check project files
```bash
ls -la
find . -maxdepth 5 -type f | sort
cat build.sbt
cat project/build.properties
```

## Compile
```bash
sbt clean compile
```

## Run
```bash
sbt run
```

## Save output
```bash
sbt run > output/result.txt 2>&1
cat output/result.txt
```

## Copy source to code folder
```bash
cp src/main/scala/Day01.scala code/Day01.scala
```

## Check README and source
```bash
cat README.md
sed -n '1,240p' src/main/scala/Day01.scala
```

## Git workflow
```bash
cd ~/scala-spark-30-day-practice
git status --short
git add Day-01-Scala-Essentials
git commit -m "Complete Day 1 Scala Essentials"
git push origin main
git status
git log --oneline -3
```

## Screenshot from Windows to WSL
```bash
cp "/mnt/c/Users/chint/OneDrive/图片/Screenshots/<file>.png.png" screenshots/<file>.png
```
