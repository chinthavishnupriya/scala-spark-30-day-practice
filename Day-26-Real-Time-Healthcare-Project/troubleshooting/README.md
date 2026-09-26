# Day 26 Troubleshooting

## 1. Connection refused on port 9999

### Problem

Spark Streaming reported:

`Connection refused`

### Cause

`socketTextStream("localhost", 9999)` expects an external TCP socket server.

### Solution

Start netcat as the socket server:

```bash
nc -lk 9999
