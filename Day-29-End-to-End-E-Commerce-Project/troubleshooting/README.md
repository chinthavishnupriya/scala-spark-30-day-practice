# Day 29 Troubleshooting

## Compilation

Run:

    sbt clean compile

Verify Java, Scala, and sbt versions:

    java -version
    scala -version
    sbt --version

Expected versions are Java 17, Scala 2.13.18, sbt 1.10.11, and Spark 4.2.0.

## Java 17 ZoneInfo Access Error

If runtime fails with an exception mentioning IllegalAccessException, sun.util.calendar.ZoneInfo, or SparkDateTimeUtils.toJavaDate, verify that build.sbt contains:

    "--add-opens=java.base/sun.util.calendar=ALL-UNNAMED"

This option was required by the successful local Java 17 / Spark 4.2.0 Day 29 run for date processing. After changing the build configuration, rerun:

    sbt clean compile
    sbt run

## Input Path

The program expects:

    input/orders.csv
    input/products.csv
    input/customers.csv

Run from the Day 29 project directory. Check with:

    pwd
    ls -lh input/

## Empty Results

The cleaning stage keeps only COMPLETED orders with positive quantity and unit price. The sample CANCELLED order is intentionally excluded.

## UDF

The UDF only normalizes legacy category names. Do not replace ordinary parsing, filtering, arithmetic, or aggregation with UDFs when built-in Spark functions are available.

## Join Issues

Orders join products on product_id and customers on customer_id. Check the reference keys if rows disappear after enrichment.

## Window Results

The analytical window is partitioned by customer_id and ordered by revenue descending, then order_date ascending. Each customer's ranking therefore starts at 1.

## Shuffle and Performance

Expected shuffle-producing or shuffle-prone operations include reduceByKey, repartition, joins, groupBy aggregations, and window partitioning.

The project persists reused DataFrames. The sample is small enough for local execution.

## Physical Plan

customerSummary.explain("formatted") prints the physical plan. Look for Exchange operators when explaining shuffle boundaries.

## Re-run

This is a batch project, so no streaming checkpoint reset is required. Run:

    sbt clean compile
    sbt run

## Driver Output

collect is used only after Pair RDD aggregation reduces the result to a tiny number of payment methods. Avoid collecting large datasets in production.

## Source Synchronization

Run:

    diff -u src/main/scala/Day29.scala code/Day29.scala

No output means the two source copies match.

## Evidence

Before committing:

    git status --short

After committing:

    git status

The final working tree should be clean.

The runtime output is generated under output/ during execution. The project ignore rules exclude generated output files; committed screenshots preserve the execution evidence.
