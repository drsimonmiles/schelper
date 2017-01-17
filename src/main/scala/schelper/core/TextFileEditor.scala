package schelper.core

import java.io.File
import schelper.core.SchelperConstants._
import schelper.navigate._
import scala.swing._
import scala.swing.BorderPanel.Position._
import TextFiles._

class TextFileEditor (file: => File, val linkName: String, val linkColour: Color) extends BorderPanel with Navigable {
  val edit = new TextArea
  val saveButton = new BackButton ("Save", () => { save (file, edit.text) }, ActionColour)
  val cancelButton = new BackButton ("Cancel", NavigateColour)
  val buttonPane = new FlowPanel {
    contents += saveButton
    contents += cancelButton
  }
  val componentPublishers = List (saveButton, cancelButton)

  override def opened () {
    edit.text = load (file)
  }

  def load (file: File): String = openTextFile (file)
  def save (file: File, text: String) = saveTextFile (file, text)

  layout (new ScrollPane (edit)) = Center
  layout (buttonPane) = South
}
