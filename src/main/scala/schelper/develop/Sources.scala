package schelper.develop

import java.io._
import schelper.core.SchelperConstants.SourceColour
import schelper.core.TextFileEditor
import schelper.core.TextFiles._

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

    files.values.map (new File (_)).flatMap (allIn)
  }

  def allScalaPaths: Iterable[String] =
    allScalaFiles.map (_.getAbsolutePath)

  def createSourceFile (parent: File, name: String): File = {
    val file = createTextFile (parent, name, "")
    publish (new DirectoryChanged (parent))
    file
  }

  def nonDirectoryToScreen (file: File) =
    new TextFileEditor (file, file.getName, SourceColour)

  def openSourceFile (file: File): String = try {
    openTextFile (file)
  } catch {
    case _: Throwable => throw UnopenableSourceException
  }

  def saveSourceFile (file: File, text: String) {
    saveTextFile (file, text)
  }
}
