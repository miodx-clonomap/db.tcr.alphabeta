
```scala
package ohnosequences.db.tcr

import ohnosequences.awstools.s3._

case object data {

  /** The base folder under which all data for this @param geneType will be. */
  def base(geneType: GeneType): S3Folder =
    s3prefix                    /
    geneType.species.taxonomyID /
    geneType.chain.name         /
    geneType.segment.name       /

  def fasta(geneType: GeneType): S3Object =
    base(geneType) / s"${geneType.ID}.fasta"

  def blastDB(geneType: GeneType): S3Folder =
    base(geneType) / "blast" /

  def igblastAux(species: Species, chain: Chain): S3Object = {

    val geneType =
      GeneType(species, chain, Segment.J)

    blastDB(geneType) / s"${geneType.ID}.aux"
  }

  def fastaHeader(gene: Gene): String =
    s"${gene.ID}|${projectID}"
}

```




[test/scala/outputData.scala]: ../../test/scala/outputData.scala.md
[test/scala/genericTests.scala]: ../../test/scala/genericTests.scala.md
[test/scala/inputData.scala]: ../../test/scala/inputData.scala.md
[test/scala/io.scala]: ../../test/scala/io.scala.md
[test/scala/humanTRB.scala]: ../../test/scala/humanTRB.scala.md
[main/scala/package.scala]: package.scala.md
[main/scala/model.scala]: model.scala.md
[main/scala/data.scala]: data.scala.md