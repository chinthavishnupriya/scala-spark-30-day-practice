# Day 11 — Broadcast and Accumulators

## Objective

Practice Spark broadcast variables and accumulators by validating transactions against a small product master table.

## Practice Requirements

- Broadcast a small product reference map.
- Use an accumulator to count bad records.
- Explain why normal driver variables should not be used for distributed updates.
- Combine broadcast data with RDD processing.
- Validate transactions against a small master table.

## Concepts Covered

- Broadcast variables
- Accumulators
- RDD processing
- Distributed validation
- Read-only reference data
- Driver versus executor variables
- Actions and transformations

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

## Broadcast Variable

```scala
val productBroadcast = sc.broadcast(productMaster)
```

The product master is small and read-only, making it suitable for broadcasting. Tasks can access the value through:

```scala
productBroadcast.value
```

## Accumulator

```scala
val badRecords = sc.longAccumulator("Bad Records")
```

When an unknown product is found:

```scala
badRecords.add(1)
```

The final value is read on the driver:

```scala
badRecords.value
```

## Why Not a Normal Driver Variable?

A normal mutable driver variable should not be used as a distributed counter because Spark tasks execute on executors and distributed tasks do not safely update the driver's local variable. An accumulator provides the supported mechanism for distributed counters and sums.

## Processing Flow

```text
Product Master Map
        |
        v
    Broadcast
        |
        v
Spark Tasks <--------- Transactions RDD
        |
        v
   Validate IDs
     /       \
  Valid     Invalid
              |
              v
         Accumulator
              |
              v
        Bad Records
```

## Transformations and Actions

### Transformation

`map` validates each transaction and creates a validated result. It is lazy and does not execute immediately.

### Action

`collect()` triggers execution of the transformation and brings the final small result to the driver.

## Execution Environment

- Java 17.0.20
- Scala 2.13.18
- Apache Spark 4.2.0
- SBT 1.10.11
- Ubuntu WSL2
- Spark local mode: `local[4]`

## Run Commands

```bash
sbt compile
sbt run
```

Save the terminal output:

```bash
sbt run > output/result.txt 2>&1
```

## Execution Result

```text
Spark version: 4.2.0
Master: local[4]
Broadcast product count: 4
Valid records: 3
Bad records: 2
```

Validated transactions:

```text
T001,P101,Laptop,2,VALID
T002,P102,Mouse,5,VALID
T003,P999,UNKNOWN,1,INVALID
T004,P103,Keyboard,3,VALID
T005,P888,UNKNOWN,2,INVALID
```

## Performance Considerations

- Broadcast avoids repeatedly shipping a small reference dataset with task work.
- Broadcast is intended for data small enough to be held in executor memory.
- Accumulators are useful for counters and sums, not for implementing general shared mutable state.
- `collect()` should be used only when the resulting data is small enough to fit safely on the driver.
- The transaction RDD uses four partitions in this local demonstration.

## Project Structure

```text
Day-11-Broadcast-Accumulators/
├── .gitignore
├── .jvmopts
├── README.md
├── COMMANDS.md
├── build.sbt
├── code/
│   └── Day11.scala
├── input/
│   ├── products.txt
│   └── transactions.txt
├── output/
│   └── result.txt
├── project/
│   ├── build.properties
│   └── README.md
├── screenshots/
│   └── README.md
├── src/
│   └── main/
│       └── scala/
│           └── Day11.scala
└── troubleshooting/
    └── README.md
```

## Technologies

- Scala 2.13.18
- Apache Spark 4.2.0
- SBT 1.10.11
- Java 17.0.20
- Ubuntu WSL2
- Local Spark mode with 4 cores

## Learning Outcome

Day 11 demonstrates how Spark shares small read-only reference data using broadcast variables and records distributed counters using accumulators. It also reinforces RDD transformations, actions, driver/executor behavior, and basic performance considerations.
