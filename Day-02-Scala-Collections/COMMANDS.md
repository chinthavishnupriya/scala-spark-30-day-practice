# Day 02 — Commands Used

## Project navigation
cd ~/scala-spark-30-day-practice/Day-02-Scala-Collections

## Create project directories
mkdir -p code output project screenshots src/main/scala troubleshooting

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

## Create .gitignore
cat > .gitignore

## Check Git status
cd ~/scala-spark-30-day-practice
git status --short

## Stage Day 2
git add Day-02-Scala-Collections

## Commit Day 2
git commit -m "Complete Day 2 Scala Collections"

## Push Day 2
git push

## Add screenshots
git add Day-02-Scala-Collections/screenshots

## Commit screenshots
git commit -m "Add Day 2 screenshots"

## Push screenshots
git push

## Final Git verification
git status
git log --oneline -3
