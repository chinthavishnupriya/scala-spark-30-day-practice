# Day 08 – DAG and Spark Execution

## Objective

Understand how Apache Spark converts an RDD transformation pipeline into a DAG and divides execution into jobs, stages, tasks, and partitions.

## Practice Requirements

- Create a Spark job with several transformations and actions.
- Identify stages and shuffle boundaries.
- Explain jobs, stages, tasks, and partitions.
- Compare narrow and wide transformations.
- Predict the number of stages for a `reduceByKey` pipeline.

## Input

Input file:

`input/sales.txt`

The dataset contains sales records in the format:

```text
Day,Product,Quantity,Price
