
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

abstract class TestsFor(
  val species : Species       ,
  val chain   : Chain         ,
  val segments: Set[Segment]
)
extends org.scalatest.FunSuite {

  val geneTypes =
    segments map { GeneType(species, chain, _) }

  val description: String =
    s"${species} ${chain}:"

  //////////////////////////////////////////////////////////////////////////////
  // Input Tests
  test(s"${description} -input- well-formed FASTA files") {

    geneTypes foreach { geneType =>
      (inputData sequences geneType) foreach { lr => assert( lr.isRight ) }
    }
  }

  test(s"${description} -input- FASTA files have no duplicate IDs") {

    geneTypes foreach { geneType =>

      val ids =
        inputData idsFor geneType

      assert { ids.distinct == ids }
    }
  }

  test(s"${description} -input- all J IDs are in the aux file, same order") {

    assert { inputData.idsFor(GeneType(species, chain, Segment.J)) == inputData.auxIDs(species).toList }
  }

  //////////////////////////////////////////////////////////////////////////////
  // FASTA output Tests
  test(s"${description} -FASTA output- generate FASTA files with scoped IDs") {

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

  test(s"${description} -FASTA output- well-formed FASTA") {

    geneTypes foreach { geneType =>
      (outputData sequences geneType) foreach { lr => assert( lr.isRight ) }
    }
  }

  test(s"${description} -FASTA output- FASTA files have no duplicate IDs") {

    geneTypes foreach { geneType =>

      val ids =
        (outputData sequencesIDs geneType).toList

      assert { ids.distinct == ids }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Aux file generation
  test(s"${description} generate aux file") {

    val geneType =
      GeneType(species, chain, Segment.J)

    io.printToFile(outputData.auxFileFor(species, chain)) {
      p =>
        inputData.aux(species)
          .map({ a => a.copy(id = data.fastaHeader(Gene(a.id, geneType))) })
          .foreach({ a => p println a.toTSVRow })
    }
  }

  test(s"${description} check aux J file") {

    val geneType =
      GeneType(species, chain, Segment.J)
      
    assert { outputData.sequencesIDs(geneType).toList == outputData.auxIDs(species, chain).toList }
  }



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

  //////////////////////////////////////////////////////////////////////////////
  // BLAST db generation
  test(s"${description} generate BLAST databases") {

    // create output folders
    geneTypes foreach { geneType =>
      val outF =
        java.nio.file.Files.createDirectories( (outputData geneTypeBase geneType).toPath )
      java.nio.file.Files.createDirectories( (outputData blastDBFolderFor geneType).toPath )
    }

    import scala.sys.process._

    val makeDBProcs =
      makeDBGeneTypeCmds map { cmd => Process(command = cmd._2, cwd = outputData blastDBFolderFor cmd._1) }

      makeDBProcs foreach { proc => println(proc.!!) }
  }

  //////////////////////////////////////////////////////////////////////////////
  // File upload
  test(s"${description} upload FASTA files to S3", ReleaseOnlyTest) {

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

  test(s"${description} upload BLAST databases to S3", ReleaseOnlyTest) {

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


































/**
  concrete subclasses will check that the input data for the corresponding combination of @param species, @param chain, and @param segments is well-formed.
*/
// abstract class _1_WellFormedInputs(
//   val species : Species       ,
//   val chain   : Chain         ,
//   val segments: Set[Segment]
// )
// extends org.scalatest.FunSuite {
//
//   val geneTypes =
//     segments map { GeneType(species, chain, _) }
//
//   val description: String =
//     s"${species} ${chain}:"
//
//   test(s"${description} well-formed FASTA files") {
//
//     geneTypes foreach { geneType =>
//       (inputData sequences geneType) foreach { lr => assert( lr.isRight ) }
//     }
//   }
//
//   test(s"${description} FASTA files have no duplicate IDs") {
//
//     geneTypes foreach { geneType =>
//
//       val ids =
//         inputData idsFor geneType
//
//       assert { ids.distinct == ids }
//     }
//   }
//
//   test(s"${description} all J IDs are in the aux file, same order") {
//
//     assert { inputData.idsFor(GeneType(species, chain, Segment.J)) == inputData.auxIDs(species).toList }
//   }
//   }
//
//
// }
//
// abstract class _2_GenerateFASTA(
//   val species : Species       ,
//   val chain   : Chain         ,
//   val segments: Set[Segment]
// )
// extends org.scalatest.FunSuite {
//
//   val geneTypes =
//     segments map { GeneType(species, chain, _) }
//
//   val description: String =
//     s"${species} ${chain}:"
//
//   test(s"${description} generate FASTA files with scoped IDs") {
//
//     geneTypes foreach { geneType =>
//
//       val writeTo =
//         outputData fastaFileFor geneType
//
//       val deleteIfThere =
//         Files deleteIfExists writeTo.toPath
//
//
//       val writeFiles =
//         inputData.sequences(geneType)
//           .collect({ case Right(a) => a })
//           .map(
//             { fa =>
//
//               val gene =
//                 Gene(fa.getV(header).id, geneType)
//
//               FASTA(
//                 header( FastaHeader(data.fastaHeader(gene)) ) ::
//                 sequence( fa.getV(sequence) )                 ::
//                 *[AnyDenotation]
//               )
//             }
//           )
//           .appendTo(writeTo)
//     }
//   }
//
//   test(s"${description} well-formed FASTA") {
//
//     geneTypes foreach { geneType =>
//       (outputData sequences geneType) foreach { lr => assert( lr.isRight ) }
//     }
//   }
//
//   test(s"${description} FASTA files have no duplicate IDs") {
//
//     geneTypes foreach { geneType =>
//
//       val ids =
//         (outputData sequencesIDs geneType).toList
//
//       assert { ids.distinct == ids }
//     }
//   }
//
//
// }
//
// abstract class _3_AuxFileGeneration(val species: Species, val chain: Chain) extends org.scalatest.FunSuite {
//
//   val geneType =
//     GeneType(species, chain, Segment.J)
//
//   val description: String =
//     s"${species} ${chain}:"
//
//
//
// }
//
// abstract class _4_GenerateBLASTDBs(
//   val species : Species       ,
//   val chain   : Chain         ,
//   val segments: Set[Segment]
// )
// extends org.scalatest.FunSuite {
//
//   val geneTypes =
//     segments map { segment => GeneType(species, chain, segment) }
//
//
// }

```




[test/scala/outputData.scala]: outputData.scala.md
[test/scala/genericTests.scala]: genericTests.scala.md
[test/scala/inputData.scala]: inputData.scala.md
[test/scala/io.scala]: io.scala.md
[test/scala/humanTRB.scala]: humanTRB.scala.md
[main/scala/package.scala]: ../../main/scala/package.scala.md
[main/scala/model.scala]: ../../main/scala/model.scala.md
[main/scala/data.scala]: ../../main/scala/data.scala.md