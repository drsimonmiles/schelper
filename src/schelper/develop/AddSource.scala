package schelper.develop

import schelper.core.BasicForm
import schelper.core.SchelperConstants._
import schelper.navigate._

class AddSource (set: FileSet) extends BasicForm with Navigable {
  val linkName = "Add " + set.kind
  val linkColour = ActionColour
  val nameField = field ("Name", "")
  val pathField = field ("Path", "")
  val addButton = button (new BackButton ("Add", () => { set.addFile (nameField.text, pathField.text); clear () }, ActionColour))
  val cancelButton = button (new BackButton ("Cancel", () => { clear () }, NavigateColour))
  val componentPublishers = List (addButton, cancelButton)
}
