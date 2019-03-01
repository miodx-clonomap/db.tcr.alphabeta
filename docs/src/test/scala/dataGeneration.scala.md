
```scala
package ohnosequences.db.tcr.test

import ohnosequences.db.tcr._
import ohnosequences.cosas._, klists._, types._
import ohnosequences.fastarious.fasta._
import java.nio.file.Files
import ohnosequences.test._
import ohnosequences.awstools.s3._
import util.{ Success, Failure }
import ohnosequences.blast._, api._, outputFields._

case object dataGeneration {

  def writeOutputFASTA(geneType: GeneType): Unit = {

    val writeTo =
      outputData fastaFileFor geneType

    val deleteIfThere =
      Files deleteIfExists writeTo.toPath

    val writeFiles =
      inputData.sequences(geneType)
        .collect({ case Right(a) => a })
        .map(
          { fa =>

            val gene =
              Gene(fa.getV(header).id, geneType)

            FASTA(
              header( FastaHeader(data.fastaHeader(gene)) ) ::
              sequence( fa.getV(sequence) )                 ::
              *[AnyDenotation]
            )
          }
        )
        .appendTo(writeTo)
  }

  def writeAuxFile(species: Species, chain: Chain): Unit = {

    val geneType =
      GeneType(species, chain, Segment.J)

    io.printToFile(outputData.auxFileFor(species, chain)) {
      p =>
        inputData.aux(species, chain)
          .map({ a => a.copy( id = FastaHeader( data fastaHeader Gene(a.id, geneType) ).id ) })
          .foreach({ a => p println a.toTSVRow })
    }
  }

  def writeBLASTDB(geneType: GeneType): Int = {

    val cmd =
      makeblastdb(
        argumentValues =
          in((outputData fastaFileFor geneType).getAbsoluteFile)    ::
          input_type(DBInputType.fasta)                             ::
          dbtype(BlastDBType.nucl)                                  ::
          out((outputData blastDBFileFor geneType).getAbsoluteFile) ::
          *[AnyDenotation],
        optionValues =
          (makeblastdb.defaults update
            title( (outputData fastaFileFor geneType).getName ) ::
            parse_seqids(true)                                  ::
            *[AnyDenotation]
          ).value
      ).toSeq

    val out1 =
      java.nio.file.Files.createDirectories( (outputData geneTypeBase geneType).toPath )
    val out2 =
      java.nio.file.Files.createDirectories( (outputData blastDBFolderFor geneType).toPath )

    import scala.sys.process._

    val proc =
      Process(command = cmd, cwd = outputData blastDBFolderFor geneType)

    proc ! ProcessLogger(_ => ())
  }
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