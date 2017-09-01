
```scala
package ohnosequences.db.tcr

sealed
trait Species {

  val taxonomyID: String
}

case object Species {
  case object human extends Species { val taxonomyID = "9606"   }
  case object mouse extends Species { val taxonomyID = "10090"  }
}

sealed
trait Chain {

  lazy val name: String =
    toString
}

case object Chain {
  case object TRA extends Chain
  case object TRB extends Chain
}

sealed
trait Segment {

  lazy val name: String =
    toString
}

case object Segment {
  case object V extends Segment
  case object D extends Segment
  case object J extends Segment
}

final
case class GeneType(
  val species : Species,
  val chain   : Chain,
  val segment : Segment
)
{
  final
  def ID: String =
    s"${species.taxonomyID}.${chain.name}.${segment.name}"
}

final
case class Gene(
  val name      : String,
  val geneType  : GeneType
)
{
  final
  def ID: String =
    s"${geneType.ID}.${name}"
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