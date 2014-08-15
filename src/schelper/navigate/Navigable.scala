package schelper.navigate

import schelper.core.Audible
import scala.swing.Color

trait Navigable extends Audible {
  def linkName: String
  def linkColour: Color
}
