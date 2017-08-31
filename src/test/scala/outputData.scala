package ohnosequences.db.tcr.test

import ohnosequences.db.tcr._
import ohnosequences.fastarious._, fasta._
import scala.collection.JavaConverters._
import java.nio.file.Files
import java.io.File

case object outputData {

  val base: File =
    new File("sandbox/")

  def fastaFileFor(geneType: GeneType): File =
    new File(base, s"${geneType.species.toString}.tcr.beta.${geneType.segment.name}.fasta")

  def auxFileFor(species: Species, chain: Chain): File =
    new File(base, s"${species.toString}.tcr.${chain.name}.${Segment.J.name}.aux")
}
