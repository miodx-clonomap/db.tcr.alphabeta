package ohnosequences.db.tcr

case class TcrConfig(
  s3bucket: String
)

object TcrConfig extends TcrConfig(
  s3bucket = "resources.miodx.com"//"miodx-clonomap-backups-dev"
)
