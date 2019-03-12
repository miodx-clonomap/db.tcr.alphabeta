
```scala
package ohnosequences.db.tcr.test

import ohnosequences.db.tcr._
import ohnosequences.fastarious._, fasta._
import scala.collection.JavaConverters._
import java.nio.file.Files
import java.io.File
import ohnosequences.cosas.types._

/**
  Input data for database generation.

  Most methods here have input a GeneType; for each onesuch there should be a
  FASTA file, containing all the gene sequences for it. The other part of the
  input are IgBLAST `aux` files; there should be one per `(species, chain)`
  pair.
*/
case object inputData {

  val base: File =
    new File("data/")

  def geneTypeBase(geneType: GeneType): File =
    new File(base, s"${geneType.ID}")

  /** This class represents a row from an IgBLAST `aux` file. */
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

  /** The sequences for this gene type. */
  def sequences(geneType: GeneType): Iterator[Either[ParseDenotationsError, FASTA.Value]] =
    io sequences new File(geneTypeBase(geneType), names ofGeneTypeFASTA geneType)

  /** The IDs for geneType coming from its FASTA file */
  def idsFor(geneType: GeneType): List[String] =
    inputData.sequences(geneType)
      .collect { case Right(fa) => fa }
      .map(fa => fa.getV(header).id)
      .toList

  /** lines for the */
  def auxLines(species: Species, chain: Chain): Iterator[String] =
    io lines new File(geneTypeBase(GeneType(species, chain, Segment.J)), names.ofAux(species, chain))

  /** Parse a `Chain` object from IgBLAST chain representation */
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

  def aux(species: Species, chain: Chain): Iterator[Aux] =
    auxLines(species, chain)
      .map(_.split('\t'))
      .filter(_.length == 4)
      .map(
        { fields =>
            Aux(
              id          = fields(0),
              codonStart  = fields(1).toInt,
              // TODO fix this; better throw something than this
              chain       = parseChain(fields(2)) getOrElse Chain.TRB,
              stopCDR3    = fields(3).toInt
            )
          }
      )

  def auxIDs(species: Species, chain: Chain): Iterator[String] =
    auxLines(species, chain) map { _.takeWhile(_ != '\t') }
}

```




[main/scala/names.scala]: ../../main/scala/names.scala.md
[main/scala/data.scala]: ../../main/scala/data.scala.md
[main/scala/package.scala]: ../../main/scala/package.scala.md
[main/scala/model.scala]: ../../main/scala/model.scala.md
[test/scala/io.scala]: io.scala.md
[test/scala/inputData.scala]: inputData.scala.md
[test/scala/humanTRB.scala]: humanTRB.scala.md
[test/scala/genericTests.scala]: genericTests.scala.md
[test/scala/outputData.scala]: outputData.scala.md
[test/scala/humanTRA.scala]: humanTRA.scala.md
[test/scala/dataGeneration.scala]: dataGeneration.scala.md