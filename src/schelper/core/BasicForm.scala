package schelper.core

import scala.swing.GridBagPanel.Fill
import scala.swing._
import schelper.core.SchelperConstants._

class BasicForm extends GridBagPanel with SchelperScreen {
  private val constraint = new Constraints
  private var row = 0
  private var fields = List[Component] ()

  def button (aButton: Button) = {
    constraint.gridy = row
    constraint.gridx = 0
    constraint.gridwidth = 2
    constraint.weightx = 1.0
    layout (aButton) = constraint
    row += 1
    aButton
  }

  def checkbox (label: String, value: Boolean) = {
    val entry = new CheckBox (label)
    entry.selected = value
    constraint.gridy = row
    constraint.gridx = 0
    constraint.gridwidth = 2
    constraint.weightx = 1.0
    layout (entry) = constraint
    row += 1
    entry
  }

  def field (label: String, value: String) = {
    val entry = new TextField (value)
    constraint.gridy = row
    constraint.gridx = 0
    constraint.fill = Fill.None
    constraint.weightx = 0.0
    layout (new Label (label)) = constraint
    constraint.gridx = 1
    constraint.fill = Fill.Horizontal
    constraint.weightx = 1.0
    layout (entry) = constraint
    row += 1
    fields = entry :: fields
    entry
  }

  def clear () {
    fields.foreach {
      case entry: TextField => entry.text = ""
      case entry: CheckBox => entry.selected = false
    }
  }
}
