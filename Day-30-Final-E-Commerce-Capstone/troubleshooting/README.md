# Day 30 Troubleshooting

## Compilation Failure
    java -version
    sbt --version
    sbt clean compile

Verify Java 17, Scala 2.13.18, and sbt 1.10.11.

## Java 17 Module Access
If Spark reports IllegalAccessException involving ZoneInfo or JDK internals, verify the add-opens entries in build.sbt, including:
    --add-opens=java.base/sun.util.calendar=ALL-UNNAMED

Then run:
    sbt clean compile

## Input Path
Run from Day-30-Final-E-Commerce-Capstone and check:
    ls -lh input/

## Batch Results
The sample contains 12 raw orders and one cancelled order, so 11 completed orders should remain.

## Streaming Connection Refused
Check:
    ss -ltn | grep 9998

## Streaming Receives No Events
Start:
    sbt "run streaming"
Then send:
    cat input/stream-events.txt | nc localhost 9998

The local streaming demonstration runs for up to 30 seconds.

## Stateful Streaming
The checkpoint directory is:
    output/checkpoint/day30

For an intentional clean restart:
    rm -rf output/checkpoint/day30

## Port Already in Use
    ss -ltnp | grep 9998

Stop the previous listener/process before retrying.

## Physical Plan
Run batch mode and inspect the formatted plan. Look for Exchange, BroadcastExchange, repartitioning, aggregation, and sorting operators.

## Evidence
Do not commit runtime output or screenshots until compilation and execution have actually succeeded.
