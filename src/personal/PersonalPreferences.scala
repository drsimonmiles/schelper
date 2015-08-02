import schelper.core.SchelperConstants.ActionColour
import schelper.core.PreferencesList
import schelper.navigate.Navigable

object PersonalPreferences extends PreferencesList (Map (
  "Citing papers file" -> "ListPath",
  "Paper signatures file" -> "PaperSignatures",
  "Reminders file" -> "RemindersPath" // ADD ITEMS HERE
)) with Navigable {
  val linkName = "Set preferences"
  val linkColour = ActionColour
}
