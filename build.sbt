name          := "db.tcr"
organization  := "ohnosequences"
description   := "db.tcr project"

bucketSuffix  := "era7.com"

libraryDependencies ++=
  Seq(
    "ohnosequences" %% "statika" % "2.0.0"
  )

generateStatikaMetadataIn(Compile)

// This includes tests sources in the assembled fat-jar:
fullClasspath in assembly := (fullClasspath in Test).value


// // For resolving dependency versions conflicts:
// dependencyOverrides ++= Set()

// // If you need to deploy this project as a Statika bundle:
// generateStatikaMetadataIn(Compile)

// // This includes tests sources in the assembled fat-jar:
// fullClasspath in assembly := (fullClasspath in Test).value

// // This turns on fat-jar publishing during release process:
// publishFatArtifact in Release := true
