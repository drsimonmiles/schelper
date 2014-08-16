import schelper.core._
import schelper.core.SchelperConstants._
import schelper.develop.Developer
import schelper.navigate.Navigable
import scala.swing.Label

object Schelper extends SchelperBase {
  lazy val helloWorld = new Label ("Hello Simon!") with SchelperScreen with Navigable {
    def componentPublishers = Nil
    def linkName = "Hello Simon!"
    def linkColour = ActionColour }
  def buttonList = List (Developer, helloWorld, CitedToList)
}
