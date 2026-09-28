# Day 29 Commands

## Enter project

    cd ~/scala-spark-30-day-practice/Day-29-End-to-End-E-Commerce-Project

## Compile

    sbt clean compile

## Run

    sbt run

## Capture runtime evidence

    mkdir -p output
    sbt run 2>&1 | tee output/day29-execution-output.txt

## Verify source copies

    diff -u src/main/scala/Day29.scala code/Day29.scala

## Inspect important output

    grep -E "Pair RDD|Customer Revenue Summary|Window:|Category Summary|Physical Plan|Pipeline Summary" output/day29-execution-output.txt

## Git status

    git status --short

## Commit evidence

    git add output/ screenshots/
    git commit -m "Add Day 29 execution evidence"
    git push origin main
