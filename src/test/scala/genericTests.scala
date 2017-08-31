package ohnosequences.db.tcr.test

import ohnosequences.db.tcr._
import ohnosequences.cosas._
import ohnosequences.fastarious.fasta._

abstract class WellFormedInputs(
  val species : Species       ,
  val chain   : Chain         ,
  val segments: Set[Segment]
)
extends org.scalatest.FunSuite {

  val geneTypes =
    segments map { GeneType(species, chain, _) }

  def idsFor(segment: Segment): List[String] =
    inputData.sequences(GeneType(species, chain, segment))
      .collect { case Right(fa) => fa }
      .map(fa => fa.getV(header).id)
      .toList

  val description: String =
    s"${species} ${chain}:"

  test(s"${description} well-formed FASTA files") {

    geneTypes foreach { geneType =>
      (inputData sequences geneType) foreach { lr => assert( lr.isRight ) }
    }
  }

  test(s"${description} FASTA files have no duplicate IDs") {

    segments foreach { segment =>

      val ids =
        idsFor(segment)

      assert { ids.distinct == ids }
    }
  }

  test(s"${description} all J IDs are in the aux file, same order") {

    assert { idsFor(Segment.J) == inputData.auxIDs(species).toList }
  }
}
