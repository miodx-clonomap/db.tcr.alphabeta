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

  def sequences(geneType: GeneType): Iterator[Either[ParseDenotationsError, FASTA.Value]] =
    // TODO fix this
    lines( new File(s"data/${geneType.species.toString}.tcr.beta.${geneType.segment.name}.fasta") )
      .buffered.parseFasta()

  def auxLines(species: Species): Iterator[String] =
    lines( new File(s"data/${species.toString}.tcr.beta.J.aux") )

  def parseChain(rep: String): Option[Chain] =
    rep match {
      case "JA" => Some(Chain.TRA)
      case "JB" => Some(Chain.TRB)
      case _    => None
    }

  def chainToAuxFormat(chain: Chain): String =
    chain match {
      case Chain.TRA => "JA"
      case Chain.TRB => "JB"
    }



  def aux(species: Species): Iterator[Aux] =
    auxLines(species)
      .map(_.split('\t'))
      .filter(_.length == 4)
      .map(
        { fields =>
            Aux(
              id          = fields(0),
              codonStart  = fields(1).toInt,
              chain       = parseChain(fields(2)) getOrElse Chain.TRB,
              stopCDR3    = fields(3).toInt
            )
          }
      )

  def auxIDs(species: Species): Iterator[String] =
    auxLines(species) map { _.takeWhile(_ != '\t') }

  case class Aux(
    val id          : String,
    val codonStart  : Int   ,
    val chain       : Chain ,
    val stopCDR3    : Int
  )
  {

    def toTSVRow: String =
      Seq(id, codonStart.toString, chainToAuxFormat(chain), stopCDR3.toString).mkString("\t")
  }
}
