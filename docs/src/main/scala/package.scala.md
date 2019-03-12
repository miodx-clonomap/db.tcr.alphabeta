
```scala
package ohnosequences.db

import ohnosequences.awstools.s3._
import ohnosequences.db.tcr.alphabeta.BuildInfo

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

  val projectID: String = Seq(
    BuildInfo.organization,
    BuildInfo.normalizedName,
    BuildInfo.version
  ).mkString(".")

  val s3prefix: S3Folder =
    s3"resources.ohnosequences.com" /
    BuildInfo.organization /
    BuildInfo.normalizedName /
    BuildInfo.version /
}

```




[main/scala/names.scala]: names.scala.md
[main/scala/data.scala]: data.scala.md
[main/scala/package.scala]: package.scala.md
[main/scala/model.scala]: model.scala.md
[test/scala/io.scala]: ../../test/scala/io.scala.md
[test/scala/inputData.scala]: ../../test/scala/inputData.scala.md
[test/scala/humanTRB.scala]: ../../test/scala/humanTRB.scala.md
[test/scala/genericTests.scala]: ../../test/scala/genericTests.scala.md
[test/scala/outputData.scala]: ../../test/scala/outputData.scala.md
[test/scala/humanTRA.scala]: ../../test/scala/humanTRA.scala.md
[test/scala/dataGeneration.scala]: ../../test/scala/dataGeneration.scala.md