package ohnosequences.db.tcr.test

import ohnosequences.db.tcr._
import ohnosequences.cosas._, klists._, types._
import ohnosequences.fastarious.fasta._
import java.nio.file.Files
import ohnosequences.test._
import ohnosequences.awstools.s3._
import util.{ Success, Failure }
import ohnosequences.blast._, api._, outputFields._

/**
  Contains all the code that should be run for generating the data corresponding to the given @param species, @param chain, and @param segments. In short:

  - Check input files (no duplicate sequences/IDs, FASTA files OK, ...)
  - Generate FASTA files with version-scoped IDs, check everything again
  - Generate IgBLAST `.aux` files, check them
  - Generate BLAST databases for each gene type
  - (Release-Only) upload everything to S3
*/
abstract class TestsFor(
  val species : Species       ,
  val chain   : Chain         ,
  val segments: Set[Segment]
)
extends org.scalatest.FunSuite {

  val geneTypes =
    segments map { GeneType(species, chain, _) }

  val testInputMsg: String =
    "checking inputs |"

  val generateFASTAMsg: String =
    "generating FASTA files |"

  val generateAuxFileMsg: String =
    "generating .aux file |"

  val generateBLASTDBsMsg: String =
    "generating BLAST DBs |"

  val uploadToS3Msg: String =
    "uploading to S3 |"

  //////////////////////////////////////////////////////////////////////////////
  // Input Tests
  test(s"${testInputMsg} well-formed FASTA files") {

    geneTypes foreach { geneType =>
      (inputData sequences geneType) foreach { lr => assert( lr.isRight ) }
    }
  }

  test(s"${testInputMsg} FASTA files have no duplicate IDs") {

    geneTypes foreach { geneType =>

      val ids =
        inputData idsFor geneType

      assert { ids.distinct == ids }
    }
  }

  test(s"${testInputMsg} all J IDs are in the aux file, same order") {

    assert { inputData.idsFor(GeneType(species, chain, Segment.J)) == inputData.auxIDs(species, chain).toList }
  }

  //////////////////////////////////////////////////////////////////////////////
  // FASTA output Tests
  test(s"${generateFASTAMsg} generate FASTA files with scoped IDs") {

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

  test(s"${generateFASTAMsg} well-formed generated FASTA") {

    geneTypes foreach { geneType =>
      (outputData sequences geneType) foreach { lr => assert( lr.isRight ) }
    }
  }

  test(s"${generateFASTAMsg} generated FASTA have no duplicate IDs") {

    geneTypes foreach { geneType =>

      val ids =
        (outputData sequencesIDs geneType).toList

      assert { ids.distinct == ids }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Aux file generation
  test(s"${generateAuxFileMsg} generate aux J file") {

    val geneType =
      GeneType(species, chain, Segment.J)

    io.printToFile(outputData.auxFileFor(species, chain)) {
      p =>
        inputData.aux(species, chain)
          .map({ a => a.copy(id = data.fastaHeader(Gene(a.id, geneType))) })
          .foreach({ a => p println a.toTSVRow })
    }
  }

  test(s"${generateAuxFileMsg} check aux J file") {

    val geneType =
      GeneType(species, chain, Segment.J)

    assert { outputData.sequencesIDs(geneType).toList == outputData.auxIDs(species, chain).toList }
  }

  //////////////////////////////////////////////////////////////////////////////
  // BLAST db generation

  val makeDBGeneTypeCmds: Set[(GeneType, Seq[String])] =
    geneTypes map {
      geneType =>
        geneType -> {

          makeblastdb(
            argumentValues =
              in((outputData fastaFileFor geneType).getAbsoluteFile)    ::
              input_type(DBInputType.fasta)                             ::
              dbtype(BlastDBType.nucl)                                  ::
              out((outputData blastDBFileFor geneType).getAbsoluteFile) ::
              *[AnyDenotation],
            optionValues =
              (makeblastdb.defaults update title( (outputData fastaFileFor geneType).getName )).value
          ).toSeq
        }
    }

  test(s"${generateBLASTDBsMsg} generate BLAST databases") {

    // create output folders
    geneTypes foreach { geneType =>
      val outF =
        java.nio.file.Files.createDirectories( (outputData geneTypeBase geneType).toPath )
      java.nio.file.Files.createDirectories( (outputData blastDBFolderFor geneType).toPath )
    }

    import scala.sys.process._

    val makeDBProcs =
      makeDBGeneTypeCmds map { cmd => Process(command = cmd._2, cwd = outputData blastDBFolderFor cmd._1) }

    val exitCodes =
      makeDBProcs.toList map { _ ! ProcessLogger(_ => ()) }

    assert { exitCodes == List.fill(exitCodes.size)(0) }
  }

  //////////////////////////////////////////////////////////////////////////////
  // File upload
  test(s"${uploadToS3Msg} FASTA files", ReleaseOnlyTest) {

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

  test(s"${uploadToS3Msg} aux file", ReleaseOnlyTest) {

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

  test(s"${uploadToS3Msg} BLAST databases", ReleaseOnlyTest) {

    val s3 =
      S3Client()

    val transferManager =
      s3.createTransferManager

    geneTypes foreach { geneType =>

      transferManager.upload(
        outputData blastDBFolderFor geneType,
        data blastDB geneType
      )
      match {
        case Success(_) => succeed
        case Failure(a) => fail(a.toString)
      }
    }

    transferManager.shutdownNow
  }
}
