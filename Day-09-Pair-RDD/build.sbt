ThisBuild / scalaVersion := "2.13.18"

lazy val root = (project in file("."))
  .settings(
    name := "Day-09-Pair-RDD",
    version := "1.0",
    libraryDependencies ++= Seq(
      "org.apache.spark" %% "spark-core" % "4.2.0",
      "org.apache.spark" %% "spark-sql" % "4.2.0"
    ),
    Compile / run / fork := true,
    Compile / run / javaOptions ++= Seq(
      "-Dspark.serializer=org.apache.spark.serializer.JavaSerializer",
      "--add-opens=java.base/java.nio=ALL-UNNAMED",
      "--add-opens=java.base/sun.nio.ch=ALL-UNNAMED",
      "--add-opens=java.base/java.lang.invoke=ALL-UNNAMED",
      "--add-opens=java.base/java.util=ALL-UNNAMED"
    )
  )
