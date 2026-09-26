# Day 21 - Spark Catalog

## 📌 Overview

Day 21 focuses on the Spark Catalog, which provides a metadata layer for databases, tables, temporary views, schemas, and SQL objects used by Spark SQL.

The practical scenario is a small hotel booking analytics database. The project demonstrates how booking data can be loaded into a DataFrame, exposed through a temporary SQL view, stored as a managed catalog table, queried with Spark SQL, and inspected through SQL metadata commands and the Spark Catalog API.

## 🎯 Objectives

1. Create and list Spark SQL databases.
2. Read hotel booking data into a DataFrame.
3. Create a temporary view from the DataFrame.
4. Query the temporary view using Spark SQL.
5. Create a managed catalog table.
6. List registered tables.
7. Query a registered table.
8. Inspect table schema using DESCRIBE.
9. Inspect detailed metadata using DESCRIBE EXTENDED.
10. Use the Spark Catalog API to inspect tables and columns.
11. Identify managed and temporary objects.
12. Verify that catalog objects exist.

## 📚 Concepts Covered

### Spark Catalog

The Spark Catalog is used to discover and inspect SQL objects available to the Spark application.

This practical uses the Catalog to inspect:

- Current database
- Tables
- Columns
- Table types
- Temporary views
- Object existence

### Database

A database provides a namespace for tables.

The project creates the hotel_analytics database and then selects it with USE hotel_analytics.

### Temporary View

A DataFrame is registered as the temporary view hotel_bookings_view.

The view is then queried with Spark SQL. It is temporary and associated with the current Spark application.

### Managed Catalog Table

The booking DataFrame is stored as the managed table hotel_analytics.hotel_bookings.

The verified metadata identifies the table as MANAGED with Parquet as its provider.

## 🗂️ Project Structure

    Day-21-Spark-Catalog/
    ├── README.md
    ├── COMMANDS.md
    ├── .gitignore
    ├── .jvmopts
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

## 💾 Input Dataset

File: input/hotel_bookings.csv

The dataset contains 8 hotel bookings.

| Column | Description |
|---|---|
| booking_id | Unique booking identifier |
| customer_id | Customer identifier |
| hotel | Hotel name |
| city | Hotel city |
| room_type | Booked room type |
| nights | Number of nights |
| amount | Booking amount |
| booking_date | Date of booking |

## 🔄 Practical Workflow

    CSV File
       ↓
    Spark DataFrame
       ↓
    Temporary View
       ↓
    Spark SQL Query
       ↓
    hotel_analytics Database
       ↓
    Managed hotel_bookings Table
       ↓
    DESCRIBE / DESCRIBE EXTENDED
       ↓
    Spark Catalog API
       ↓
    Metadata Verification

## 🧩 Implementation Steps

### Step 1 - Create Spark Session

The application starts Spark in local mode using local[4].

The Spark SQL warehouse is configured under output/spark-warehouse/ so generated warehouse data remains isolated from the repository source files.

### Step 2 - Create Database

The application executes:

    CREATE DATABASE IF NOT EXISTS hotel_analytics

### Step 3 - List Databases

The application executes:

    SHOW DATABASES

The verified execution showed:

    default
    hotel_analytics

### Step 4 - Read the CSV

The CSV is loaded with a header and schema inference.

The resulting DataFrame contains 8 booking records.

The verified schema includes:

- String fields for identifiers and descriptive fields
- Integer fields for nights and amount
- Date type for booking_date

### Step 5 - Create Temporary View

The DataFrame is registered as:

    hotel_bookings_view

This makes the booking data accessible using Spark SQL.

### Step 6 - Query Temporary View

Revenue is calculated by hotel and city.

Verified results:

| Hotel | City | Total Revenue |
|---|---|---:|
| Beach Resort | Goa | 44000 |
| Lake View Resort | Hyderabad | 29000 |
| Grand Palace | Hyderabad | 21000 |
| City Inn | Bengaluru | 18000 |

### Step 7 - Create Catalog Table

The application switches to hotel_analytics and creates the managed table hotel_bookings.

The implementation drops an existing table before recreating it during the practical run.

### Step 8 - List Tables

The verified catalog contained:

| Object | Temporary |
|---|---|
| hotel_bookings | false |
| hotel_bookings_view | true |

### Step 9 - Query Registered Table

The managed table is aggregated by city.

Verified results:

| City | Bookings | Revenue |
|---|---:|---:|
| Hyderabad | 4 | 50000 |
| Goa | 2 | 44000 |
| Bengaluru | 2 | 18000 |

## 🔎 Metadata Inspection

### DESCRIBE

The application runs DESCRIBE hotel_bookings.

Verified schema:

    booking_id    string
    customer_id   string
    hotel         string
    city          string
    room_type     string
    nights        int
    amount        int
    booking_date  date

### DESCRIBE EXTENDED

