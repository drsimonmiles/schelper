package schelper.core

import schelper.core.SchelperConstants._
import schelper.navigate.Navigable

import scala.swing.Label

object ErrorScreen extends Label ("Internal error. Please click Home or Back.") with SchelperScreen with Navigable {
  def componentPublishers = Nil
  def linkName = "Error"
  def linkColour = ActionColour
}
