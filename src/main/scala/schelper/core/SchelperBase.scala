package schelper.core

import javax.swing.UIManager
import schelper.navigate._
import History._
import SchelperConstants._
import scala.collection.mutable
import scala.swing._
import scala.swing.event._
import schelper.core.SchelperPreferences._

abstract class SchelperBase extends SimpleSwingApplication {
  UIManager.setLookAndFeel (UIManager.getCrossPlatformLookAndFeelClassName)

  def buttonList: Seq[Navigable]

  def goBack () { reverse ().foreach (show) }
  def goHome () { navigate (homepage) }

  def navigate (screen: Audible) {
    def createPage = {
      val created = new NavigatingPanel (screen)
      register (created)
      pageCache += (screen -> created)
      created
    }
    try {
      show (recorded (pageCache.getOrElse (screen, createPage)))
      screen.opened ()
    } catch {
      case _: Throwable => navigate (ErrorScreen)
    }
  }

  def top = new MainFrame {
    frame = Some (this)
    title = "Schelper"
    navigate (homepage)
    loadPosition (MainWindowPosition, this)
    reactions += {
      case WindowClosing (_) => save ()
    }
  }

  private var frame: Option[MainFrame] = None
  private lazy val homepage = new SchelperMenu (buttonList)
  private val pageCache = mutable.Map[Component, BorderPanel] ()

  private def register (audible: Audible) {
    audible.publishers.foreach (listenTo (_))
  }

  private def save () {
    if (frame.isDefined) savePosition (MainWindowPosition, frame.get)
  }

  private def show (whole: BorderPanel) {
    if (frame.isDefined)
      if (frame.get.contents.isEmpty)
        frame.get.contents = whole
      else {
        val preSize = frame.get.contents (0).size
        whole.preferredSize = preSize
        frame.get.contents = whole
        frame.get.repaint ()
      }
  }

  reactions += {
    case ButtonClicked (button) if button.isInstanceOf[NavigationButton] => navigate (button.asInstanceOf[NavigationButton].destination)
    case ButtonClicked (button) if button.isInstanceOf[BackButton] => goBack ()
    case ButtonClicked (button) if button.isInstanceOf[HomeButton] => goHome ()
    case NavigateBack => goBack ()
    case NavigateHome => goHome ()
    case NavigateTo (destination) => navigate (destination)
    case PublisherAdded (publisher) => listenTo (publisher)
    case SavePosition => save ()
    case ExitApplication => System.exit (0)
  }
}
