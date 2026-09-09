# Troubleshooting

## Spark startup warnings

WSL may show hostname or native Hadoop warnings. Check whether the application still reaches successful completion before treating a warning as a failure.

## Java module errors

Spark on Java 17 may require the module-opening options already configured in `build.sbt` and `.jvmopts`.

## Local core testing

Use `sbt "run 2"` and `sbt "run 4"` to compare the two local configurations.

## Output verification

```bash
grep -E "Spark version|Master|Default parallelism|success" output/result_local4.txt
```
