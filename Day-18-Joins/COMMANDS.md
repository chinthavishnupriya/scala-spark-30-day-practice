# Day 18 - Commands

## 1. Navigate to the repository

    cd ~/scala-spark-30-day-practice
    cd Day-18-Joins

## 2. Verify project structure

    find . -maxdepth 3 -type f | sort
    ls -lh input/
    ls -lh screenshots/

Expected input files:
- input/customers.csv
- input/orders.csv
- input/payments.csv

Expected screenshots:
- screenshots/01-compilation-success.png
- screenshots/02-join-results.png
- screenshots/03-null-and-full-join.png
- screenshots/04-sortmerge-execution-plan.png

## 3. Check Scala and Java

    scala -version
    java -version
    sbt --version

The project build uses Scala 2.13.18, Spark 4.2.0, SBT 1.10.11, and Java 17.

## 4. Inspect the source

    sed -n '1,260p' src/main/scala/Day18.scala
    diff -u src/main/scala/Day18.scala code/Day18.scala

The program demonstrates inner, left, right, full outer, two-table payment, three-table customer-order-payment, and Spark SQL joins.

## 5. Compile the project

    sbt clean compile

Compilation should finish successfully before running the application.

## 6. Run the Spark application

    sbt run

This runs the Scala Spark application using the project configuration in build.sbt.

## 7. Save the complete execution output

    sbt run 2>&1 | tee output/day18-execution-output.txt

The output file preserves the join results, null handling, SQL result, execution plan, warnings, and completion marker.

## 8. Verify the completion marker

    grep -n "DAY 18 COMPLETED" output/day18-execution-output.txt

## 9. Verify record counts

    grep -n "Customer records\|Order records\|Payment records" output/day18-execution-output.txt

The sample contains 5 customers, 5 orders, and 4 payments.

## 10. Verify join sections

    grep -n "INNER JOIN\|LEFT JOIN\|RIGHT JOIN\|FULL OUTER JOIN" output/day18-execution-output.txt
    grep -n "CUSTOMER + ORDER + PAYMENT\|SPARK SQL" output/day18-execution-output.txt

## 11. Verify NULL handling

    grep -n "Unknown Customer\|No payment\|NULL\|coalesce" output/day18-execution-output.txt

The left join keeps customers without matching orders. The payment left join keeps orders without matching payments.

## 12. Verify Sort-Merge Join

    grep -n "SortMergeJoin" output/day18-execution-output.txt
    grep -n "Exchange hashpartitioning" output/day18-execution-output.txt

The program disables automatic broadcast joins so the physical plan can demonstrate SortMergeJoin and shuffle Exchange stages.

## 13. Inspect the physical plan

    grep -n -A30 -B10 "SortMergeJoin" output/day18-execution-output.txt

Look for:
- SortMergeJoin
- Exchange hashpartitioning
- Sort
- FileScan
- LeftOuter or other join types

## 14. Check broadcast configuration

    grep -n "autoBroadcastJoinThreshold" src/main/scala/Day18.scala

The configuration is intentionally set to -1 for this practical demonstration. Without that setting, the tiny sample datasets can produce BroadcastHashJoin instead.

## 15. Copy the maintained source to code/

    cp src/main/scala/Day18.scala code/Day18.scala
    diff -u src/main/scala/Day18.scala code/Day18.scala

Both source copies should match.

## 16. Re-run after source changes

    sbt clean compile
    sbt run 2>&1 | tee output/day18-execution-output.txt

## 17. Check Git status

    cd ~/scala-spark-30-day-practice
    git status --short

Do not add target/ or generated build files because .gitignore handles them.

## 18. Review Day 18 files before commit

    git status --short
    git diff -- Day-18-Joins/README.md
    git diff -- Day-18-Joins/COMMANDS.md
    git diff -- Day-18-Joins/code/Day18.scala

## 19. Stage Day 18

    git add Day-18-Joins
    git status --short

## 20. Commit Day 18

    git commit -m "Add Day 18 Spark joins practice"

If only documentation was changed later, use a separate descriptive commit message.

## 21. Sync with GitHub before pushing

    git pull --rebase origin main

This avoids the non-fast-forward problem when GitHub has newer documentation commits.

## 22. Push to GitHub

    git push origin main

## 23. Final verification

    git status
    git log --oneline -5

The working tree should be clean and main should be synchronized with origin/main.

## 24. GitHub verification checklist

Confirm Day-18-Joins contains:
- README.md
- COMMANDS.md
- build.sbt
- .jvmopts
- .gitignore
- code/Day18.scala
- src/main/scala/Day18.scala
- project/build.properties
- input/customers.csv
- input/orders.csv
- input/payments.csv
- output/day18-execution-output.txt
- four screenshots
- troubleshooting/README.md

## 25. Practical completion checklist

- [ ] Project compiles successfully
- [ ] Application runs successfully
- [ ] Inner join tested
- [ ] Left join tested
- [ ] Right join tested
- [ ] Full outer join tested
- [ ] NULL handling tested
- [ ] Customer-order-payment scenario tested
- [ ] Spark SQL join tested
- [ ] SortMergeJoin verified
- [ ] Exchange/shuffle stages verified
- [ ] Execution output saved
- [ ] Screenshots added
- [ ] README.md complete
- [ ] COMMANDS.md complete
- [ ] Troubleshooting documentation added
- [ ] Changes pushed to GitHub
