# Troubleshooting

## Partition count

Use `getNumPartitions` and the program's partition-distribution output to verify the requested partition changes.

## `repartition`

`repartition` uses a shuffle. Increased partition count is useful only when the added parallelism justifies the redistribution cost.

## `coalesce`

`coalesce` is primarily used to reduce partitions with less data movement than a full repartition in common cases.

## Output verification

```bash
tail -n 40 output/result.txt
grep -n "DAY 10 COMPLETED SUCCESSFULLY" output/result.txt
```

## Java 17

If Spark reports module-access errors, verify `build.sbt` and `.jvmopts`.
