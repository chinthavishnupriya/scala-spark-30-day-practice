# Day 24 Troubleshooting

## updateStateByKey callback type error

If Scala reports a missing parameter type for the state update callback, explicitly type the parameters:

```scala
.updateStateByKey[Int] {
  (newValues: Seq[Int], previousState: Option[Int]) =>
    Some(previousState.getOrElse(0) + newValues.sum)
}
```

## Checkpoint directory

Stateful DStream processing requires checkpoint configuration.

Use:

```scala
ssc.checkpoint("output/checkpoint")
```

before starting the StreamingContext.

Keep the generated checkpoint directory out of Git:

```text
output/checkpoint/
```

## Local Spark replication warnings

In local single-node mode, messages such as:

```text
RandomBlockReplicationPolicy: Expecting 1 replicas with only 0 peer/s
BlockManager: replicated to only 0 peer(s) instead of 1 peers
```

may appear. These warnings do not necessarily indicate application failure. Verify that the stateless and stateful results continue to be produced.

## No streaming input

Check that the TCP client is listening on port 9999:

```bash
nc -l 9999
```

Then send records in the expected format:

```text
ACC001,100
ACC002,250
```

## Stateless vs stateful result confusion

- **Stateless:** reports the result for the current micro-batch only.
- **Stateful:** retains accumulated state across micro-batches.

For example, if ACC001 has 2 transactions in the first batch and 1 in the next batch, the stateful result should increase from 2 to 3 while the stateless result for the second batch shows only 1.

## Stopping the application

Use:

```text
Ctrl+C
```

to stop the streaming application after capturing the required output.
