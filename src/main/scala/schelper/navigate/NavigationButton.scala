package schelper.navigate

import schelper.core.Audible
import schelper.core.SchelperConstants._

import scala.swing.Color

class NavigationButton (text: String, val destination: Audible, action: () => Unit, colour: Color) extends SideEffectButton (text, action, colour) {
  def this (text: String, destination: Audible, colour: Color) = this (text, destination, () => {}, colour)
  def this (navigable: Navigable, colour: Color) = this (navigable.linkName, navigable, colour)
}
