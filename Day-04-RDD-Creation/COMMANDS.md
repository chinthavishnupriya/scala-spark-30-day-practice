# Day 04 — Commands Used

## Navigate and create directories
```bash
cd ~/scala-spark-30-day-practice
mkdir -p Day-04-RDD-Creation/{code,input,output,project,screenshots,troubleshooting,src/main/scala}
cd Day-04-RDD-Creation
```

## Check project configuration
```bash
cat build.sbt
cat project/build.properties
cat .gitignore
```

## Check input and source
```bash
cat input/customers.txt
cat input/sales.txt
sed -n '1,260p' src/main/scala/Day04.scala
```

## Copy source
```bash
cp src/main/scala/Day04.scala code/Day04.scala
```

## Compile and run
```bash
sbt clean compile
sbt run
sbt run 2>&1 | tee output/day04_run.txt
```

## Inspect results and partitions
```bash
cat output/day04_run.txt
tail -40 output/day04_run.txt
```

## Verify project files
```bash
find . -maxdepth 5 -type f | sort
```

## Git workflow
```bash
cd ~/scala-spark-30-day-practice
git status --short
git add Day-04-RDD-Creation
git commit -m "Complete Day 4 RDD Creation and Operations"
git push origin main
git status
git log --oneline -3
```

## Git ignore troubleshooting
```bash
cat -n Day-04-RDD-Creation/.gitignore
git status --ignored --short
git status --short
```
