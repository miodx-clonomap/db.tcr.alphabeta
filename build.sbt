name          := "db.tcr.alphabeta"
organization  := "com.miodx.clonomap"
version       := "0.4.0"
description   := "A germline TCR database"
scalaVersion  := "2.11.11"

bucketSuffix  := "era7.com"

libraryDependencies ++= Seq(
  "com.miodx.common" %% "aws-scala-tools" % "0.19.0"
) ++ testDependencies

val testDependencies = Seq(
  "com.miodx.common"   %% "fastarious" % "0.11.0",
  "com.miodx.clonomap" %% "blast-api"  % "0.10.0",
  "org.scalatest"      %% "scalatest"  % "3.0.1"
) map { _ % Test }

// For constructing the S3 address of the generated data
enablePlugins(BuildInfoPlugin)
buildInfoPackage := s"${organization.value}.${name.value}"
buildInfoKeys := Seq[BuildInfoKey](
  organization,
  normalizedName,
  version
)
