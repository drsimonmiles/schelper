package schelper.core

import java.io.File
import settings4scala.Preferences

object SchelperPreferences extends Preferences {
  def getFile (kind: String, defaultPath: String) = new File (getString (kind).getOrElse (defaultPath))
}