# Day 28 Troubleshooting

## Port 9997 already in use

```bash
ss -ltnp | grep 9997
```

Stop the old process and restart netcat.

## Spark starts but no events appear

Check that:

1. `nc -lk 9997` is running.
2. Spark is running in Terminal 2.
3. Events are pasted into the active netcat terminal.
4. Each event has six comma-separated fields.

## State totals are incorrect

For the supplied sample:

- R001: 5 booked, 1 cancelled → 4 occupied.
- R002: 4 booked → 4 occupied.
- R003: 2 booked → 2 occupied.

## No rolling window output

Send the BOOK events close together so they fall within the same 20-second window.

## No Spark SQL report

The SQL report is produced only when the state RDD is non-empty. Check that at least one valid event reached the stateful stream.

## Checkpoint reset

```bash
rm -rf output/checkpoint
```

## Large-data warning

The example uses `collect()` and `show(false)` for a tiny educational dataset. Do not collect large production results to the driver.

## Stateful-streaming note

`updateStateByKey` maintains state across batches. A production application should define state-retention and recovery requirements and use a durable streaming design appropriate to the workload.
