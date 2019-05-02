package ohnosequences.db.tcr.test

import ohnosequences.db.tcr._
import ohnosequences.fastarious._, fasta._
import scala.collection.JavaConverters._
import java.nio.file.Files
import java.io.File
import ohnosequences.cosas.types._

case object outputData {

  val base: File =
    new File("sandbox/")

  def geneTypeBase(geneType: GeneType): File =
    new File(base, s"${geneType.ID}")

  def fastaFileFor(geneType: GeneType): File =
    new File(base, names ofGeneTypeFASTA geneType)
    //  s"${geneType.species.toString}.tcr.beta.${geneType.segment.name}.fasta")

  def auxFileFor(species: Species, chain: Chain): File =
    new File(base, names.ofAux(species, chain))
      //  s"${species.toString}.tcr.${chain.name}.${Segment.J.name}.aux")

  def sequences(geneType: GeneType): Iterator[Either[ParseDenotationsError, FASTA.Value]] =
    io sequences fastaFileFor(geneType)

  def blastDBFolderFor(geneType: GeneType): File =
    new File(geneTypeBase(geneType), "blast/")

  def blastDBFileFor(geneType: GeneType): File =
    new File(blastDBFolderFor(geneType), s"${geneType.ID}")

  def sequencesIDs(geneType: GeneType): Iterator[String] =
    io sequencesIDs fastaFileFor(geneType)

  def auxIDs(species: Species, chain: Chain): Iterator[String] =
    io auxIDs auxFileFor(species, chain)
}
