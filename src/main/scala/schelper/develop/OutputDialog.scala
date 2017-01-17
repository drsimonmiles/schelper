package schelper.develop

import scala.swing._

class OutputDialog (parentSize: Dimension) extends Dialog {
  val log = new TextArea ("")
  contents = new ScrollPane (log)
  preferredSize = parentSize
}
