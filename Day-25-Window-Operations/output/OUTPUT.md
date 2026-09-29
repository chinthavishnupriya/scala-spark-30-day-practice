# Day 25 — Window Operations Output

## Execution Evidence

The Day 25 README documents a working window-processing demonstration using DStreams.

### Configuration

```text
Batch interval: 5 seconds
Window size: 20 seconds
Slide interval: 10 seconds
```

### Operations Demonstrated

- `countByWindow`
- `reduceByKeyAndWindow`
- Transaction count inside the current window
- Rolling transaction totals by account

### Documented Result

The README records an observed `countByWindow` result and provides screenshots for:

- Streaming context startup
- Window input batch
- Count-by-window processing

The runtime checkpoint directory is generated locally and is intentionally not committed to Git.

> This file records the window configuration and output evidence documented by the Day 25 project README. Numeric output is not fabricated where the README does not provide the complete value.
