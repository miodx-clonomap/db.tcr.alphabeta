
```scala
package era7bio.db.tcr

import ohnosequences.awstools.s3._

/**
  All the S3 addresses for this release data are here.
*/
case object data {

  /** The base folder under which all data for this gene type will be. */
  def base(geneType: GeneType): S3Folder =
    s3prefix                    /
    (names ofGeneType geneType) /

  /** FASTA for this gene type. */
  def fasta(geneType: GeneType): S3Object =
    base(geneType) / (names ofGeneTypeFASTA geneType)

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
    s"${gene.name} ${projectID}"
}

```




[test/scala/humanTRA.scala]: ../../test/scala/humanTRA.scala.md
[test/scala/outputData.scala]: ../../test/scala/outputData.scala.md
[test/scala/dataGeneration.scala]: ../../test/scala/dataGeneration.scala.md
[test/scala/genericTests.scala]: ../../test/scala/genericTests.scala.md
[test/scala/inputData.scala]: ../../test/scala/inputData.scala.md
[test/scala/io.scala]: ../../test/scala/io.scala.md
[test/scala/humanTRB.scala]: ../../test/scala/humanTRB.scala.md
[main/scala/package.scala]: package.scala.md
[main/scala/model.scala]: model.scala.md
[main/scala/names.scala]: names.scala.md
[main/scala/data.scala]: data.scala.md