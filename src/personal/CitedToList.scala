import java.io.{BufferedWriter, FileWriter, PrintWriter}
import schelper.core.SchelperConstants._
import schelper.core.SchelperPreferences._
import schelper.core.SchelperScreen
import schelper.navigate._
import scala.swing._
import scala.swing.BorderPanel.Position._

object CitedToList extends BorderPanel with SchelperScreen with Navigable {
  val linkName = "Add citations to list"
  val linkColour = ActionColour
  val ListPathSetting = "ListPath"
  val listPath = new TextField (getString (ListPathSetting).getOrElse (""))
  val saveButton = new SideEffectButton ("Save", () => save (), ActionColour)
  val pathPanel = new BorderPanel {
    layout (new Label ("List path")) = West
    layout (listPath) = Center

  }
  val pasteArea = new TextArea ("")
  val addButton = new BackButton ("Add cites", () => { save (); processCites (); clear () }, ActionColour)
  val cancelButton = new BackButton ("Cancel", () => clear (), NavigateColour)
  val buttonPane = new FlowPanel {
    contents += addButton
    contents += cancelButton
  }
  val componentPublishers = List (saveButton, addButton, cancelButton)

  layout (pathPanel) = North
  layout (new ScrollPane (pasteArea)) = Center
  layout (buttonPane) = South

  def clear () {
    pasteArea.text = ""
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

    val cites = collectCites (pasteArea.text.split ('\n').map (_.trim).dropWhile (_.isEmpty).toList)
    val out = new PrintWriter (new BufferedWriter (new FileWriter (listPath.text, true)))
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

  def save () {
    putString (ListPathSetting, listPath.text)
  }
}

/*
[PDF] Database Queries that Explain their Work
J Cheney, A Ahmed, UA Acar - arXiv preprint arXiv:1408.1675, 2014
Abstract: Provenance for database queries or scientific workflows is often motivated as
providing explanation, increasing understanding of the underlying data sources and
processes used to compute the query, and reproducibility, the capability to recompute the ...

The D-NET Software Toolkit: A Framework for the Realization, Maintenance, and Operation of Aggregative Infrastructures
P Manghi, M Artini, C Atzori, A Bardi, A Mannocci… - Program: electronic library …, 2014
Purpose-This paper presents the architectural principles and the services of the D-NET
Software Toolkit. D-NET is a framework where designers and developers find the tools for
constructing and operating aggregative infrastructures (systems for aggregating data ...
 */