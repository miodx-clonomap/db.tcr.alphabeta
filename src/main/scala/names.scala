package ohnosequences.db.tcr

/** Functions to generate names of gene types */
case object names {

  /** Build the name of the specified gene type */
  def ofGeneType(geneType: GeneType): String =
    s"${geneType.species.taxonomyID}.${geneType.chain.name}.${geneType.segment.name}"

  /** Build the name of the specified [[Species]] and [[Chain]] */
  def ofAux(species: Species, chain: Chain): String =
    s"${species.taxonomyID}.${chain.name}.${Segment.J.name}.aux"

  /** Build the name of the FASTA of the specified gene type */
  def ofGeneTypeFASTA(geneType: GeneType): String =
    s"${ofGeneType(geneType)}.fasta"

  /** Build the name of the BLAST database of the specified gene type */
  def ofGeneTypeBLASTDB(geneType: GeneType): String =
    s"${ofGeneType(geneType)}/blast/"
}
