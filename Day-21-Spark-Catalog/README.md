# Day 21 - Spark Catalog

## Objective

Practice Spark Catalog operations for a small hotel-booking analytics database.

## Topics Covered

- Create and list databases
- Create temporary views
- Query temporary views with Spark SQL
- Create and register a managed catalog table
- List catalog tables
- Query a registered table
- Inspect table schema with DESCRIBE
- Inspect extended table metadata with DESCRIBE EXTENDED
- Use the Spark Catalog API
- Check table types and object existence

## Environment

- Apache Spark 4.2.0
- Scala 2.13.18
- SBT 1.10.11
- Java 17.0.20
- WSL2 / Ubuntu
- Spark master: local[4]

## Project Structure

```
Day-21-Spark-Catalog/
├── README.md
├── COMMANDS.md
├── .jvmopts
├── .gitignore
├── build.sbt
├── code/
│   └── Day21.scala
├── input/
│   └── hotel_bookings.csv
├── output/
│   └── day21-execution-output.txt
├── project/
│   └── build.properties
├── screenshots/
│   ├── 01-compilation-success.png
│   ├── 02-catalog-database-and-table.png
│   ├── 03-catalog-metadata.png
│   └── 04-execution-success.png
├── src/main/scala/
│   └── Day21.scala
└── troubleshooting/
    └── README.md
```

## Dataset

The input contains 8 hotel bookings with:

- booking ID
- customer ID
- hotel
- city
- room type
- number of nights
- amount
- booking date

## Implementation

The application:

1. Creates the `hotel_analytics` database.
2. Lists available databases.
3. Reads `input/hotel_bookings.csv` into a DataFrame.
4. Creates the temporary view `hotel_bookings_view`.
5. Runs an aggregation query on the temporary view.
6. Creates the managed table `hotel_analytics.hotel_bookings`.
7. Lists catalog tables.
8. Queries the registered table by city.
9. Runs `DESCRIBE` and `DESCRIBE EXTENDED`.
10. Uses the Spark Catalog API to inspect tables and columns.
11. Checks table/view types and existence.

The Spark SQL warehouse is configured under `output/spark-warehouse/`, and that generated directory is ignored by Git.

## Verified Results

The successful execution verified:

- Database: `hotel_analytics`
- Temporary view: `hotel_bookings_view`
- Managed table: `hotel_bookings`
- Table provider: Parquet
- Table type: MANAGED
- Temporary view type: TEMPORARY
- Table exists: true
- Temporary view exists: true

City-level registered-table results:

| City | Bookings | Revenue |
|---|---:|---:|
| Hyderabad | 4 | 50000 |
| Goa | 2 | 44000 |
| Bengaluru | 2 | 18000 |

The application ended with:

```
DAY 21 COMPLETED SUCCESSFULLY
```

## How to Run

From this directory:

```bash
sbt clean compile
sbt run
```

Execution output is saved in:

```
output/day21-execution-output.txt
```

## Notes

Spark startup may print WSL hostname and native Hadoop library warnings. These did not prevent the successful Day 21 execution.
