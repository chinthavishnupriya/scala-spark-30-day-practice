ThisBuild / scalaVersion := "2.13.18"

lazy val root = (project in file("."))
  .settings(
    name := "day04-rdd-creation",
    version := "0.1.0",
    libraryDependencies ++= Seq(
      "org.apache.spark" %% "spark-core" % "4.2.0",
      "org.apache.spark" %% "spark-sql" % "4.2.0"
    )
  )
