# Day 30 Commands

## 1. Enter Project
    cd ~/scala-spark-30-day-practice/Day-30-Final-E-Commerce-Capstone

## 2. Check Environment
    java -version
    scala -version
    sbt --version

Expected:
    Java 17
    Scala 2.13.18
    sbt 1.10.11
    Spark 4.2.0

## 3. Inspect Project
    find . -maxdepth 3 -type f | sort
    ls -lh input/

## 4. Compile
    sbt clean compile

## 5. Run Batch
    sbt "run batch"

## 6. Capture Batch Output
    mkdir -p output
    sbt "run batch" 2>&1 | tee output/day30-batch-output.txt

## 7. Inspect Batch Results
    grep -E "Raw orders|Clean completed orders|Enriched orders|Enriched partitions" output/day30-batch-output.txt
    grep -E "CARD|CASH|UPI" output/day30-batch-output.txt
    grep -E "Batch SQL|Batch Window|Batch Physical Plan" output/day30-batch-output.txt
    grep -E "broadcast|accumulator|cache|persist|repartition|Batch concepts" output/day30-batch-output.txt

## 8. Start Streaming
Terminal 1:
    nc -lk 9998

Terminal 2:
    sbt "run streaming"

## 9. Send Sample Events
Terminal 1:
    cat input/stream-events.txt | nc localhost 9998

## 10. Capture Streaming Output
    sbt "run streaming" 2>&1 | tee output/day30-streaming-output.txt

## 11. Run Complete Capstone
    sbt "run all"

## 12. Verify Source Copies
    diff -u src/main/scala/Day30.scala code/Day30.scala

No output means the source copies match.

## 13. Verify Build Configuration
    grep -E "scalaVersion|spark-core|spark-sql|spark-streaming" build.sbt

## 14. Verify Java 17 Module Options
    grep "add-opens" build.sbt

The Spark date-handling option should include:
    --add-opens=java.base/sun.util.calendar=ALL-UNNAMED

## 15. Evidence Screenshots
    mkdir -p screenshots
    ls -lh screenshots/

Recommended:
    01-compilation-success.png
    02-batch-started.png
    03-batch-results.png
    04-physical-plan.png
    05-streaming-state.png
    06-complete-capstone.png

## 16. Git Status
    git status --short

## 17. Commit Evidence
After successful execution and screenshots:
    git add screenshots/
    git add -f output/day30-batch-output.txt output/day30-streaming-output.txt
    git commit -m "Add Day 30 execution evidence"
    git push origin main

## 18. Final Verification
    git status
    git log -5 --oneline
Expected:
    nothing to commit, working tree clean
