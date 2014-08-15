package schelper.navigate

import scala.swing.Color
import schelper.core.SchelperConstants._

class HomeButton (title: String, action: () => Unit, colour: Color) extends SideEffectButton (title, action, colour) {
  def this (title: String, colour: Color) = this (title, () => {}, colour)
  def this () = this ("Home", NavigateColour)
}
