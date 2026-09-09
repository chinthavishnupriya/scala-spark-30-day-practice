# Troubleshooting

## Spark output

Use `tail -n 30 output/day05_run.txt` to inspect the end of the execution log and confirm successful completion.

## ERROR count

The sample application should identify 5 ERROR records. Check the input log if the count differs.

## Shuffle behavior

`distinct` can introduce a shuffle. This is expected and is not itself an error.

## Java 17

If Spark reports module-access errors, verify the Java 17 options in `build.sbt` and `.jvmopts`.
