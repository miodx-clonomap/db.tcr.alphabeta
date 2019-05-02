resolvers ++= Seq(
  "repo.jenkins-ci.org" at "https://repo.jenkins-ci.org/public"
)

addSbtPlugin("com.miodx.sbt.plugins" % "nice-sbt-settings" % "0.10.1")

addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.7.0")
