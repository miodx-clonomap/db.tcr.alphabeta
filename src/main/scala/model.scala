package ohnosequences.db.tcr

sealed
trait Species {

  val taxonomyID: String
}

case object Species {
  case object human extends Species { val taxonomyID = ??? }
  case object mouse extends Species { val taxonomyID = ??? }
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

sealed
trait GeneType {

  def species: Species
  def chain: Chain
  def segment: Segment
}

sealed
trait Gene {

  val id: String
  val geneType: GeneType
}
