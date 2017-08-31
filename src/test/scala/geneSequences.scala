package ohnosequences.db.tcr.test

import ohnosequences.db.tcr._
import ohnosequences.cosas._
import ohnosequences.fastarious.fasta._

class HumanTRB extends WellFormedInputs(Species.human, Chain.TRB, Set(Segment.V, Segment.D, Segment.J))
