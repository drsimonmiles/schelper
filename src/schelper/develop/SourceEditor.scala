package schelper.develop

import java.io.File
import schelper.core.SchelperConstants._
import schelper.navigate.{BackButton, Navigable}
import scala.swing._
import scala.swing.BorderPanel.Position._
import Sources._

class SourceEditor (file: File) extends BorderPanel with Navigable {
  val linkName = file.getName
  val linkColour = SourceColour
  val edit = new TextArea (openSourceFile (file))
  val saveButton = new BackButton ("Save", () => { saveSourceFile (file, edit.text) }, ActionColour)
  val cancelButton = new BackButton ("Cancel", NavigateColour)
  val buttonPane = new FlowPanel {
    contents += saveButton
    contents += cancelButton
  }
  val componentPublishers = List (saveButton, cancelButton)

  layout (new ScrollPane (edit)) = Center
  layout (buttonPane) = South
}