The application runs DESCRIBE EXTENDED hotel_bookings.

Verified metadata includes:

| Property | Value |
|---|---|
| Catalog | spark_catalog |
| Database | hotel_analytics |
| Table | hotel_bookings |
| Type | MANAGED |
| Provider | parquet |

The managed table location is under:

    output/spark-warehouse/hotel_analytics.db/hotel_bookings

The generated warehouse is excluded from Git.

## 🧰 Spark Catalog API

The Scala implementation demonstrates:

- spark.catalog.currentDatabase
- spark.catalog.listTables()
- spark.catalog.listColumns("hotel_bookings")
- spark.catalog.getTable("hotel_bookings").tableType
- spark.catalog.getTable("hotel_bookings_view").tableType
- spark.catalog.tableExists("hotel_bookings")
- spark.catalog.tableExists("hotel_bookings_view")

Verified metadata:

    Current database: hotel_analytics
    hotel_bookings table type: MANAGED
    hotel_bookings_view table type: TEMPORARY
    Table exists: true
    Temporary view exists: true

## 🔁 Managed Table vs Temporary View

| Feature | Managed Table | Temporary View |
|---|---|---|
| Object | hotel_bookings | hotel_bookings_view |
| Type | MANAGED | TEMPORARY |
| Queryable with SQL | Yes | Yes |
| Catalog inspection | Yes | Yes |
| Lifetime | Catalog-managed table | Current Spark application |

## ⚙️ Spark Operations Used

### DataFrame Operations

- CSV read
- Schema inference
- show
- printSchema
- createOrReplaceTempView
- write
- saveAsTable

### Spark SQL Operations

- CREATE DATABASE
- SHOW DATABASES
- USE
- DROP TABLE IF EXISTS
- SHOW TABLES
- SELECT
- GROUP BY
- ORDER BY
- DESCRIBE
- DESCRIBE EXTENDED

### Catalog API

- currentDatabase
- listTables
- listColumns
- getTable
- tableExists

## 🧪 Testing and Verification

The project was tested using:

    sbt clean compile
    sbt run

Execution output is preserved in:

    output/day21-execution-output.txt

The successful execution verified the database, temporary view, managed table, schema, metadata, Catalog API information, and aggregation results.

Final output:

    DAY 21 COMPLETED SUCCESSFULLY

## 📸 Screenshots

### 1. Compilation Success

screenshots/01-compilation-success.png

Shows successful SBT compilation.

### 2. Catalog Database and Table

screenshots/02-catalog-database-and-table.png

Shows database creation, temporary view creation, table creation, and table listing.

### 3. Catalog Metadata

screenshots/03-catalog-metadata.png

Shows schema and extended table metadata.

### 4. Execution Success

screenshots/04-execution-success.png

Shows final successful completion and Catalog API verification.

## 🧠 Key Learning Points

- Spark Catalog provides metadata access to Spark SQL objects.
- A DataFrame can be exposed as a temporary SQL view.
- A DataFrame can be persisted as a managed catalog table.
- SHOW DATABASES and SHOW TABLES help inspect SQL objects.
- DESCRIBE provides table schema information.
- DESCRIBE EXTENDED provides detailed table metadata.
- The Catalog API provides programmatic metadata access.
- Table type information distinguishes managed and temporary objects.
- Generated Spark warehouse files should not be committed to Git.
- Spark 4.2 Catalog API compatibility should be considered when selecting methods.

## ⚠️ Troubleshooting Summary

### isTemporaryTable API issue

An earlier implementation attempted to use spark.catalog.isTemporaryTable.

That method was not available in the Spark 4.2.0 Catalog API used by this project.

The implementation instead uses getTable(...).tableType.

Verified results:

    hotel_bookings -> MANAGED
    hotel_bookings_view -> TEMPORARY

### LOCATION_ALREADY_EXISTS

A previous rerun encountered a managed-table location conflict.

The project uses a dedicated warehouse:

    output/spark-warehouse/

For a clean local rerun:

    rm -rf output/spark-warehouse
    sbt run

More details are available in troubleshooting/README.md.

## ▶️ How to Run

Navigate to the project:

    cd ~/scala-spark-30-day-practice/Day-21-Spark-Catalog

Compile:

    sbt clean compile

Run:

    sbt run

Save execution output:

    sbt run 2>&1 | tee output/day21-execution-output.txt

Check Git status:

    git status --short --untracked-files=all

## 📌 Final Result

Day 21 successfully demonstrates the Spark Catalog workflow using a hotel booking analytics scenario.

The project includes:

- Complete Scala implementation
- Sample CSV dataset
- Spark SQL database and table operations
- Temporary view
- Catalog API inspection
- Schema and extended metadata inspection
- Verified execution output
- Four screenshots
- Command reference
- Troubleshooting documentation
- GitHub-ready project structure

Status: DAY 21 COMPLETED SUCCESSFULLY
