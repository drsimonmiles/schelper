package schelper.navigate

import schelper.core.Audible
import schelper.core.SchelperConstants._
import scala.swing.BorderPanel.Position._
import scala.swing._

class NavigatingPanel (val screen: Audible) extends BorderPanel with Audible {
  val back = new BackButton
  val home = new HomeButton
  val navigation = new GridPanel (1, 2) {
    contents += back
    contents += home
    background = BackgroundColour
  }

  layout (navigation) = North
  layout (screen) = Center

  val componentPublishers = back :: home :: screen.publishers
}
