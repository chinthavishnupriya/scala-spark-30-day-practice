# Day 18 - Commands

## Navigate

    cd ~/scala-spark-30-day-practice/Day-18-Joins

## Compile

    sbt clean compile

## Run and save output

    sbt run 2>&1 | tee output/day18-execution-output.txt

## Verify completion

    grep -n "DAY 18 COMPLETED" output/day18-execution-output.txt

## Verify Sort-Merge Join

    grep -n "SortMergeJoin" output/day18-execution-output.txt
    grep -n "Exchange hashpartitioning" output/day18-execution-output.txt

## Inspect files

    find . -maxdepth 3 -type f | sort
    ls -lh screenshots/

## Copy source

    cp src/main/scala/Day18.scala code/Day18.scala

## Git

    cd ~/scala-spark-30-day-practice
    git status --short
    git add Day-18-Joins
    git commit -m "Add Day 18 documentation and troubleshooting"
    git push origin main

## Expected screenshots

- 01-compilation-success.png
- 02-join-results.png
- 03-null-and-full-join.png
- 04-sortmerge-execution-plan.png

## Important

The sample datasets are small, so Spark can normally choose a BroadcastHashJoin. Day 18 disables spark.sql.autoBroadcastJoinThreshold so the physical plan demonstrates SortMergeJoin and its shuffle Exchange stages.
