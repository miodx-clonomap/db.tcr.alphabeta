package ohnosequences.db.tcr.test

import ohnosequences.db.tcr._
import ohnosequences.cosas._
import ohnosequences.fastarious.fasta._

class WellFormedInputs extends org.scalatest.FunSuite {

  val segments: Set[Segment] =
    Set(Segment.V, Segment.D, Segment.J)

  test("well-formed human TCR beta FASTA files") {

    segments foreach { segment =>
      inputData.sequences(Species.human, Chain.TRB, segment) foreach { lr =>
        assert( lr.isRight )
      }
    }
  }

  test("TCR beta FASTA files have no duplicate IDs") {

    segments foreach { segment =>

      val ids =
        inputData.sequences(Species.human, Chain.TRB, segment)
          .collect { case Right(fa) => fa }
          .map(fa => fa.getV(header).id)
          .toList

      assert { ids.distinct == ids }
    }
  }
}
