package ohnosequences.db.tcr.test

import ohnosequences.db.tcr._
import ohnosequences.cosas._
import ohnosequences.fastarious.fasta._

class HumanTRBInputs          extends WellFormedInputs(Species.human, Chain.TRB, Set(Segment.V, Segment.D, Segment.J))
class HumanTRBFastaGeneration extends GenerateFASTA(Species.human, Chain.TRB, Set(Segment.V, Segment.D, Segment.J))
class HumanTRBAuxFiles        extends AuxFileGeneration(Species.human, Chain.TRB)
