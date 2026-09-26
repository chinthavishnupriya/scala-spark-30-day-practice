# Day 23 — Commands

## Enter the project

```bash
cd ~/scala-spark-30-day-practice/Day-23-DStreams-Basics
```

## Compile

```bash
sbt clean compile
```

Expected result:

```text
[success] Total time: ...
```

A Spark deprecation warning may appear; it does not prevent compilation.

## Start the streaming application

```bash
sbt run
```

The application uses:

- host: `localhost`
- port: `9999`
- batch interval: **5 seconds**

Expected startup output:

```text
DAY 23 - DSTREAMS BASICS
Batch interval: 5 seconds
Waiting for log stream on localhost:9999
StreamingContext started.
Send log lines to port 9999.
```

## Send socket input

Use a second terminal and provide application log lines to port 9999 using a socket producer such as netcat.

Example:

```bash
nc localhost 9999
```

Then type sample lines such as:

```text
INFO Application started
ERROR Database connection failed
INFO Request received
ERROR Timeout while calling service
WARN Retry started
ERROR Service unavailable
```

Stop the Spark application with:

```text
Ctrl+C
```

## Verify the project

```bash
find . -maxdepth 4 -type f | sort
```

Check screenshots:

```bash
ls -lh screenshots/
```

Check Git status:

```bash
git status
```

## Commit and push

From the repository root:

```bash
cd ~/scala-spark-30-day-practice
git add Day-23-DStreams-Basics
git commit -m "Add Day 23 DStreams basics"
git push origin main
```
