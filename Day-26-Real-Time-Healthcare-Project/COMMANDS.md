# Day 26 - Commands

## 1. Navigate to the project

```bash
cd ~/scala-spark-30-day-practice/Day-26-Real-Time-Healthcare-Project


## 15. Complete Local Workflow

### Project navigation

```bash
cd ~/scala-spark-30-day-practice/Day-26-Real-Time-Healthcare-Project
```

### Check Java

```bash
java -version
```

### Check Scala/sbt environment

```bash
sbt --version
```

### Compile

```bash
sbt clean compile
```

### Start TCP socket server

Terminal 1:

```bash
nc -lk 9999
```

### Start Spark Streaming

Terminal 2:

```bash
cd ~/scala-spark-30-day-practice/Day-26-Real-Time-Healthcare-Project
rm -rf output/checkpoint
sbt run 2>&1 | tee output/day26-execution-output.txt
```

### Send sample records

Terminal 1:

```bash
cat input/sample-vitals.txt
```

Copy the displayed records into the active `nc -lk 9999` terminal.

### Inspect results

```bash
cat output/day26-execution-output.txt
```

### Find immediate alerts

```bash
grep 'ALERT:' output/day26-execution-output.txt
```

### Find repeated alerts

```bash
grep 'REPEATED ALERT' output/day26-execution-output.txt
```

### Find accumulator result

```bash
grep 'Total abnormal records' output/day26-execution-output.txt
```

Expected accumulator result for the supplied six records:

```text
Total abnormal records: 4
```

## 16. Checkpoint Management

Remove generated checkpoint state before a fresh demonstration run:

```bash
rm -rf output/checkpoint
```

Check whether checkpoint files were generated:

```bash
find output/checkpoint -type f | sort
```

Checkpoint state should not be committed to Git.

## 17. Source Verification

Compare the main source and copied source:

```bash
diff -u src/main/scala/Day26.scala code/Day26.scala
```

No output means the two files match.

Display the source:

```bash
cat src/main/scala/Day26.scala
```

## 18. Project Structure Verification

```bash
find . -maxdepth 3 -type f | sort
```

Check Git status:

```bash
git status --short
```

## 19. Troubleshooting Commands

Check port 9999:

```bash
ss -ltnp | grep 9999
```

If necessary, check running netcat processes:

```bash
ps aux | grep '[n]c.*9999'
```

Check the latest execution output:

```bash
tail -n 80 output/day26-execution-output.txt
```

Search Spark startup:

```bash
grep 'StreamingContext started' output/day26-execution-output.txt
```

Search patient processing:

```bash
grep 'Patients processed' output/day26-execution-output.txt
```

## 20. Fresh Test Procedure

Use this sequence for a clean local test:

```bash
cd ~/scala-spark-30-day-practice/Day-26-Real-Time-Healthcare-Project
rm -rf output/checkpoint
sbt clean compile
```

Then start the socket server in Terminal 1:

```bash
nc -lk 9999
```

Start Spark in Terminal 2:

```bash
sbt run 2>&1 | tee output/day26-execution-output.txt
```

Paste `input/sample-vitals.txt` into Terminal 1 and wait for the windowed output.

## 21. Git Commands

From the repository root:

```bash
cd ~/scala-spark-30-day-practice
git status --short
git add Day-26-Real-Time-Healthcare-Project
git commit -m "Update Day 26 documentation"
git push origin main
```

Review recent commits:

```bash
git log --oneline -5
```

## 22. Stop Running Processes

Stop Spark with:

```text
Ctrl+C
```

Stop the netcat server with:

```text
Ctrl+C
```

Do not enter patient records at a normal Bash prompt; they must be entered while the netcat server is running.
