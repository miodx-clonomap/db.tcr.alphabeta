package ohnosequences.db.tcr.test

import ohnosequences.db.tcr._
import ohnosequences.cosas._
import ohnosequences.fastarious.fasta._

class WellFormedInputs extends org.scalatest.FunSuite {

  val segments: Set[Segment] =
    Set(Segment.V, Segment.D, Segment.J)

  def geneType(segment: Segment): GeneType =
    GeneType(Species.human, Chain.TRB, segment)

  def idsFor(segment: Segment): List[String] =
    inputData.sequences(geneType(segment))
      .collect { case Right(fa) => fa }
      .map(fa => fa.getV(header).id)
      .toList

  test("TCR beta human: well-formed FASTA files") {

    segments foreach { segment =>
      inputData.sequences(geneType(segment)) foreach { lr =>
        assert( lr.isRight )
      }
    }
  }

  test("TCR beta human: FASTA files have no duplicate IDs") {

    segments foreach { segment =>

      val ids =
        idsFor(segment)

      assert { ids.distinct == ids }
    }
  }

  test("TCR beta human: all J IDs are in the aux file, same order") {

    assert { idsFor(Segment.J) == inputData.auxIDs(Species.human).toList }
  }
}
