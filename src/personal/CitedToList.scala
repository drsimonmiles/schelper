import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor.stringFlavor
import java.io.{BufferedWriter, FileWriter, PrintWriter}
import schelper.core.SchelperConstants.{ActionColour, NavigateColour}
import schelper.core.SchelperPreferences.getString
import schelper.core.SchelperScreen
import schelper.navigate.{BackButton, Navigable}
import scala.swing.{TextArea, ScrollPane, FlowPanel, BorderPanel}
import scala.swing.BorderPanel.Position.{Center, South}

object CitedToList extends BorderPanel with SchelperScreen with Navigable {
  val linkName = "Add citations to list"
  val linkColour = ActionColour
  val ListPathPreference = "ListPath"
  val pasteArea = new TextArea ("")
  val addButton = new BackButton ("Add cites", () => { processCites (); clear () }, ActionColour)
  val cancelButton = new BackButton ("Cancel", () => clear (), NavigateColour)
  val buttonPane = new FlowPanel {
    contents += addButton
    contents += cancelButton
  }
  val componentPublishers = List (addButton, cancelButton)

  layout (new ScrollPane (pasteArea)) = Center
  layout (buttonPane) = South

  def clear () {
    pasteArea.text = ""
  }

  override def opened () {
    pasteCurrent ()
  }

  def pasteCurrent () {
    val contents = Toolkit.getDefaultToolkit.getSystemClipboard.getContents (this)
    if (contents != null && contents.isDataFlavorSupported (stringFlavor))
      try {
        pasteArea.text = contents.getTransferData (stringFlavor).toString
      } catch {
        case _: Throwable =>
      }
  }

  def processCites () {
    def collectCites (rows: List[String], matches: List[(String, String)] = Nil): List[(String, String)] = rows match {
      case Nil => matches
      case soleLine :: Nil => matches
      case title :: author :: rest => collectCites (ignoreAbstract (rest), toCite (title, author) :: matches)
    }
    def toCite (titleRow: String, authorRow: String): (String, String) =
      (if (titleRow.startsWith ("[")) titleRow.dropWhile (_ != ']').substring (1).trim else titleRow,
        authorRow.takeWhile (_ != '-').trim)
    def ignoreAbstract (rows: List[String]): List[String] =
      rows.dropWhile (!_.isEmpty).dropWhile (_.isEmpty)

    val path = getString (ListPathPreference)
    if (path.isDefined) {
      val out = new PrintWriter (new BufferedWriter (new FileWriter (path.get, true)))
      val cites = collectCites (pasteArea.text.split ('\n').map (_.trim).dropWhile (_.isEmpty).toList)
      try {
        cites.foreach { cite =>
          out.println (cite._1)
          out.println (cite._2)
          out.println ()
        }
      } finally {
        out.close ()
      }
    }
  }
}
