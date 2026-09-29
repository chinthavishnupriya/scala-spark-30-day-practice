# Day 30 Troubleshooting

## Java module errors

Check:

    java -version
    sbt -version

The project includes Java 17 module-opening options.

## Input file not found

Run sbt from the Day-30-Final-Educational-Capstone directory.

## Port 9998 already in use

Check:

    ss -ltnp | grep 9998

Stop the process using that port before restarting streaming.

## No streaming events

Start the Spark application first, then send:

    cat input/attendance-events.txt | nc localhost 9998

## Stale checkpoint

For a fresh local experiment:

    rm -rf output/checkpoint/day30

## Slow batch

Inspect partitions, shuffle stages, broadcast joins, persisted datasets, physical plans and possible data skew.
