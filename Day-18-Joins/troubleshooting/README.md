# Day 18 - Troubleshooting

## 1. BroadcastHashJoin appears instead of SortMergeJoin

Small sample datasets may trigger Spark's automatic broadcast join. The Day 18 program disables automatic broadcast joins with spark.conf.set("spark.sql.autoBroadcastJoinThreshold", -1). Rerun the application and grep for SortMergeJoin.

## 2. SortMergeJoin and Exchange

Sort-merge joins require data to be partitioned by the join key. The physical plan should show Exchange hashpartitioning and SortMergeJoin.

## 3. Hostname warning

A hostname-to-loopback warning can occur in WSL. It does not prevent the local Spark job from completing when the application starts and finishes successfully.

## 4. Native Hadoop warning

Spark may report that the native Hadoop library is unavailable. For this local practice project, the warning does not prevent the DataFrame operations from running.

## 5. Missing input files

Verify the input directory contains customers.csv, orders.csv, and payments.csv.

    ls -lh input/

## 6. Compilation failure

Run sbt clean compile and check Scala 2.13.18 and Spark 4.2.0 in build.sbt.

## 7. Output verification

    grep -n "DAY 18 COMPLETED" output/day18-execution-output.txt
    grep -n "SortMergeJoin" output/day18-execution-output.txt

A successful run should contain the completion marker and SortMergeJoin entries.
