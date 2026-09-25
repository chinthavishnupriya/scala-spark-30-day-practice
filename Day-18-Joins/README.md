# Day 18 - Spark Joins

## Overview

Day 18 focuses on joining multiple Spark DataFrames with Scala and Apache Spark. The practical uses customer, order, and payment datasets to demonstrate common relational join patterns, NULL handling, multi-table joins, Spark SQL, and physical execution-plan analysis.

The implementation is designed to show not only the returned records, but also what Spark does internally when join operations are executed.

## Objectives

This exercise covers:

- Inner Join
- Left Join
- Right Join
- Full Outer Join
- Table aliases for ambiguous columns
- NULL handling after joins
- Joining multiple DataFrames
- Spark SQL joins
- Shuffle Sort-Merge Join
- Exchange/shuffle stages
- Physical execution-plan analysis
- Practical join verification using saved execution output

## Practice-Set Alignment

The Day 18 task requires:

1. Perform inner, left, right, and full joins.
2. Handle ambiguous column names with aliases.
3. Handle NULL values after a left join.
4. Explain the shuffle and Sort-Merge Join mechanism.
5. Apply joins to a scenario involving orders, customers, and payments.

This project implements each of these areas with small sample CSV files.

## Environment

- Apache Spark: 4.2.0
- Scala: 2.13.18
- Java: 17
- SBT: 1.10.11
- Spark master: `local[4]`
- Platform: Ubuntu / WSL2

## Project Structure

```text
Day-18-Joins/
├── README.md
├── COMMANDS.md
├── .gitignore
├── .jvmopts
├── build.sbt
├── code/
│   └── Day18.scala
├── input/
│   ├── customers.csv
│   ├── orders.csv
│   └── payments.csv
├── output/
│   └── day18-execution-output.txt
├── project/
│   └── build.properties
├── screenshots/
│   ├── 01-compilation-success.png
│   ├── 02-join-results.png
│   ├── 03-null-and-full-join.png
│   └── 04-sortmerge-execution-plan.png
├── src/
│   └── main/
│       └── scala/
│           └── Day18.scala
└── troubleshooting/
    └── README.md
```

## Input Data

Three CSV files are used.

### customers.csv

Columns:

- `customer_id`
- `customer_name`
- `city`

Sample customers include C101 through C105.

### orders.csv

Columns:

- `order_id`
- `customer_id`
- `product`
- `amount`

The order data intentionally contains an order for customer C106, which is not present in the customer master data. This provides an unmatched-record case for outer joins.

### payments.csv

Columns:

- `payment_id`
- `order_id`
- `payment_method`
- `payment_status`

There are four payment records for five orders. Therefore, at least one order has no matching payment, which demonstrates NULL values after the payment left join.

## Input Record Counts

The application reads:

- 5 customer records
- 5 order records
- 4 payment records

These counts are printed at runtime and are also preserved in `output/day18-execution-output.txt`.

## 1. Inner Join

The customer and order DataFrames are joined using the common `customer_id` column.

Conceptually:

```scala
customers.alias("c")
  .join(
    orders.alias("o"),
    col("c.customer_id") === col("o.customer_id"),
    "inner"
  )
```

An inner join returns only records where the join condition matches on both sides.

Therefore, the unmatched order belonging to C106 is not returned by this join.

## 2. Left Join

The customer DataFrame is placed on the left side:

```scala
customers.alias("c")
  .join(
    orders.alias("o"),
    col("c.customer_id") === col("o.customer_id"),
    "left"
  )
```

A left join keeps every customer, even when that customer has no matching order.

This is useful for questions such as:

> Which customers have orders, and which customers currently have no matching order?

## 3. NULL Handling

A left join can produce NULL values for columns coming from the right DataFrame when no matching record exists.

The implementation demonstrates NULL handling with Spark expressions such as:

- `isNull()`
- `when()`
- `coalesce()`

For example, a missing order can be displayed using a readable replacement value rather than leaving the result as NULL.

