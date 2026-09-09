# Troubleshooting

- Verify that `input/customers.txt` and `input/sales.txt` exist before running.
- If an input path fails, confirm the application is being run from the Day 4 directory.
- Check partition output with `find . -maxdepth 3 -type f | sort` and the saved result file.
- For Java 17 runtime errors, verify the Spark module-opening options in the project configuration.
