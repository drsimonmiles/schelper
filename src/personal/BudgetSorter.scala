import java.awt.datatransfer.DataFlavor
import java.io.{FileInputStream, FileOutputStream, File}
import javax.swing.TransferHandler
import javax.swing.TransferHandler.TransferSupport
import org.apache.poi.hssf.extractor.OldExcelExtractor
import personal.BudgetDirectories
import schelper.core.SchelperConstants._
import schelper.core.SchelperPreferences._
import schelper.core.SchelperScreen
import schelper.navigate.{BackButton, Navigable, NavigationButton}
import scala.collection.JavaConverters._
import scala.swing.BorderPanel.Position._
import scala.swing._

object BudgetSorter extends BorderPanel with SchelperScreen with Navigable {
  val linkName = "Save budget spreadsheet"
  val linkColour = ActionColour
  val dropPanel = new Label ("Drop budgets here")
  val dirsButton = new NavigationButton (BudgetDirectories, NavigateColour)
  val cancelButton = new BackButton ("Cancel", NavigateColour)
  val buttonPane = new FlowPanel {
    contents += dirsButton
    contents += cancelButton
  }
  val componentPublishers: Iterable[Publisher] = Seq(dirsButton, cancelButton)

  def determineType (file: File) = {
    val workbook = new OldExcelExtractor (file)
    val text = workbook.getText
    val isTotals = text.trim.startsWith ("KING") || text.dropWhile (_ != '\n').trim.startsWith ("KING")
    if (isTotals) " Latest totals.xls" else " Latest transactions.xls"
  }

  peer.setTransferHandler (new TransferHandler {
    override def canImport (support: TransferSupport) = true
    override def importData (support: TransferSupport) = {
      val data = support.getTransferable.getTransferData (DataFlavor.javaFileListFlavor)
      val src = data.asInstanceOf[java.util.List[File]].asScala.head
      val code = src.getName.takeWhile (_.isLetterOrDigit)
      val dir = getString (code + "Directory").getOrElse (".")
      val dest = new File (dir, code + " untyped.xls")
      dest.getParentFile.mkdirs ()
      new FileOutputStream (dest).getChannel transferFrom (new FileInputStream (src).getChannel, 0, Long.MaxValue)
      try {
        dest.renameTo (new File (dir, code + determineType (dest)))
      } catch { case t: Throwable => t.printStackTrace () }
      true
    }
  })

  layout (dropPanel) = Center
  layout (buttonPane) = South
}
