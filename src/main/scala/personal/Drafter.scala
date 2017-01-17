import schelper.core.SchelperConstants.ActionColour
import schelper.navigate.{Navigable, SideEffectButton}
import scala.swing.BorderPanel.Position.{Center, South}
import scala.swing.{BorderPanel, ScrollPane, TextArea}

object Drafter extends BorderPanel with Navigable {
  val linkName = "Draft text"
  val linkColour = ActionColour
  val edit = new TextArea
  val clearButton = new SideEffectButton ("Clear", () => { edit.text = "" }, ActionColour)
  val componentPublishers = List (clearButton)

  edit.charWrap = true
  layout (new ScrollPane (edit)) = Center
  layout (clearButton) = South
}
