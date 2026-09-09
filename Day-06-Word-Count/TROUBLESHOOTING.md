# Troubleshooting

- Confirm `input/application.log` exists before running.
- Verify lowercase normalization and punctuation removal when word counts differ.
- Use `tail -n 40 output/result.txt` to inspect the final output.
- `reduceByKey` creating a shuffle is expected behavior.
- For Java 17 module errors, verify the runtime options in `build.sbt` and `.jvmopts`.
