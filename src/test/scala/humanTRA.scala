package era7bio.db.tcr.test

import era7bio.db.tcr._

/** Human TCR β data generation */
class HumanTRA extends TestsFor(Species.human, Chain.TRA, Set(Segment.V, Segment.J))
