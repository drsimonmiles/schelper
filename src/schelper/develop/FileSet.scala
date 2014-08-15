package schelper.develop

import java.io.File
import schelper.core.SchelperConstants._
import schelper.navigate.{Navigable, NavigationButton}
import settings4scala.Settings._
import scala.swing.Publisher

abstract class FileSet (val kind: String, val atomic: Boolean) extends Publisher {
  var files: Map[String, File] = getList (ApplicationName, kind + "Names").zip (getFiles (ApplicationName, kind + "Paths")).toMap

  def addFile (name: String, path: String) {
    files += (name -> new File (path))
    saveFileDetails ()
    publish (SourcesChanged)
  }

  def contentsToScreens (directory: File): Seq[Navigable] =
    if (!atomic && directory.isDirectory) directory.listFiles.map (fileToScreen) else Nil

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
    putList (ApplicationName, kind + "Names", files.keys.toList)
    putFiles (ApplicationName, kind + "Paths", files.values.toList)
  }
}
