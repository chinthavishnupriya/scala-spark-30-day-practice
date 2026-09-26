# Day 25 — Commands

## 1. Enter the project

```bash
cd ~/scala-spark-30-day-practice/Day-25-Window-Operations
```

## 2. Compile

```bash
sbt clean compile
```

Expected:

```text
[success] Total time: ...
```

## 3. Run the streaming application

Terminal 1:

```bash
sbt run
```

Expected startup:

```text
DAY 25 - WINDOW OPERATIONS
Batch interval : 5 seconds
Window size    : 20 seconds
Slide interval : 10 seconds
Listening on   : localhost:9999
StreamingContext started.
```

## 4. Start the TCP input

Terminal 2:

```bash
cd ~/scala-spark-30-day-practice/Day-25-Window-Operations
nc -l 9999
```

## 5. Send the first batch

```text
ACC001,100
ACC002,250
ACC001,150
```

## 6. Send another batch

```text
ACC001,200
ACC002,300
ACC001,100
```

Continue running the application long enough to observe the 20-second rolling window and 10-second slide.

## 7. Expected count output

```text
COUNT BY WINDOW - ...
Transactions in last 20 seconds: 3
```

## 8. Expected rolling sales output

```text
REDUCE BY KEY AND WINDOW - ...
ACC001 -> rolling sales = 250.00
ACC002 -> rolling sales = 250.00
```

The exact values depend on which records are currently inside the rolling window.

## 9. Checkpoint

The application configures:

```text
output/checkpoint
```

This directory is runtime state and is ignored by Git.

## 10. Stop the application

```text
Ctrl+C
```

## 11. Verify screenshots

```bash
ls -lh screenshots/
```

Expected files:

```text
01-compilation-success.png
02-streaming-context-started.png
03-window-input-batch-1.png
04-count-by-window.png
05-rolling-sales-and-burst-alert.png
06-complete-success.png
```

## 12. Git commands

Run these from the repository root:

```bash
cd ~/scala-spark-30-day-practice
git status
git add Day-25-Window-Operations
git commit -m "Add Day 25 window operations"
git push origin main
```

Day 25 implementation and screenshots were pushed in commit:

```text
0ca3873 Add Day 25 window operations
```
