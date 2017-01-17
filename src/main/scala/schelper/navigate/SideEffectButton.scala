package schelper.navigate

import scala.swing._
import scala.swing.event.ButtonClicked

class SideEffectButton (title: String, action: () => Unit, colour: Color) extends Button (title) {
  reactions += { case ButtonClicked (_) => action () }

  background = colour
//  opaque = true
//  borderPainted = false
}