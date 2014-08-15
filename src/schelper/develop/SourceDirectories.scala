package schelper.develop

import schelper.core.SchelperConstants._
import schelper.core.SubMenu
import scala.swing.Reactor

class SourceDirectories (set: FileSet) extends SubMenu (set.kind + " Files", SourceColour, new AddSource (set) :: set.rootScreens) with Reactor {
  listenTo (set)
  reactions += {
    case SourcesChanged => items = new AddSource (set) :: set.rootScreens
  }
}
