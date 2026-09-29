# Day 24 — Stateless vs Stateful Streaming Output

## Execution Evidence

The Day 24 README documents micro-batch results comparing stateless and stateful transaction counts.

### Observed Behavior

```text
STATELESS RESULT
STATEFUL RESULT
```

The documented test demonstrates that:

- The stateless result contains counts from the current micro-batch.
- The stateful result maintains accumulated counts across batches.
- At the beginning, both results can be equal because there is no previous state.
- After another batch arrives, the stateful result includes the previous accumulated count.

### Evidence

- Streaming context startup screenshot is available.
- The project contains screenshots documenting the streaming demonstration.
- The implementation uses keyed transaction counts and accumulated state.

> This file records the output behavior documented by the Day 24 project README. It does not invent numeric results that are not recorded there.
