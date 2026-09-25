# Day 20 - Troubleshooting

## Issue 1: `value !! is not a member of Seq[String]`

### Cause

The initial implementation attempted to use Scala's `!!` shell-command operator without importing the required process package.

### Resolution

The shell command was removed from the final implementation.

The program now uses Java's `File` API to recursively inspect output directories. This keeps the solution self-contained and correctly handles nested partition directories.

## Issue 2: Partitioned Output File Counting

A simple directory-level file counter does not find Parquet files inside nested partition directories.

The final implementation uses a recursive `dataFiles` function and counts only files ending in `.parquet`.

This correctly reports the four actual Parquet data files under the date partitions.

## Issue 3: `_SUCCESS` and `.crc` Files

Spark output directories can contain:

```text
_SUCCESS
*.crc
```

These are metadata/checksum files rather than data records.

The implementation counts only the requested data extensions such as `.csv`, `.json`, and `.parquet`.

## Issue 4: WSL Hostname Warning

Spark may display a warning that the hostname resolves to a loopback address and suggest setting `SPARK_LOCAL_IP`.

For this local WSL execution, the Spark application continued successfully, so the warning did not prevent the exercise from completing.

## Issue 5: Native Hadoop Library Warning

Spark may display:

```text
Unable to load native-hadoop library
```

when running locally in WSL.

For this practice project, Spark continued using its available functionality and completed successfully.

## Verification

The final Day 20 execution:

- compiled successfully
- processed 12 records
- wrote CSV, JSON, and Parquet
- repartitioned the data into 3 partitions
- created four date partitions
- produced four actual partitioned Parquet data files
- printed the execution plan
- completed successfully
