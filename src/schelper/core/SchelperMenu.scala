package schelper.core

import schelper.navigate.{NavigationButton, Navigable}
import schelper.core.SchelperConstants._
import scala.swing.GridBagPanel._
import scala.swing._

class SchelperMenu (private var _items: Seq[Navigable]) extends ScrollPane with Audible {
  var menu = toButtons
  def componentPublishers = menu
  contents = constructMenu

  def items = _items

  def items_= (newItems: Seq[Navigable]) {
    _items = newItems
    menu = toButtons
    contents = constructMenu
    menu.foreach { b => publish (PublisherAdded (b)) }
  }

  private def constructMenu = new GridBagPanel with SchelperScreen {
    val constraint = new Constraints
    constraint.fill = Fill.Horizontal
    constraint.weightx = 1.0
    constraint.gridx = 0
    for (row <- menu.indices; button = menu (row)) {
      constraint.gridy = row
      layout (button) = constraint
    }
  }

  private def toButtons = _items.map (l => new NavigationButton (l.linkName, l, l.linkColour))
}


