package era7bio.db.tcr

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
