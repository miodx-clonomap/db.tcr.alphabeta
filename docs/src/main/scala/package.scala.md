
```scala
package ohnosequences.db

import ohnosequences.awstools.s3._

package object tcr {

  val name: String =
    "ohnosequences.db.tcr"

  private val metadata =
    generated.metadata.tcr

  def projectID: String =
    s"${metadata.organization}.${metadata.artifact}.${metadata.version}"

  val s3prefix: S3Folder =
    s3"resources.ohnosequences.com" /
    metadata.organization           /
    metadata.artifact               /
    metadata.version                /
}

```




[test/scala/outputData.scala]: ../../test/scala/outputData.scala.md
[test/scala/genericTests.scala]: ../../test/scala/genericTests.scala.md
[test/scala/inputData.scala]: ../../test/scala/inputData.scala.md
[test/scala/io.scala]: ../../test/scala/io.scala.md
[test/scala/humanTRB.scala]: ../../test/scala/humanTRB.scala.md
[main/scala/package.scala]: package.scala.md
[main/scala/model.scala]: model.scala.md
[main/scala/data.scala]: data.scala.md