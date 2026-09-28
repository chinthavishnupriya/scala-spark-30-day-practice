# Day 29 Commands

## 1. Enter Project

    cd ~/scala-spark-30-day-practice/Day-29-End-to-End-E-Commerce-Project

## 2. Check Environment

    java -version
    scala -version
    sbt --version

Expected environment:

    Java 17
    Scala 2.13.18
    sbt 1.10.11
    Spark 4.2.0

## 3. Inspect Project

    find . -maxdepth 3 -type f | sort
    ls -lh input/
    ls -lh screenshots/

## 4. Check Java 17 Spark Module Fix

    grep "sun.util.calendar" build.sbt

Expected:

    "--add-opens=java.base/sun.util.calendar=ALL-UNNAMED"

This option is required by the verified local Java 17 / Spark 4.2.0 Day 29 runtime.

## 5. Clean Build

    sbt clean

## 6. Compile

    sbt clean compile

## 7. Run

    sbt run

## 8. Capture Runtime Evidence

    mkdir -p output
    sbt run 2>&1 | tee output/day29-execution-output.txt

## 9. Verify Runtime Counts

    grep -E "Raw orders|Products|Customers|Clean completed orders|Enriched rows" output/day29-execution-output.txt

Expected:

    Raw orders : 12
    Products   : 5
    Customers  : 4
    Clean completed orders: 11
    Enriched rows: 11

## 10. Verify Pair RDD Revenue

    grep -E "CARD|CASH|UPI" output/day29-execution-output.txt

Expected:

    CARD -> ₹7100.00
    CASH  -> ₹800.00
    UPI   -> ₹4600.00

## 11. Verify Customer Summary

    grep -A8 "Customer Revenue Summary" output/day29-execution-output.txt

Expected customer revenue:

    C001 -> ₹4600.00
    C002 -> ₹3800.00
    C003 -> ₹1200.00
    C004 -> ₹2900.00

## 12. Verify Window and Category Results

    grep -E "Window: Orders|Enriched Category Summary|Electronics|Stationery" output/day29-execution-output.txt

Expected category totals:

    Electronics -> ₹7400.00
    Stationery  -> ₹5100.00

## 13. Verify Physical Plan and Pipeline Summary

    grep -E "Physical Plan|Exchange|BroadcastExchange|InMemory|Pipeline Summary|Shuffle points" output/day29-execution-output.txt

Look for evidence of:

    Exchange
    BroadcastExchange
    InMemoryRelation / InMemoryTableScan
    repartition / hashpartitioning
    AdaptiveSparkPlan
    ShuffleQueryStage

## 14. Inspect Complete Runtime Log

    ls -lh output/day29-execution-output.txt
    head -20 output/day29-execution-output.txt
    tail -20 output/day29-execution-output.txt

Do not type:

    output/day29-execution-output.txt

by itself. That attempts to execute the text file as a shell command.

## 15. Verify Source Copies

    diff -u src/main/scala/Day29.scala code/Day29.scala

No output means both source copies match.

## 16. Verify Screenshots

    ls -lh screenshots/

Expected:

    01-compilation-success.png
    02-runtime-started.png
    03-pair-rdd-revenue.png
    04-dataframe-summary.png
    05-window-and-category.png
    06-physical-plan-complete.png

## 17. Git Status

    git status --short

## 18. Commit Screenshot Evidence

    git add screenshots/
    git commit -m "Add Day 29 execution evidence"
    git push origin main

## 19. Add Runtime Output When Required

The project .gitignore intentionally ignores output/. If the verified runtime output must be committed, use:

    git add -f output/day29-execution-output.txt
    git commit -m "Add Day 29 runtime output"
    git push origin main

## 20. Final Repository Verification

    git status
    git log -5 --oneline

Expected:

    Your branch is up to date with 'origin/main'.
    nothing to commit, working tree clean

## 21. Pull Latest Repository Changes

    git pull origin main

After pulling:

    git status

The Day 29 project should remain clean and synchronized with origin/main.
