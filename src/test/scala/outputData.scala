package ohnosequences.db.tcr.test

import ohnosequences.db.tcr._
import ohnosequences.fastarious._, fasta._
import scala.collection.JavaConverters._
import java.nio.file.Files
import java.io.File

case object outputData {

  def fastaFileFor(
    species : Species,
    chain   : Chain,
    segment : Segment
  )
  : File =
    new File(s"sandbox/${species.toString}.tcr.beta.${segment.name}.fasta")
}
