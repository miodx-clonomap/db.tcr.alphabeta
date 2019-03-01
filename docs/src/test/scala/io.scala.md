
```scala
package era7bio.db.tcr.test

import era7bio.db.tcr._
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