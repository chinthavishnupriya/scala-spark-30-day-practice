# Day 11 — Broadcast and Accumulators

## Objective

Practice Spark broadcast variables and accumulators by validating transactions against a small product master table.

## Concepts Covered

- Broadcast variables
- Accumulators
- RDD processing
- Distributed validation
- Read-only reference data
- Driver versus executor variables

## Scenario

A small product reference map is broadcast to Spark executors. Transactions are processed as an RDD and each product ID is checked against the broadcast map. An accumulator counts transactions containing unknown product IDs.

## Input

Product master:

```text
P101 -> Laptop
P102 -> Mouse
P103 -> Keyboard
P104 -> Monitor
```

Transactions:

```text
T001,P101,2
T002,P102,5
T003,P999,1
T004,P103,3
T005,P888,2
```

`P999` and `P888` are invalid product IDs.

## Broadcast

```scala
val productBroadcast = sc.broadcast(productMaster)
```

The small product map is read-only and is made available to executors efficiently.

## Accumulator

```scala
val badRecords = sc.longAccumulator("Bad Records")
```

Invalid transactions increment the accumulator:

```scala
badRecords.add(1)
```

The final value is read on the driver using `badRecords.value`.

## Why Not a Normal Driver Variable?

A normal mutable driver variable should not be used as a distributed counter because Spark tasks execute on executors and each executor may work with its own copy. Accumulators provide the appropriate mechanism for distributed counters and sums.

## Execution

Environment:

- Java 17.0.20
- Scala 2.13.18
- Apache Spark 4.2.0
- SBT 1.10.11
- Ubuntu WSL2
- Spark local mode: `local[4]`

Run:

```bash
sbt compile
sbt run
```

## Result

```text
Broadcast product count: 4
Valid records: 3
Bad records: 2
```

The two invalid transactions contain product IDs `P999` and `P888`.

## Processing Flow

```text
Product Master Map
        |
        v
    Broadcast
        |
        v
Spark Executors <--- Transactions RDD
        |
        v
   Validate IDs
     /       \
  Valid     Invalid
              |
              v
         Accumulator
```

## Key Learning

Broadcast is suitable for small read-only reference data. Accumulators are suitable for distributed counters and sums whose final value is read by the driver.
