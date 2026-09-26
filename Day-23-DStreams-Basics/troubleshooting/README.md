# Day 23 — Troubleshooting

## 1. Source directory does not exist

Create it before writing the Scala source:

```bash
mkdir -p src/main/scala
```

## 2. Compilation succeeds without compiling the source

If sbt reports success but says no Scala sources were compiled, verify:

```bash
ls -lh src/main/scala/Day23.scala
```

Then run:

```bash
sbt clean compile
```

The correct build should report:

```text
compiling 1 Scala source
```

## 3. Socket connection problem

The application expects:

```text
localhost:9999
```

Check whether another process is using the port:

```bash
ss -ltnp | grep 9999
```

Start the input producer before sending data to the Spark application.

## 4. No ERROR records

If the output repeatedly shows:

```text
ERROR count: 0
```

send lines containing the exact uppercase string `ERROR`, for example:

```text
ERROR Database connection failed
ERROR Request timeout
```

## 5. Hostname loopback warning

Spark may print a warning similar to:

```text
Your hostname ... resolves to a loopback address
```

In the tested WSL environment Spark continued successfully and started the StreamingContext, so this warning did not prevent the exercise from running.

## 6. Native Hadoop library warning

A message such as:

```text
Unable to load native-hadoop library
```

can appear in a local Spark installation. It did not prevent this local DStreams exercise from executing.

## 7. Deprecation warning during compilation

The tested build reported one Spark deprecation warning:

```text
1 deprecation (since Spark 3.4.0)
```

Compilation still completed successfully.

## 8. Micro-batch output

The application uses a 5-second interval. Therefore, output appears according to the micro-batch schedule rather than immediately for every individual input line.

A successful test produced a batch containing 6 log lines and an ERROR count of 3.

## 9. Stop the application

Because `awaitTermination()` keeps the streaming application running, use:

```text
Ctrl+C
```

after collecting the required evidence.
