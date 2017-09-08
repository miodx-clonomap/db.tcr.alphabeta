
```scala
package era7bio.db.tcr

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