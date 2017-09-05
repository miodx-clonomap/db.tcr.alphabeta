name          := "db.tcr"
organization  := "era7bio"
description   := "A germline TCR database"

bucketSuffix  := "era7.com"

libraryDependencies ++=
  Seq(
    "ohnosequences" %% "statika"    % "2.0.0"
  ) ++ testDependencies

val testDependencies =
  Seq(
    "ohnosequences" %% "fastarious" % "0.11.0",
    "ohnosequences" %% "blast-api"  % "0.9.0"
  ) map { _ % Test }

generateStatikaMetadataIn(Compile)

// This includes tests sources in the assembled fat-jar:
fullClasspath in assembly := (fullClasspath in Test).value
