# Day 26 Troubleshooting

## 1. Connection refused on port 9999

### Problem

Spark Streaming reports:

`Connection refused`

### Cause

`socketTextStream("localhost", 9999)` expects an external TCP socket server.

### Solution

Start netcat as the socket server in a separate terminal:

```bash
nc -lk 9999
```

Then start the Spark application in another terminal:

```bash
sbt run
```

Keep the netcat terminal running while the Spark Streaming application is active.

---

## 2. Netcat immediately returns to the shell

### Problem

Running:

```bash
nc localhost 9999
```

immediately returns to the shell.

### Cause

That command attempts to connect as a client. Spark is also a client/receiver for `socketTextStream`, so there is no socket server available.

### Solution

Use:

```bash
nc -lk 9999
```

This creates the TCP socket server.

---

## 3. Patient data produces command-not-found errors

### Problem

Entering:

```text
P001,10:00:01,78,36.7,98
```

produces a Bash error such as:

```text
command not found
```

### Cause

The data was entered into a normal Bash prompt rather than the terminal running the netcat server.

### Solution

Start:

```bash
nc -lk 9999
```

and paste patient records into that running netcat terminal.

---

## 4. No abnormal readings appear

Verify the input format:

```text
patientId,timestamp,heartRate,temperature,spo2
```

Example:

```text
P001,10:00:04,130,38.1,91
```

The demonstration thresholds are:

- Heart rate > 120
- Temperature > 38.0
- SpO2 < 94

A record is abnormal when at least one condition is true.

---

## 5. Repeated abnormal alert does not appear

The application uses:

```text
Window size    = 20 seconds
Slide interval = 10 seconds
Minimum count  = 2
```

A patient needs at least two abnormal readings inside the active window.

For example:

```text
P002,10:00:02,125,37.2,94
P002,10:00:05,128,37.4,93
```

produces a repeated abnormal-reading alert for P002.

---

## 6. Accumulator count is unexpectedly high

The abnormal-vital DStream is persisted with:

```scala
.persist(StorageLevel.MEMORY_ONLY)
```

The persistence allows multiple downstream operations to reuse the abnormal stream rather than repeatedly recomputing the upstream transformation.

The tested six-record input contains four abnormal records, and the successful run reports:

```text
Patients processed: 6
Total abnormal records: 4
```

---

## 7. Checkpoint directory problems

The application requires a checkpoint directory for the windowed computation.

The source contains:

```scala
ssc.checkpoint("output/checkpoint")
```

If a stale local checkpoint causes a problem during a fresh demonstration run, remove it before restarting:

```bash
rm -rf output/checkpoint
```

Then start the application again.

Do not commit generated checkpoint files to Git.

---

## 8. Local Spark replication warnings

A local single-node Spark run can show warnings similar to:

```text
Expecting 1 replicas with only 0 peer/s
replicated to only 0 peer(s) instead of 1 peers
```

These can occur because the application is running with a single local Spark executor and no peer workers.

If the application continues processing the stream and produces the expected alerts, these warnings did not prevent the demonstration from running.

---

## 9. Loopback hostname warning

Spark may report:

```text
Your hostname resolves to a loopback address
```

This can occur in WSL/local environments.

The tested Day 26 application continued to start Spark and process the patient events despite this warning.

---

## 10. Native Hadoop library warning

A local Spark installation may report:

```text
Unable to load native-hadoop library
```

For this local demonstration, Spark can continue using its available fallback implementation.

---

## 11. Port 9999 already in use

Check whether another process is listening:

```bash
ss -ltnp | grep 9999
```

Stop the old socket server if necessary, then start:

```bash
nc -lk 9999
```

---

## 12. Verify the execution output

The tested execution output is stored at:

```text
output/day26-execution-output.txt
```

Inspect it with:

```bash
cat output/day26-execution-output.txt
```

Look for:

```text
StreamingContext started.
```

and the abnormal/repeated-alert results.

---

## 13. Clean rebuild

If the project behaves unexpectedly after source changes:

```bash
sbt clean compile
```

Then start a fresh streaming run:

```bash
rm -rf output/checkpoint
sbt run
```

---

## 14. Expected successful result

For the provided six-record test input, the successful run should contain:

```text
ALERT: P001 | HR=130.0 | Temp=38.1 | SpO2=91.0
ALERT: P002 | HR=125.0 | Temp=37.2 | SpO2=94.0
ALERT: P002 | HR=128.0 | Temp=37.4 | SpO2=93.0
ALERT: P003 | HR=82.0 | Temp=38.5 | SpO2=97.0

REPEATED ALERT: P002 -> 2 abnormal readings in window

Patients processed: 6
Total abnormal records: 4
```

This is the expected local test result for the supplied sample input.
