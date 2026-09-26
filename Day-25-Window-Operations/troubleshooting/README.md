# Day 25 Troubleshooting

## Checkpoint directory not set

Error:

```text
The checkpoint directory has not been set.
Please set it by StreamingContext.checkpoint().
```

Fix:

```scala
ssc.checkpoint("output/checkpoint")
```

The checkpoint call must be configured before `ssc.start()`.

## Local replication warnings

Messages such as:

```text
Expecting 1 replicas with only 0 peer/s
replicated to only 0 peer(s) instead of 1 peers
```

can occur with a local single-node Spark Streaming application. These warnings did not stop the Day 25 window calculations.

## Port conflict

If port 9999 is occupied:

```bash
ss -ltnp | grep 9999
```

Stop the process using the port, or change the application and client to the same available port.

## No window output

Check:

1. The Spark application says `StreamingContext started.`
2. The TCP client is listening on port 9999.
3. Input follows `ACCOUNT_ID,AMOUNT`.
4. The application remains running long enough for the 5-second batches and 10-second window slides.
5. Window and slide durations are compatible with the batch interval.

## Generated checkpoint files

Do not commit:

```text
output/checkpoint/
```

The project `.gitignore` excludes this runtime directory.
