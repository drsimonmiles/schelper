package schelper.develop

import java.io.File
import schelper.core.SchelperConstants._
import schelper.core.SubMenu

object SourceDirectory {
  private def getScreens (directory: File, set: FileSet) = set.contentsToScreens (directory) ++ AddFile.addEither (directory)
}

class SourceDirectory (directory: File, set: FileSet)
  extends SubMenu (directory.getName, SourceColour, SourceDirectory.getScreens (directory, set)) {

  listenTo (set)
  reactions += {
    case DirectoryChanged (changed)
      if directory == changed => items = SourceDirectory.getScreens (directory, set)
  }
}