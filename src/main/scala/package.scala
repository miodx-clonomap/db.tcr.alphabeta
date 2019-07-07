package ohnosequences.db

import ohnosequences.awstools.s3._
import com.miodx.clonomap.db.tcr.alphabeta.BuildInfo

/**
  = db.tcr =

  The main artifact contains S3 addresses for this release data. Everything
  will be under `s3Prefix`, and you can find the relevant S3 addresses through
  methods in the [[data]] object.

  Most data depends on a particular [[GeneType]], which is formed by a
  combination of

  - [[Species]] (such as "Human"),
  - [[Chain]] (α, β), and
  - [[Segment]] (J, for example)
*/
package object tcr {

  val config: TcrConfig = TcrConfig
  val projectID: String = Seq(
    BuildInfo.organization,
    BuildInfo.normalizedName,
    BuildInfo.version
  ).mkString(".")

  val s3prefix: S3Folder = S3Bucket(config.s3bucket) / "ohnosequences"/"db-tcr-alphabeta"/"0.4.0"/
//    S3Bucket(config.s3bucket) /
//    BuildInfo.organization /
//    BuildInfo.normalizedName /
//    BuildInfo.version /
}
