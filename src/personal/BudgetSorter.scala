import java.awt.datatransfer.DataFlavor
import java.io._
import javax.swing.TransferHandler
import javax.swing.TransferHandler.TransferSupport
import personal.BudgetDirectories
import schelper.core.SchelperConstants._
import schelper.core.SchelperPreferences._
import schelper.core.SchelperScreen
import schelper.navigate.{NavigationButton, BackButton, Navigable}
import scala.collection.JavaConverters._
import scala.swing.BorderPanel.Position._
import scala.swing._

object BudgetSorter extends BorderPanel with SchelperScreen with Navigable {
  val linkName = "Save budget spreadsheet"
  val linkColour = ActionColour
  val dropPanel = new Label ("Drop budgets here")
  val totalsButton = new RadioButton ("Totals")
  val transactionsButton = new RadioButton ("Transactions")
  val group = new ButtonGroup (totalsButton, transactionsButton)
  val dirsButton = new NavigationButton (BudgetDirectories, NavigateColour)
  val cancelButton = new BackButton ("Cancel", NavigateColour)
  val typePane = new FlowPanel {
    contents += totalsButton
    contents += transactionsButton
  }
  val buttonPane = new FlowPanel {
    contents += dirsButton
    contents += cancelButton
  }
  val componentPublishers: Iterable[Publisher] = Seq(dirsButton, cancelButton)
  def typed = if (group.selected.contains (totalsButton)) " Latest totals.xls" else " Latest transactions.xls"

  peer.setTransferHandler (new TransferHandler {
    override def canImport (support: TransferSupport) = true
    override def importData (support: TransferSupport) = {
      val data = support.getTransferable.getTransferData (DataFlavor.javaFileListFlavor)
      val src = data.asInstanceOf[java.util.List[File]].asScala.head
      val code = src.getName.takeWhile (_.isLetter)
      val dest = new File (getString (code + "Directory").getOrElse ("."), code + typed)
      dest.getParentFile.mkdirs ()
      new FileOutputStream (dest).getChannel transferFrom (new FileInputStream (src).getChannel, 0, Long.MaxValue)
      true
    }
  })

  group.select (totalsButton)
  layout (typePane) = North
  layout (dropPanel) = Center
  layout (buttonPane) = South
}

