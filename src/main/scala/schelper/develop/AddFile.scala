package schelper.develop

import java.io.File
import schelper.core.BasicForm
import schelper.core.SchelperConstants._
import schelper.develop.Sources._
import schelper.navigate._

object AddFile {
  def apply (parent: File, addFile: Boolean) = new AddFile (parent, addFile)
  def addEither (parent: File) = List (AddFile (parent, addFile = true), AddFile (parent, addFile = false))
}

class AddFile (parent: File, addFile: Boolean) extends BasicForm with Navigable {
  val linkName = if (addFile) "Add file" else "Add directory"
  val linkColour = ActionColour
  val nameField = field ("Name", "")
  val addButton = button (new BackButton ("Add", () => {
    if (addFile)
      createSourceFile (parent, nameField.text)
    else
      createSubdirectory (parent, nameField.text)
    clear () }, ActionColour))
  val cancelButton = button (new BackButton ("Cancel", () => { clear () }, NavigateColour))
  val componentPublishers = List (addButton, cancelButton)
}
