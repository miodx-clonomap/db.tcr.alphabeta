package ohnosequences.db.tcr

import ohnosequences.awstools.s3._

/**
  All the S3 addresses for this release data are here.
*/
case object data {

  /** The base folder under which all data for this gene type will be. */
  def base(geneType: GeneType): S3Folder =
    s3prefix                    /
    geneType.species.taxonomyID /
    geneType.chain.name         /
    geneType.segment.name       /

  /** FASTA for this gene type. */
  def fasta(geneType: GeneType): S3Object =
    base(geneType) / s"${geneType.ID}.fasta"

  /** BLAST database for this gene type */
  def blastDB(geneType: GeneType): S3Folder =
    base(geneType) / "blast" /

  /** BLAST database title/name for this gene type */
  def blastDBName(geneType: GeneType): String =
    s"${geneType.ID}"

  def igblastAux(species: Species, chain: Chain): S3Object = {

    val geneType =
      GeneType(species, chain, Segment.J)

    blastDB(geneType) / s"${geneType.ID}.aux"
  }

  def fastaHeader(gene: Gene): String =
    s"${gene.ID}|${projectID}"
}
