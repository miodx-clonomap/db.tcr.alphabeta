package ohnosequences.db.tcr.test

import ohnosequences.db.tcr._
import ohnosequences.cosas._, klists._, types._
import ohnosequences.fastarious.fasta._
import java.nio.file.Files
import ohnosequences.test._
import ohnosequences.awstools.s3._
import util.{ Success, Failure }

/**
  concrete subclasses will check that the input data for the corresponding combination of @param species, @param chain, and @param segments is well-formed.
*/
abstract class WellFormedInputs(
  val species : Species       ,
  val chain   : Chain         ,
  val segments: Set[Segment]
)
extends org.scalatest.FunSuite {

  val geneTypes =
    segments map { GeneType(species, chain, _) }

  val description: String =
    s"${species} ${chain}:"

  test(s"${description} well-formed FASTA files") {

    geneTypes foreach { geneType =>
      (inputData sequences geneType) foreach { lr => assert( lr.isRight ) }
    }
  }

  test(s"${description} FASTA files have no duplicate IDs") {

    geneTypes foreach { geneType =>

      val ids =
        inputData idsFor geneType

      assert { ids.distinct == ids }
    }
  }

  test(s"${description} all J IDs are in the aux file, same order") {

    assert { inputData.idsFor(GeneType(species, chain, Segment.J)) == inputData.auxIDs(species).toList }
  }
}

abstract class GenerateFASTA(
  val species : Species       ,
  val chain   : Chain         ,
  val segments: Set[Segment]
)
extends org.scalatest.FunSuite {

  val geneTypes =
    segments map { GeneType(species, chain, _) }

  val description: String =
    s"${species} ${chain}:"

  test(s"${description} generate FASTA files with scoped IDs") {

    geneTypes foreach { geneType =>

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

  test(s"${description} well-formed FASTA") {

    geneTypes foreach { geneType =>
      (outputData sequences geneType) foreach { lr => assert( lr.isRight ) }
    }
  }

  test(s"${description} FASTA files have no duplicate IDs") {

    geneTypes foreach { geneType =>

      val ids =
        (outputData sequencesIDs geneType).toList

      assert { ids.distinct == ids }
    }
  }

  test(s"${description} upload files to S3", ReleaseOnlyTest) {

    val s3 =
      S3Client()

    val transferManager =
      s3.createTransferManager

    geneTypes foreach { geneType =>

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

abstract class AuxFileGeneration(val species: Species, val chain: Chain) extends org.scalatest.FunSuite {

  val geneType =
    GeneType(species, chain, Segment.J)

  val description: String =
    s"${species} ${chain}:"

  test(s"${description} generate aux file") {

    io.printToFile(outputData.auxFileFor(species, chain)) {
      p =>
        inputData.aux(species)
          .map({ a => a.copy(id = data.fastaHeader(Gene(a.id, geneType))) })
          .foreach({ a => p println a.toTSVRow })
    }
  }

  test(s"${description} check aux file") {

    assert { outputData.sequencesIDs(geneType).toList == outputData.auxIDs(species, chain).toList }
  }

  test(s"${description} upload aux file to S3", ReleaseOnlyTest) {

    val s3 =
      S3Client()

    val transferManager =
      s3.createTransferManager

    val upload =
      transferManager.upload(
        outputData.auxFileFor(species, chain),
        data.igblastAux(species, chain)
      )
      match {
        case Success(_) => succeed
        case Failure(a) => fail(a.toString)
      }

    transferManager.shutdownNow
  }
}
