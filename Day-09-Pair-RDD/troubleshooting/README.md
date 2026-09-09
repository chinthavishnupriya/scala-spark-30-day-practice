# Troubleshooting

## Input parsing

Confirm that `input/transactions.txt` follows the expected comma-separated format.

## Aggregation results

Check the revenue formula: `Quantity × Price`.

## `reduceByKey` vs `groupByKey`

A shuffle for key-based grouping is expected. Prefer `reduceByKey` for direct aggregation such as sums and counts.

## Output verification

```bash
tail -n 40 output/result.txt
grep -n "DAY 09 COMPLETED SUCCESSFULLY" output/result.txt
```
