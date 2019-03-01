name          := "db.tcr.miodx"
organization  := "ohnosequences"
description   := "A germline TCR database"

bucketSuffix  := "era7.com"

libraryDependencies ++= Seq(
  "ohnosequences" %% "aws-scala-tools" % "0.18.1"
) ++ testDependencies

val testDependencies = Seq(
  "ohnosequences" %% "fastarious" % "0.11.0",
  "ohnosequences" %% "blast-api"  % "0.9.0"
) map { _ % Test }

// For constructing the S3 address of the generated data
enablePlugins(BuildInfoPlugin)
buildInfoPackage := s"${organization.value}.${name.value}"
buildInfoKeys := Seq[BuildInfoKey](
  organization,
  normalizedName,
  version
)
