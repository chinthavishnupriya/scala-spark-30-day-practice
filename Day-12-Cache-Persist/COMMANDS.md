# Day 12 - Commands

## Enter the project
```bash
cd ~/scala-spark-30-day-practice/Day-12-Cache-Persist
```

## Check files
```bash
find . -maxdepth 5 -type f | sort
```

## Compile
```bash
sbt clean compile
```

## Run
```bash
sbt run
```

## Clean build output
```bash
sbt clean
```

## Check versions
```bash
java -version
sbt --version
scala -version
```

## Inspect input and output
```bash
cat input/transactions.txt
cat output/result.txt
```

## Git commands
```bash
git status
git add .
git commit -m "Complete Day 12 cache and persist"
git push origin main
```

## Pull latest repository changes
```bash
git pull origin main
```

## Notes
- Run `sbt clean compile` before the first test after a source or build configuration change.
- Run `sbt run` to execute the Spark application.
- The application uses Spark 4.2.0 and Scala 2.13.18.
- `cache()` uses the default `MEMORY_ONLY` persistence level.
- `persist()` allows an explicit storage level such as `MEMORY_AND_DISK`.
