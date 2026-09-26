# Day 27 Troubleshooting

## Port already in use

```bash
ss -ltnp | grep 9998
```

Stop the old process and restart `nc -lk 9998`.

## Spark starts but no events appear

Make sure Terminal 1 is running:

```bash
nc -lk 9998
```

Then run Spark in Terminal 2 and paste the records into the active netcat session.

## No burst alert

The sample must contain all three A001 records inside the same 20-second window. Send the six sample records close together.

## Checkpoint reset

For a fresh local demonstration:

```bash
rm -rf output/checkpoint
```

## Large-data warning

The demonstration uses `collect()` for readable console output. Do not use this pattern for large production datasets.
