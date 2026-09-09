# Day 06 Troubleshooting

## Hadoop NoSuchFileException

After the application completed successfully, Hadoop reported a
`NoSuchFileException` for a temporary `hadoop-client-api-3.5.0.jar`
under the SBT `target/bg-jobs` directory.

The application had already printed:

DAY 06 APPLICATION COMPLETED

and SBT reported:

[success]

Therefore, the exception occurred during post-execution cleanup and did
not prevent the Word Count application from completing successfully.

No application-code change was required.
