# Day 21 Troubleshooting

## 1. Spark 4.2 Catalog API compatibility

An earlier implementation attempted to use `spark.catalog.isTemporaryTable`. Spark 4.2.0 does not expose that method in the Catalog API used by this project.

### Fix

Use table metadata instead:

```scala
spark.catalog.getTable("hotel_bookings").tableType
spark.catalog.getTable("hotel_bookings_view").tableType
```

The verified output reports:

- `hotel_bookings` -> `MANAGED`
- `hotel_bookings_view` -> `TEMPORARY`

## 2. LOCATION_ALREADY_EXISTS

When rerunning a managed `saveAsTable`, Spark can report:

```
LOCATION_ALREADY_EXISTS
```

This can happen when the managed table's warehouse directory remains from an earlier run.

### Fix used in this project

The application explicitly uses:

```scala
.config("spark.sql.warehouse.dir", "output/spark-warehouse")
```

The generated warehouse is ignored by Git. For a clean local rerun, remove the generated warehouse before running again:

```bash
rm -rf output/spark-warehouse
sbt run
```

The application also executes:

```sql
DROP TABLE IF EXISTS hotel_bookings
```

before recreating the managed table.

## 3. WSL Spark warnings

Spark may print warnings about:

- hostname resolving to a loopback address
- native Hadoop library not being available

These appeared during the verified run but did not prevent the application from completing successfully.

## 4. Verify a successful run

Look for:

```
DAY 21 COMPLETED SUCCESSFULLY
```

and an SBT success message at the end of the execution output.
