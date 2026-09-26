# Day 27 — Commands

## Navigate

```bash
cd ~/scala-spark-30-day-practice/Day-27-Real-Time-Banking-Project
```

## Compile

```bash
sbt clean compile
```

## Start TCP server

Terminal 1:

```bash
nc -lk 9998
```

## Run Spark

Terminal 2:

```rm -rf output/checkpoint
sbt run 2>&1 | tee output/day27-execution-output.txt
```

## Send sample events

Paste:

```text
T001,A001,10:00:01,5000,B001,ATM
T002,A001,10:00:03,4500,B001,ATM
T003,A001,10:00:05,4200,B001,ONLINE
T004,A002,10:00:06,300,B002,POS
T005,A003,10:00:08,15000,B003,ONLINE
T006,A002,10:00:12,250,B002,POS
```

Expected account totals:

```text
A001 -> 13700.00
A002 -> 550.00
A003 -> 15000.00
```

Expected burst:

```text
BURST ALERT: A001 -> 3 transactions in window
```

## Inspect

```bash
cat output/day27-execution-output.txt
grep 'ACCOUNT TOTALS' output/day27-execution-output.txt
grep 'BURST ALERT' output/day27-execution-output.txt
grep 'BRANCH/RISK ENRICHMENT' output/day27-execution-output.txt
```

## Check port

```bash
ss -ltnp | grep 9998
```

## Fresh run

```bash
rm -rf output/checkpoint
sbt clean compile
```

Then start `nc -lk 9998`, run `sbt run`, and send the sample events.

## Verify source copies

```bash
diff -u src/main/scala/Day27.scala code/Day27.scala
```

No output means both copies match.

## Git

From repository root:

```bash
git status --short
git pull origin main
git add Day-27-Real-Time-Banking-Project
git commit -m "Add Day 27 real-time banking project"
git push origin main
```

## Stop

Use `Ctrl+C` in the Spark terminal and netcat terminal.
