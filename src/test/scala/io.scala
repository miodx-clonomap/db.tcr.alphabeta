package ohnosequences.db.tcr.test

case object io {

  import java.io._

  def printToFile(f: File)(op: PrintWriter => Unit) {
    val p =
      new java.io.PrintWriter(f)

    try { op(p) } finally { p.close }
  }
}
