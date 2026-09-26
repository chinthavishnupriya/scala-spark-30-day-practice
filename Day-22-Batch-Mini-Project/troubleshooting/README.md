# Day 22 — Troubleshooting

## SBT / Compilation

Run:

```bash
sbt clean compile
```

Check:

```bash
java -version
sbt --version
```

The project uses Scala 2.13.18, Spark 4.2.0, and Java 17.

## Existing Output

Remove previous generated output before rerunning:

```bash
rm -rf output/daily-sales output/spark-warehouse
```

The application writes Parquet using overwrite mode.

## Input Files

Run from the project root and verify:

```bash
ls -lh input/
```

Required files:

- `transactions.csv`
- `customers.csv`
- `products.csv`

## Invalid Records

The sample intentionally contains T006 as cancelled and T011 with negative quantity.

Expected:

```text
Invalid records removed: 2
Clean transaction count: 10
```

## Output Verification

```bash
find output/daily-sales -type f | sort
```

Expected application result:

```text
Output record count: 10
```

## Execution Plan

Important physical operators include:

- `BroadcastHashJoin`
- `BroadcastExchange`
- `HashAggregate`
- `Exchange`
- `Sort`
- `AdaptiveSparkPlan`

The exchanges represent shuffle boundaries associated with aggregation and ordering.

## WSL Warning

Spark may warn that the hostname resolves to a loopback address in WSL. This warning did not prevent the tested Day 22 application from completing successfully.

## Git

Generated Parquet data is ignored with:

```text
output/daily-sales/
```

The execution log and screenshots remain tracked as evidence.