This is important when joined data is later used for reporting or analytics.

## 4. Right Join

The right join keeps every record from the right-side DataFrame.

This allows the order dataset to retain unmatched orders even when there is no corresponding customer.

The C106 order demonstrates this case because C106 does not appear in the customer master data.

## 5. Full Outer Join

A full outer join keeps matching and non-matching records from both DataFrames.

The implementation uses aliases so columns with the same name can be referenced explicitly.

The result can therefore contain:

- customers with matching orders
- customers without orders
- orders without matching customers

This is useful when the goal is to preserve the complete population from both input datasets.

## 6. Aliases and Ambiguous Columns

Both customers and orders contain a `customer_id` column.

Referencing `customer_id` without qualification after a join can create ambiguous-column problems.

The implementation therefore uses aliases such as:

```scala
customers.alias("c")
orders.alias("o")
```

and qualified expressions such as:

```scala
col("c.customer_id")
col("o.customer_id")
```

This makes the join condition and selected columns unambiguous.

## 7. Orders and Payments Join

Orders are joined with payments using `order_id`.

A left join is used so that every order remains in the result, including orders that do not yet have a matching payment.

This produces a practical reporting view containing:

- order ID
- customer ID
- product
- amount
- payment method
- payment status

An order without a payment record produces NULL payment fields.

## 8. Three-DataFrame Join Scenario

The main business scenario combines:

```text
Customers
    |
    | customer_id
    v
Orders
    |
    | order_id
    v
Payments
```

The application first relates customers to orders and then relates orders to payments.

The resulting dataset represents customer-order-payment information in one joined view.

This is the main practical scenario for Day 18.

## 9. Spark SQL Join

The same multi-table scenario is also expressed through a temporary SQL view.

The application creates temporary views and executes a SQL query containing joins.

This demonstrates that Spark SQL and the DataFrame API can express the same relational operations using different syntax.

## 10. Broadcast Join vs Sort-Merge Join

The sample datasets are deliberately small.

For small tables, Spark may automatically choose a `BroadcastHashJoin`. That behavior is normal because broadcasting a small relation can avoid a full shuffle.

For this practical, the application sets:

```scala
spark.conf.set("spark.sql.autoBroadcastJoinThreshold", -1)
```

This disables automatic broadcast joins for the demonstration.

As a result, the physical plan can show a `SortMergeJoin`, making the shuffle behavior required by the Day 18 task visible.

This configuration is for learning and plan inspection; it is not a statement that SortMergeJoin should always replace broadcast joins.

## 11. Shuffle and Sort-Merge Join

A Sort-Merge Join generally requires both sides of the join to be arranged by the join key.

In the physical plan, Spark can therefore show stages such as:

```text
Exchange hashpartitioning(...)
        |
      Sort
        |
  SortMergeJoin
```

### Exchange

`Exchange` represents a redistribution of data between partitions.

Rows with the same join key need to be brought into compatible partitions before the join can proceed.

This redistribution is a shuffle.

### Sort

After partitioning, Spark sorts the relevant records by the join key as part of the sort-merge strategy.

### SortMergeJoin

Spark can then merge the sorted streams and match rows with equal join keys.

The saved execution output contains the physical plan used for this exercise.

## 12. Physical Plan Verification

The execution output is checked for:

- `SortMergeJoin`
- `Exchange hashpartitioning`
- `Sort`
- `FileScan`
- join type information

The presence of `Exchange hashpartitioning` demonstrates that data redistribution is part of the selected physical plan.

The presence of `SortMergeJoin` confirms that Spark selected the intended join strategy after automatic broadcast was disabled.

## 13. Transformations and Actions

The join operations are DataFrame transformations.

Examples include:

- `join()`
- `select()`
- `filter()`
- `withColumn()`
- `orderBy()`

Spark evaluates transformations lazily.

Actions such as:

- `show()`
- `count()`

cause Spark to execute the required computation.

The physical plan therefore describes how Spark will execute the logical DataFrame operations when an action requires a result.

