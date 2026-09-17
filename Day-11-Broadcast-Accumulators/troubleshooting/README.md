# Day 11 — Troubleshooting

## 1. `cd` says the directory does not exist

Pull the latest repository changes:

```bash
git pull origin main
```

Then enter the Day 11 folder.

## 2. Compilation fails

Run:

```bash
sbt clean compile
```

Check:

```bash
java -version
sbt --version
scala -version
```

Expected environment: Java 17, Scala 2.13.18, SBT 1.10.11, Spark 4.2.0.

## 3. Broadcast validation gives unexpected results

Check that the product IDs in `input/products.txt` match the transaction product IDs exactly.

## 4. Accumulator count is unexpected

Ensure the accumulator is incremented only when the product ID is not found in the broadcast map.

## 5. Normal variable does not behave as a distributed counter

Do not use a mutable driver variable for executor-side distributed updates. Use an accumulator for counters and sums.

## 6. Spark hostname/native Hadoop warnings in WSL

Warnings about loopback hostname resolution or the native Hadoop library can appear in local WSL execution. If the application starts and produces the expected result, these warnings do not indicate a Day 11 logic failure.
