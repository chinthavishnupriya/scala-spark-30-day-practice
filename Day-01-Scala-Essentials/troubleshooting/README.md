# Troubleshooting

## Common Issues

### `sbt` command not found

Verify that SBT is installed and available on `PATH`.

### Scala compilation errors

Check the Scala version in `build.sbt` and ensure the source code uses compatible syntax.

### Git files not appearing

Check `.gitignore` and run:

```bash
git status --short
```

### WSL path issues

Windows files can usually be accessed from WSL through `/mnt/c/`.