## 14. Why Join Keys Matter

The join keys used in this project are:

| Join | Join Key |
|---|---|
| Customers + Orders | `customer_id` |
| Orders + Payments | `order_id` |
| Customer + Order + Payment | `customer_id` and `order_id` |

A correct join key is essential because an incorrect key can produce missing matches or unintended row combinations.

## 15. NULL Cases in This Dataset

The sample data deliberately provides two useful unmatched cases:

1. Customer C105 has no matching order.
2. Order O005 belongs to C106, which is not present in customers.
3. Order O003 has no payment record.

These cases allow the project to demonstrate the different behavior of inner, outer, and left joins.

## 16. Output and Evidence

The application output is stored at:

```text
output/day18-execution-output.txt
```

The screenshots document the major stages:

### 01-compilation-success.png

Shows successful project compilation.

### 02-join-results.png

Shows the main join execution and returned records.

### 03-null-and-full-join.png

Shows outer-join behavior and NULL handling.

### 04-sortmerge-execution-plan.png

Shows the physical execution plan containing the join strategy and shuffle-related stages.

## 17. Verification Commands

Compile:

```bash
sbt clean compile
```

Run:

```bash
sbt run 2>&1 | tee output/day18-execution-output.txt
```

Check completion:

```bash
grep -n "DAY 18 COMPLETED" output/day18-execution-output.txt
```

Check Sort-Merge Join:

```bash
grep -n "SortMergeJoin" output/day18-execution-output.txt
```

Check shuffle exchanges:

```bash
grep -n "Exchange hashpartitioning" output/day18-execution-output.txt
```

## 18. Result Summary

The completed practical demonstrates:

| Requirement | Implementation |
|---|---|
| Inner join | Customers + Orders |
| Left join | Customers + Orders |
| Right join | Customers + Orders |
| Full outer join | Customers + Orders |
| Ambiguous columns | Aliases `c` and `o` |
| NULL handling | `when`, `isNull`, `coalesce` |
| Multi-table join | Customers + Orders + Payments |
| Spark SQL | SQL join query |
| Shuffle explanation | Exchange/hash partitioning |
| Sort-Merge Join | Verified in physical plan |
| Execution evidence | Saved output + screenshots |

## 19. Important Observation

Because the input datasets are small, Spark's default optimizer can select a broadcast join.

The project explicitly disables automatic broadcasting only to make the Sort-Merge Join and shuffle stages visible for this exercise.

Therefore:

- BroadcastHashJoin is a possible normal optimization for small inputs.
- SortMergeJoin is demonstrated here intentionally.
- The physical plan should always be used to determine the actual strategy selected for a particular query.

## 20. Troubleshooting

Detailed troubleshooting information is available in:

```text
troubleshooting/README.md
```

Topics include:

- BroadcastHashJoin appearing unexpectedly
- SortMergeJoin verification
- Exchange/shuffle verification
- WSL hostname warnings
- Native Hadoop warnings
- Missing input files
- Compilation failures
- Output verification

## 21. Completion Checklist

- [x] Spark DataFrames loaded from CSV
- [x] Inner join implemented
- [x] Left join implemented
- [x] Right join implemented
- [x] Full outer join implemented
- [x] Ambiguous columns handled with aliases
- [x] NULL handling demonstrated
- [x] Orders and payments joined
- [x] Customer-order-payment scenario implemented
- [x] Spark SQL join demonstrated
- [x] Automatic broadcast disabled for plan demonstration
- [x] SortMergeJoin verified
- [x] Exchange/shuffle stages verified
- [x] Execution output saved
- [x] Screenshots captured
- [x] Commands documented
- [x] Troubleshooting documented

## Conclusion

Day 18 demonstrates how Spark joins relate to relational operations and physical execution.

The practical moves from basic inner/outer joins to a three-DataFrame business scenario and then examines the physical plan. The execution evidence shows how join-key partitioning, shuffle Exchange stages, sorting, and Sort-Merge Join participate in Spark's execution of the joined DataFrames.
