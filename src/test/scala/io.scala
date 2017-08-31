package ohnosequences.db.tcr.test

import ohnosequences.db.tcr._
import scala.collection.JavaConverters._
import java.nio.file.Files
import java.io.{ File, PrintWriter }
import ohnosequences.cosas.types._
import ohnosequences.fastarious._, fasta._

case object io {

  def printToFile(f: File)(op: PrintWriter => Unit) {
    val p =
      new java.io.PrintWriter(f)

    try { op(p) } finally { p.close }
  }

  def lines(file: File): Iterator[String] =
    Files.lines(file.toPath).iterator.asScala

  def sequences(file: File): Iterator[Either[ParseDenotationsError, FASTA.Value]] =
    lines(file)
      .buffered.parseFasta()

  def sequencesIDs(file: File): Iterator[String] =
    sequences(file)
      .collect { case Right(fa) => fa.getV(header).id }

  def auxIDs(file: File): Iterator[String] =
    lines(file) map { _.takeWhile(_ != '\t') }
}
