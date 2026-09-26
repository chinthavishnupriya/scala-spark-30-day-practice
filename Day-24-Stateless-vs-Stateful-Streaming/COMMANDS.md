# Day 24 — Commands

## Enter the project

```bash
cd ~/scala-spark-30-day-practice/Day-24-Stateless-vs-Stateful-Streaming
```

## Compile

```bash
sbt clean compile
```

## Start the streaming application

Terminal 1:

```bash
sbt run
```

The application listens on:

```text
localhost:9999
```

## Start the TCP input

Terminal 2:

```nc -l 9999
```

## Send test data

Enter transaction records in the netcat terminal:

```text
ACC001,250
ACC002,500
ACC001,100
```

Then send additional records in later micro-batches:

```text
ACC001,300
```

and:

```text
ACC002,200
```

Press Enter after each record.

## Expected behavior

First batch:

```text
ACC001 -> 2 transaction(s) in current batch
ACC002 -> 1 transaction(s) in current batch

ACC001 -> 2 transaction(s) accumulated
ACC002 -> 1 transaction(s) accumulated
```

Later ACC001 batch:

```text
ACC001 -> 1 transaction(s) in current batch

ACC001 -> 3 transaction(s) accumulated
ACC002 -> 1 transaction(s) accumulated
```

Later ACC002 batch:

```text
ACC002 -> 1 transaction(s) in current batch

ACC001 -> 3 transaction(s) accumulated
ACC002 -> 2 transaction(s) accumulated
```

## Stop the application

In the Spark terminal:

```text
Ctrl+C
```

## Verify screenshots

```bash
ls -lh screenshots/
```

## Git commands

```bash
git status --short
git add .
git commit -m "Add Day 24 stateless vs stateful streaming"
git push origin main
```

## Pull documentation updates

After documentation is updated on GitHub:

```bash
git pull origin main
```
