# Troubleshooting

## Java 17 `InaccessibleObjectException`

Spark runtime reflection can require Java module access. This project uses forked execution and module-opening options in `build.sbt` and `.jvmopts`.

## Lineage inspection

Direct `toDebugString` inspection can be sensitive to runtime configuration. The application therefore prints a manual lineage representation.

## Output verification

```bash
tail -n 40 output/result.txt
grep -n "DAY 07 COMPLETED SUCCESSFULLY" output/result.txt
```
