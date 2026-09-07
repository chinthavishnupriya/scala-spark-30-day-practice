# Day 04 - Commands Used

## Project Setup

cd ~/scala-spark-30-day-practice
mkdir -p Day-04-RDD-Creation/{code,input,output,project,screenshots,troubleshooting,src/main/scala}
cd Day-04-RDD-Creation

## Project Configuration

cat > .gitignore
cat > project/build.properties
cat > build.sbt

## Input Files

cat > input/customers.txt
cat > input/sales.txt

## Source Code

cat > src/main/scala/Day04.scala
cp src/main/scala/Day04.scala code/Day04.scala

## Compilation

sbt compile

## Execution

sbt "run"

## Save Execution Output

sbt "run" 2>&1 | tee output/day04_run.txt

## README Verification

wc -l README.md
tail -10 README.md

## File Verification

find . -maxdepth 3 -type f | sort

## Git Verification

git status
git add Day-04-RDD-Creation/
git status
git commit -m "Complete Day 4 RDD Creation and Operations"
git push origin main
git status

## Git Ignore Troubleshooting

cat -n .gitignore
git status --ignored --short
git add .gitignore
git status --short
