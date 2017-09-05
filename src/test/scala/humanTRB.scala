package era7bio.db.tcr.test

import era7bio.db.tcr._
import ohnosequences.cosas._
import ohnosequences.fastarious.fasta._

/** Human TCR β data generation */
class HumanTRB extends TestsFor(Species.human, Chain.TRB, Set(Segment.V, Segment.D, Segment.J))
