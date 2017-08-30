package ohnosequences.db.tcr.test

import ohnosequences.db.tcr._
import ohnosequences.fastarious._, fasta._
import scala.collection.JavaConverters._
import java.nio.file.Files
import java.io.File
import ohnosequences.cosas.types._

case object inputData {

  private
  def lines(file: File): Iterator[String] =
    Files.lines(file.toPath).iterator.asScala

  def sequences(
    species : Species,
    chain   : Chain,
    segment : Segment
  )
  : Iterator[Either[ParseDenotationsError, FASTA.Value]] =
    // TODO fix this
    lines( new File(s"data/${species.toString}.tcr.beta.${segment.name}.fasta") )
      .buffered.parseFasta()

  def auxLines(species: Species): Iterator[String] =
    lines( new File(s"data/${species.toString}.tcr.beta.J.aux") )

  def auxIDs(species: Species): Iterator[String] =
    auxLines(species) map { _.takeWhile(_ != '\t') }
}
