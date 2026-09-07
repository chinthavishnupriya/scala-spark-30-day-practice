# Day 03 — Commands Used

## Project navigation

cd ~/scala-spark-30-day-practice/Day-03-Spark-Setup

## Create project directories

mkdir -p code input output project screenshots src/main/scala troubleshooting

## Create/check project files

cat > .gitignore
cat > project/build.properties
cat > build.sbt
cat > input/sample.txt
cat > src/main/scala/Day03.scala

## Copy source file

cp src/main/scala/Day03.scala code/Day03.scala

## Check Spark configuration in source

grep -n "master" src/main/scala/Day03.scala

## Compile the Spark project

sbt clean compile

## Run with 2 local cores

sbt "run 2"

## Run with 4 local cores

sbt "run 4"

## Save execution output

sbt "run 2" > output/result_local2.txt 2>&1
sbt "run 4" > output/result_local4.txt 2>&1

## Verify output

grep -E "Spark version|Master|Default parallelism|Number of lines|DAY 03 APPLICATION COMPLETED|success" output/result_local2.txt
grep -E "Spark version|Master|Default parallelism|Number of lines|DAY 03 APPLICATION COMPLETED|success" output/result_local4.txt

## Check screenshots

ls -lh screenshots/

## Copy screenshots from Windows into WSL

cp "/mnt/c/Users/chint/OneDrive/图片/Screenshots/02_day03_execution_local2.png.png" screenshots/02_day03_execution_local2.png
cp "/mnt/c/Users/chint/OneDrive/图片/Screenshots/03_day03_execution_local4.png.png" screenshots/03_day03_execution_local4.png

## Verify project files

find . -maxdepth 5 -type f | sort

## Git status

cd ~/scala-spark-30-day-practice
git status --short

## Stage Day 03

git add Day-03-Spark-Setup

## Commit Day 03

git commit -m "Complete Day 3 Spark Setup"

## Push Day 03

git push origin main

## Final Git verification

git status
git log --oneline -3
