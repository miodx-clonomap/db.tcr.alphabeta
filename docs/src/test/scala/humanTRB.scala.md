
```scala
package ohnosequences.db.tcr.test

import ohnosequences.db.tcr._
import ohnosequences.cosas._
import ohnosequences.fastarious.fasta._

/** Human TCR β data generation */
class HumanTRB extends TestsFor(Species.human, Chain.TRB, Set(Segment.V, Segment.D, Segment.J))

```




[main/scala/names.scala]: ../../main/scala/names.scala.md
[main/scala/data.scala]: ../../main/scala/data.scala.md
[main/scala/package.scala]: ../../main/scala/package.scala.md
[main/scala/model.scala]: ../../main/scala/model.scala.md
[test/scala/io.scala]: io.scala.md
[test/scala/inputData.scala]: inputData.scala.md
[test/scala/humanTRB.scala]: humanTRB.scala.md
[test/scala/genericTests.scala]: genericTests.scala.md
[test/scala/outputData.scala]: outputData.scala.md
[test/scala/humanTRA.scala]: humanTRA.scala.md
[test/scala/dataGeneration.scala]: dataGeneration.scala.md