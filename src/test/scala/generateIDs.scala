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
  }

  test("TCR beta human: upload files to S3", ReleaseOnlyTest) {

    val s3 =
      S3Client()

    val transferManager =
      s3.createTransferManager

    segments foreach { segment =>

      val geneType =
        GeneType(Species.human, Chain.TRB, segment)

      transferManager.upload(
        outputData fastaFileFor geneType,
        data fasta geneType
      )
      match {
        case Success(_) => succeed
        case Failure(a) => fail(a.toString)
      }
    }

    transferManager.shutdownNow
  }
}
