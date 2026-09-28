# Day 28 — Commands

## Navigate

```bash
cd ~/scala-spark-30-day-practice/Day-28-Real-Time-Booking-Project
```

## Compile

```bash
sbt clean compile
```

## Start socket

Terminal 1:

```bash
nc -lk 9997
```

## Run Spark

Terminal 2:

```bash
mkdir -p output
rm -rf output/checkpoint
sbt run 2>&1 | tee output/day28-execution-output.txt
```

## Send sample events

```text
BK001,C001,BOOK,10:00:01,R001,2
BK002,C002,BOOK,10:00:03,R001,1
BK003,C003,BOOK,10:00:05,R002,3
BK004,C004,BOOK,10:00:07,R003,2
BK005,C001,CANCEL,10:00:09,R001,1
BK006,C005,BOOK,10:00:11,R001,2
BK007,C006,BOOK,10:00:13,R002,1
```

## Expected state

```text
R001 -> booked=5, cancelled=1, occupied=4, available=36
R002 -> booked=4, cancelled=0, occupied=4, available=46
R003 -> booked=2, cancelled=0, occupied=2, available=43
```

## Expected rolling window

```text
R001 -> 3 bookings
R002 -> 2 bookings
R003 -> 1 booking
```

## Verify output

```bash
ls -lh output/day28-execution-output.txt
grep -E "ROLLING BOOKING COUNT|R001|R002|R003|SPARK SQL BOOKING REPORT|occupancyPercent" output/day28-execution-output.txt
```

## Verify source copies

```bash
diff -u src/main/scala/Day28.scala code/Day28.scala
```

No output means the source copies match.

## Fresh run

```bash
rm -rf output/checkpoint
sbt clean compile
```

Then start netcat, run Spark with the `tee` command, send the events, and stop both processes with Ctrl+C.

## Git

From repository root:

```bash
git pull origin main
git status --short
git add Day-28-Real-Time-Booking-Project
git commit -m "Add Day 28 real-time booking project"
git push origin main
```
