# SBT Project Configuration

Day 13 uses SBT to build and run the Scala Spark application.

## Versions

- SBT: 1.10.11
- Scala: 2.13.18
- Apache Spark: 4.2.0
- Java: OpenJDK 17.0.20

## build.properties

The SBT version is configured in:

`project/build.properties`

Expected content:

```text
sbt.version=1.10.11
```

## build.sbt

The project uses:

```text
Scala 2.13.18
Spark Core 4.2.0
Spark SQL 4.2.0
```

The build uses forked execution and Java module `--add-opens` options for the configured Spark runtime.

## Source Location

The application source is available at:

```text
src/main/scala/Day13.scala
```

A copy is also maintained in:

```text
code/Day13.scala
```

## Generated Files

SBT generates build output under:

```text
target/
project/target/
```

These directories are intentionally excluded from Git through `.gitignore`.

## Useful Commands

From the Day 13 directory:

```bash
sbt clean compile
sbt "runMain Day13"
```

Check the configured SBT version with:

```bash
cat project/build.properties
```
