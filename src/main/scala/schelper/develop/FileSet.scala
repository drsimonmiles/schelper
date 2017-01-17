package schelper.develop

import java.io.File
import java.io.File.pathSeparatorChar
import schelper.navigate._
import scala.swing.Publisher
import schelper.core.SchelperPreferences._
import scala.util.Try

abstract class FileSet (val kind: String, val atomic: Boolean) extends Publisher {
  var files: Map[String, String] = getList (kind + "Names", ';').zip (getList (kind + "Paths", pathSeparatorChar)).toMap

  def addFile (name: String, path: String) {
    files += (name -> path)
    saveFileDetails ()
    publish (SourcesChanged)
  }

  def contentsToScreens (directory: File): Seq[Navigable] =
    if (!atomic && directory.isDirectory) directory.listFiles.flatMap (file => Try (fileToScreen (file)).toOption) else Nil

  def createSubdirectory (parent: File, name: String): File = {
    val child = new File (parent, name)
    child.mkdirs ()
    publish (new DirectoryChanged (parent))
    child
  }

  def fileToScreen (file: File): Navigable =
    if (file.isDirectory) new SourceDirectory (file, this) else nonDirectoryToScreen (file)

  def nonDirectoryToScreen (file: File): Navigable

  def removeFile (name: String) {
    files -= name
    saveFileDetails ()
    publish (SourcesChanged)
  }

  def rootScreens =
    files.map (s => new SourceRoot (s._1, s._2, atomic, this)).toList

  private def saveFileDetails () {
    putList (kind + "Names", files.keys.toList, ';')
    putList (kind + "Paths", files.values.toList, pathSeparatorChar)
  }
}
