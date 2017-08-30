package ohnosequences.db.tcr.test

import ohnosequences.db.tcr._
import ohnosequences.cosas._, klists._, types._
import ohnosequences.fastarious.fasta._
import java.nio.file.Files
import ohnosequences.test._
import ohnosequences.awstools.s3._
import util.{ Success, Failure }

class GenerateIDs extends org.scalatest.FunSuite {

  val segments: Set[Segment] =
    Set(Segment.V, Segment.D, Segment.J)

  test("TCR beta human: generate FASTA files with scoped IDs") {

    segments foreach { segment =>

      val geneType =
        GeneType(Species.human, Chain.TRB, segment)

      val deleteIfThere =
        Files.deleteIfExists(outputData.fastaFileFor(Species.human, Chain.TRB, segment).toPath)

      val writeTo =
        outputData.fastaFileFor(Species.human, Chain.TRB, segment)

      val writeFiles =
        inputData.sequences(Species.human, Chain.TRB, segment)
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
  }

  test("TCR beta human: upload files to S3", ReleaseOnlyTest) {

    val s3 =
      S3Client()

    val transferManager =
      s3.createTransferManager

    segments foreach { segment =>

      transferManager.upload(
        outputData.fastaFileFor(Species.human, Chain.TRB, segment),
        data.fasta(Species.human, Chain.TRB, segment)
      )
      match {
        case Success(_) => succeed
        case Failure(a) => fail(a.toString)
      }
    }

    transferManager.shutdownNow
  }
}
