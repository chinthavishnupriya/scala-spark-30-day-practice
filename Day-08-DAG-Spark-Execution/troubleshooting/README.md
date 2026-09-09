# Troubleshooting

## Stage-count reasoning

Count shuffle boundaries rather than simply counting transformations. Narrow transformations can remain in the same stage.

## Shuffle behavior

`reduceByKey` and sorting can introduce shuffle boundaries. This is expected execution behavior.

## Runtime issues

For Java 17 module-access errors, verify the options in `build.sbt` and `.jvmopts`.

## Output verification

```bash
tail -n 40 output/result.txt
grep -n "DAY 08 COMPLETED SUCCESSFULLY" output/result.txt
```
