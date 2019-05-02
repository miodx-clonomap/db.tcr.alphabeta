name          := "db.tcr.alphabeta"
organization  := "com.miodx.clonomap"
description   := "A germline TCR database"
version       := "0.4.0"
bucketSuffix  := "era7.com"

libraryDependencies ++= Seq(
  "com.miodx.common" %% "aws-scala-tools" % "0.21.0"
) ++ testDependencies

val testDependencies = Seq(
  "com.miodx.clonomap" %% "fastarious" % "0.12.0",
  "com.miodx.clonomap" %% "blast-api"  % "0.11.1",
  "org.scalatest"      %% "scalatest"  % "3.0.4"
) map { _ % Test }

// For constructing the S3 address of the generated data
enablePlugins(BuildInfoPlugin)
buildInfoPackage := s"${organization.value}.${name.value}"
buildInfoKeys := Seq[BuildInfoKey](
  organization,
  normalizedName,
  version
)
