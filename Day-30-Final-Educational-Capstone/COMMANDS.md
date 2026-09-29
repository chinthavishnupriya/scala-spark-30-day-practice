# Day 30 Education Analytics — Commands

## Compile

    sbt clean compile

## Run batch

    sbt "run batch"

## Run streaming

    sbt "run streaming"

Send sample events from another terminal:

    cat input/attendance-events.txt | nc localhost 9998

## Run both

    sbt "run all"

## Clean

    rm -rf target project/target output

## Check port

    ss -ltnp | grep 9998
