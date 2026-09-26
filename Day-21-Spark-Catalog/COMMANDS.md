# Day 21 Commands

## Navigate to the project

```bash
cd ~/scala-spark-30-day-practice/Day-21-Spark-Catalog
```

## Compile

```bash
sbt clean compile
```

## Run

```bash
sbt run 2>&1 | tee output/day21-execution-output.txt
```

## Inspect the execution output

```bash
cat output/day21-execution-output.txt
```

Focused sections:

```bash
sed -n '70,135p' output/day21-execution-output.txt
sed -n '136,196p' output/day21-execution-output.txt
tail -n 15 output/day21-execution-output.txt
```

## Inspect project files

```bash
find . -maxdepth 3 -type f | sort
```

## Check Git status

```bash
git status --short --untracked-files=all
```

## Git workflow

From inside Day-21-Spark-Catalog:

```bash
git add .
git commit -m "Add Day 21 Spark Catalog practice"
git push origin main
git status
```

## Main Spark SQL operations used

```sql
CREATE DATABASE IF NOT EXISTS hotel_analytics;
SHOW DATABASES;
USE hotel_analytics;
SHOW TABLES;
DESCRIBE hotel_bookings;
DESCRIBE EXTENDED hotel_bookings;
```

## Catalog API operations used

The Scala implementation uses:

- `spark.catalog.currentDatabase`
- `spark.catalog.listTables()`
- `spark.catalog.listColumns("hotel_bookings")`
- `spark.catalog.getTable("hotel_bookings").tableType`
- `spark.catalog.getTable("hotel_bookings_view").tableType`
- `spark.catalog.tableExists("hotel_bookings")`
- `spark.catalog.tableExists("hotel_bookings_view")`

## Generated Spark warehouse

The application uses:

```
output/spark-warehouse/
```

This is generated runtime data and is excluded by `.gitignore`.
