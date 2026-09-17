# Day 12 - Troubleshooting

## `sbt clean compile` fails with a command parsing error
If SBT reports errors around `clean compile`, check the SBT version and project configuration. Day 12 uses SBT 1.10.11.

## SBT unexpectedly changes `project/build.properties`
If a local SBT 2.x client creates or updates `project/build.properties`, restore the project to `sbt.version=1.10.11` before committing. Then run `sbt clean compile` again.

## Spark hostname warning on WSL
A warning that the hostname resolves to a loopback address can occur in WSL. It does not by itself mean the application failed. If the application completes successfully, the warning can normally be left as-is for this local exercise.

## Native Hadoop library warning
`Unable to load native-hadoop library` is common in local WSL Spark setups. It is a warning, not a failure, when the application continues and produces the expected output.

## Cache vs persist
`cache()` uses `MEMORY_ONLY` by default. Use `persist()` when a different storage level is required.

## Caching performance
Do not cache every RDD automatically. Caching is most useful when an RDD is reused and expensive to recompute. It can hurt when the RDD is used once or consumes too much memory.
