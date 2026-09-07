# Day 01 — Commands Used

## Project navigation
cd ~/scala-spark-30-day-practice/Day-01-Scala-Essentials

## Create project directories
mkdir -p code output screenshots troubleshooting
mkdir -p src/main/scala project

## Create/check SBT project files
cat build.sbt
cat project/build.properties

## Compile
sbt clean compile

## Run
sbt run

## Save execution output
sbt run > output/result.txt 2>&1

## Verify project files
find . -maxdepth 5 -type f | sort

## Git verification
cd ~/scala-spark-30-day-practice
git status --short

## Git history
git log --oneline -3
