package schelper.develop

import java.io.File
import schelper.core._
import schelper.core.SchelperConstants._
import schelper.navigate._

class SourceRoot (val linkName: String, source: String, atomic: Boolean, set: FileSet) extends BasicForm with Navigable {
  val linkColour = SectionColour
  val pathField = field ("Path", source)
  val saveButton = button (new BackButton ("Save path", () => { set.addFile (linkName, pathField.text) }, ActionColour))
  val componentPublishers =
    if (atomic)
      List (saveButton)
    else
      List (saveButton, button (new NavigationButton ("Browse contents", new SourceDirectory (new File (source), set), SourceColour)))
}
