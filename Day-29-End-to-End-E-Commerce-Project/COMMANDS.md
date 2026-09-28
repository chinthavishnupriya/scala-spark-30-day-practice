# Day 29 Commands

## Enter project

    cd ~/scala-spark-30-day-practice/Day-29-End-to-End-E-Commerce-Project

## Check Environment

    java -version
    scala -version
    sbt --version

Expected environment:

    Java 17
    Scala 2.13.18
    sbt 1.10.11
    Spark 4.2.0

## Inspect Project Files

    find . -maxdepth 3 -type f | sort
    ls -lh input/
    ls -lh screenshots/

## Compile

    sbt clean compile

## Clean Build Artifacts

    sbt clean

## Compile


    sbt run

## Run


    mkdir -p output
    sbt run 2>&1 | tee output/day29-execution-output.txt

## Capture runtime evidence

    mkdir -p output
    sbt run 2>&1 | tee output/day29-execution-output.txt

## Inspect important runtime results

    grep -E "Raw orders|Products|Customers|Clean completed orders|Enriched rows" output/day29-execution-output.txt
    grep -E "CARD|CASH|UPI" output/day29-execution-output.txt
    grep -E "Customer Revenue Summary|Window: Orders|Enriched Category Summary" output/day29-execution-output.txt
    grep -E "Physical Plan|Pipeline Summary|Shuffle points" output/day29-execution-output.txt

## Inspect saved runtime output

    ls -lh output/day29-execution-output.txt
    head -20 output/day29-execution-output.txt

Do not type output/day29-execution-output.txt by itself: that attempts to execute the text file as a shell command.

## Verify source copies

    diff -u src/main/scala/Day29.scala code/Day29.scala

## Inspect important output

    grep -E "Pair RDD|Customer Revenue Summary|Window:|Category Summary|Physical Plan|Pipeline Summary" output/day29-execution-output.txt

## Git status

    git status --short

## Commit evidence

    git add screenshots/
    git commit -m "Add Day 29 execution evidence"
    git push origin main


## Verify the Java 17 Spark Module Fix

    grep "sun.util.calendar" build.sbt

Expected:

    "--add-opens=java.base/sun.util.calendar=ALL-UNNAMED"

## Check the Final Commit

    git status
    git log -5 --oneline

## Pull Latest Documentation

    git pull origin main

## Clean Working Tree Check

    git status

Expected:

    nothing to commit, working tree clean
