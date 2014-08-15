package schelper.develop

import java.io.File
import schelper.core.ErrorScreen

object Libraries extends FileSet ("Library", true) {
  def nonDirectoryToScreen (file: File) = ErrorScreen
}
