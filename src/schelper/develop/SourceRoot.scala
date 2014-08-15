package schelper.develop

import java.io.File
import schelper.core._
import schelper.core.SchelperConstants._
import schelper.navigate._

class SourceRoot (val linkName: String, source: File, atomic: Boolean, set: FileSet) extends BasicForm with Navigable {
  val linkColour = SectionColour
  val pathField = field ("Path", source.getAbsolutePath)
  val saveButton = button (new BackButton ("Save path", () => { set.addFile (linkName, pathField.text) }, ActionColour))
  val componentPublishers =
    if (atomic)
      List (saveButton)
    else
      List (saveButton, button (new NavigationButton ("Browse contents", new SourceDirectory (source, set), SourceColour)))
}
