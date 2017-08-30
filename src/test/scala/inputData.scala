package ohnosequences.db.tcr.test

import ohnosequences.db.tcr._
import ohnosequences.fastarious._, fasta._
import scala.collection.JavaConverters._
import java.nio.file.Files
import java.io.File
import ohnosequences.cosas.types._

case object inputData {

  def sequences(
    species : Species,
    chain   : Chain,
    segment : Segment
  )
  : Iterator[Either[ParseDenotationsError, FASTA.Value]] =
    // TODO fix this
    Files.lines(new File(s"data/${species.toString}.tcr.beta.${segment.name}.fasta").toPath)
      .iterator.asScala.buffered.parseFasta()

}
