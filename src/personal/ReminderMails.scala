import java.io.{FileWriter, BufferedWriter, PrintWriter}
import java.time.LocalDate
import java.time.LocalDate.now
import schelper.core.SchelperConstants._
import schelper.core.SchelperPreferences.getFile
import schelper.core.SchelperScreen
import schelper.navigate.Navigable
import scala.io.Source
import scala.swing._
import scala.swing.BorderPanel.Position._
import scala.swing.Orientation.Vertical
import scala.swing.event.ButtonClicked
import scala.util.Try

object ReminderMails extends BorderPanel with SchelperScreen with Navigable {
  val linkName: String = "Reminder mails"
  val linkColour: Color = ActionColour
  val componentPublishers: Iterable[Publisher] = Seq ()
  var reminders: List[Reminder] = loadReminders
  var panels = reminders.map (new ReminderPanel (_))
  val table = new FlowPanel {
    panels.foreach { contents += _ }
  }
  val addButton = new Button ("Add")
  val generateButton = new Button ("Generate")
  val saveButton = new Button ("Save")
  val buttons = new FlowPanel {
    contents += addButton
    contents += generateButton
    contents += saveButton
  }
  val emailsArea = new TextArea

  class Reminder (var email: String, var task: String, var date: LocalDate)

  class ReminderPanel (val reminder: Reminder) extends FlowPanel {
    import reminder._
    val emailField = new TextField (email, 20)
    val taskField = new TextField (task, 30)
    val dateField = new TextField (date.toString)
    val plusDay = new Button ("+day")
    val plusWeek = new Button ("+week")
    val delete = new Button ("Delete")

    def getDate =
      Try (LocalDate.parse (dateField.text)).getOrElse (date)

    def setDate (newDate: LocalDate) =
      dateField.text = newDate.toString

    def saveData () = {
      reminder.email = emailField.text.trim
      reminder.task = taskField.text.trim
      reminder.date = getDate
    }

    contents ++= Seq (emailField, taskField, dateField, plusDay, plusWeek, delete)
    listenTo (plusDay, plusWeek, delete)
    reactions += {
      case ButtonClicked (button) if button == plusDay => setDate (getDate.plusDays (1))
      case ButtonClicked (button) if button == plusWeek => setDate (getDate.plusDays (7))
      case ButtonClicked (button) if button == delete => removeReminder (reminder)
    }
  }

  def loadReminders: List[Reminder] = {
    val file = getFile ("RemindersPath", "./reminders.txt")
    if (file.exists) {
      Source.fromFile (file, "UTF8").getLines ().map { line =>
        val fields = line.split ("///")
        new Reminder (fields (0), fields (1), LocalDate.parse (fields (2)))
      }.toList
    } else
      Nil
  }

  def saveReminders () = {
    panels.foreach (_.saveData ())
    val out = new PrintWriter (new BufferedWriter (new FileWriter (getFile ("RemindersPath", "./reminders.txt"))))
    reminders.foreach { r =>
      out.println (r.email + "///" + r.task + "///" + r.date)
    }
    out.close ()
  }

  def addReminder (reminder: Reminder) = {
    reminders = reminder :: reminders
    panels = new ReminderPanel (reminder) :: panels
    table.contents += panels.head
    revalidate ()
  }

  def removeReminder (reminder: Reminder) = {
    reminders = reminders.filter (_ != reminder)
    getPanel (reminder).foreach { panel =>
      panels = panels.filter (_ != panel)
      table.contents -= panel
    }
    revalidate ()
    repaint ()
  }

  def getPanel (reminder: Reminder): Option[ReminderPanel] =
    panels.find (_.reminder == reminder)

  def emailText: String =
    (for (person <- reminders.map (_.email).distinct) yield {
      val tasks = reminders.filter (_.email == person).filterNot (_.date.isAfter (now)).map (_.task)
      val prefix = "to: " + person + "\nHello,\n\nAs a reminder, please could you"
      val suffix = "\nThanks,\nSimon"
      tasks.size match {
        case 0 => ""
        case 1 => prefix + " " + tasks.head + "\n" + suffix
        case _ => prefix + ":\n" + tasks.map (" - " + _ + "\n").mkString + suffix
      }}).mkString ("\n\n\n")

  def regenerate () =
    emailsArea.text = emailText

  layout (emailsArea) = South
  layout (table) = Center
  layout (buttons) = North

  listenTo (addButton, generateButton, saveButton)
  reactions += {
    case ButtonClicked (button) if button == addButton =>
      addReminder (new Reminder ("simon.miles@kcl.ac.uk", "do task", now))
    case ButtonClicked (button) if button == generateButton =>
      saveReminders ()
      regenerate ()
    case ButtonClicked (button) if button == saveButton =>
      saveReminders ()
  }

  regenerate ()
}
