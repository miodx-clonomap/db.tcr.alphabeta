package ohnosequences.db.tcr.test

import ohnosequences.db.tcr._

/** Human TCR β data generation */
class HumanTRA extends TestsFor(Species.human, Chain.TRA, Set(Segment.V, Segment.J))
