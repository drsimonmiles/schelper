package schelper.develop

import java.io.{PrintWriter, File}
import scala.io.Source.fromFile

object Sources extends FileSet ("Source", false) {
  def allScalaFiles: Iterable[File] = {
    def allIn (file: File): Seq[File] =
      if (file.isDirectory)
        file.listFiles.flatMap (allIn)
      else
        if (file.getName.endsWith (".scala"))
          List (file)
        else
          Nil

    files.values.flatMap (allIn)
  }

  def allScalaPaths: Iterable[String] =
    allScalaFiles.map (_.getAbsolutePath)

  def createSourceFile (parent: File, name: String): File = {
    val child = new File (parent, name)
    saveSourceFile (child, "")
    publish (new DirectoryChanged (parent))
    child
  }

  def nonDirectoryToScreen (file: File) =
    new SourceEditor (file)

  def openSourceFile (file: File) = {
    val source = fromFile (file)
    val lines = source.mkString
    source.close ()
    lines
  }

  def saveSourceFile (file: File, text: String) {
    val out = new PrintWriter (file)
    try {
      out.print (text)
    } finally {
      out.close ()
    }
  }
}
