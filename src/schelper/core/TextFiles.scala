package schelper.core

import java.io._
import scala.io.Source._

object TextFiles {
  def createTextFile (parent: File, name: String, text: String): File = {
    val child = new File (parent, name)
    saveTextFile (child, text)
    child
  }

  @throws[IOException]("if the file doesn't exist or is unopenable")
  def openTextFile (file: File): String = {
    if (file.exists) {
      val source = fromFile (file)
      val lines = source.mkString
      source.close ()
      lines
    } else ""
  }

  def saveTextFile (file: File, text: String) {
    val out = new PrintWriter (file)
    try {
      out.print (text)
    } finally {
      out.close ()
    }
  }
}
