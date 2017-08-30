package ohnosequences.db.tcr.test

import ohnosequences.db.tcr._

class WellFormedInputs extends org.scalatest.FunSuite {

  val segments: Set[Segment] =
    Set(Segment.V, Segment.D, Segment.J)

  test("human TCR beta") {

    segments foreach { segment =>
      inputData.sequences(Species.human, Chain.TRB, segment) foreach { lr =>
        assert( lr.isRight )
      }
    }
  }
}
