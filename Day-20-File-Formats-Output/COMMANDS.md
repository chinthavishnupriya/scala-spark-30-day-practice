# Day 20 - Commands

## Enter the Project

```bash
cd ~/scala-spark-30-day-practice/Day-20-File-Formats-Output
```

## Compile

```bash
sbt clean compile
```

## Run the Spark Application

```bash
sbt run 2>&1 | tee output/day20-execution-output.txt
```

## Inspect Partitioned Output

```bash
find output/day20_partitioned_parquet -type f | sort
```

## Inspect Project Files

```bash
find . -maxdepth 3 -type f | sort
```

## Check Git Status

```bash
git status --short
```

## Stage the Project

```bash
git add .
```

## Review Staged Files

```bash
git status
```

## Commit

```bash
git commit -m "Add Day 20 file formats and output practice"
```

## Push

```bash
git push origin main
```

## Important Spark Operations

### Read CSV

```scala
spark.read
  .option("header", "true")
  .option("inferSchema", "true")
  .csv(inputPath)
```

### Write CSV

```scala
salesDF.write
  .mode("overwrite")
  .option("header", "true")
  .csv(csvOutput)
```

### Write JSON

```scala
salesDF.write
  .mode("overwrite")
  .json(jsonOutput)
```

### Write Parquet

```scala
salesDF.write
  .mode("overwrite")
  .parquet(parquetOutput)
```

### Repartition

```scala
datedSalesDF.repartition(
  3,
  col("year"),
  col("month"),
  col("day")
)
```

### Partitioned Parquet

```scala
repartitionedDF.write
  .mode("overwrite")
  .partitionBy("year", "month", "day")
  .parquet(partitionedOutput)
```

### Explain Plan

```scala
datedSalesDF.explain(true)
```
