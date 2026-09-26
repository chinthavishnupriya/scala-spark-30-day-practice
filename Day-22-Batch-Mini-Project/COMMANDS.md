# Day 22 — Commands

## Run

```bash
cd ~/scala-spark-30-day-practice/Day-22-Batch-Mini-Project
```

Compile:

```bash
sbt clean compile
```

Remove generated output before a fresh run:

```bash
rm -rf output/daily-sales output/spark-warehouse
```

Run and save execution output:

```bash
sbt run 2>&1 | tee output/day22-execution-output.txt
```

Check Parquet files:

```bash
find output/daily-sales -type f | sort
```

Check Git:

```bash
git status
git add .
git commit -m "Add Day 22 batch mini project"
git push origin main
```

## Expected Result

```text
Invalid records removed: 2
Clean transaction count: 10
Output record count: 10
DAY 22 COMPLETED SUCCESSFULLY
```

Generated `output/daily-sales/` and Spark warehouse files are ignored by Git.
