package ohnosequences.db.tcr

case object names {

  def ofGeneType(geneType: GeneType): String =
    s"${geneType.species.taxonomyID}.${geneType.chain.name}.${geneType.segment.name}"

  def ofAux(species: Species, chain: Chain): String =
    s"${species.taxonomyID}.${chain.name}.${Segment.J.name}.aux"

  def ofGeneTypeFASTA(geneType: GeneType): String =
    s"${ofGeneType(geneType)}.fasta"

  def ofGeneTypeBLASTDB(geneType: GeneType): String =
    s"${ofGeneType(geneType)}/blast/"
}
