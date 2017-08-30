package ohnosequences.db.tcr

import ohnosequences.awstools.s3._

case object data {

  def base(
    species : Species,
    chain   : Chain,
    segment : Segment
  )
  : S3Object =
    s3prefix/species.taxonomyID/chain.name/segment.name

  def objectPrefix(
    species : Species,
    chain   : Chain,
    segment : Segment
  )
  : String =
    s"${species.taxonomyID}.${chain.name}.${segment.name}"

  def fasta(
    species : Species,
    chain   : Chain,
    segment : Segment
  )
  : S3Object =
    base(species, chain, segment) /
      s"${objectPrefix(species, chain, segment)}.fasta"

  def blastDB(
    species : Species,
    chain   : Chain,
    segment : Segment
  )
  : S3Folder =
    base(species, chain, segment)/"blast"/

  def igblastAux(
    species : Species,
    chain   : Chain
  )
  : S3Object =
    base(species, chain, Segment.J)/"blast"/
      s"${objectPrefix(species, chain, Segment.J)}.aux"

  def fastaHeader(gene: Gene): String =
    s"${gene.ID}|${projectID}"
}
