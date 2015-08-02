import schelper.core.SchelperBase
import schelper.develop.Developer

object Schelper extends SchelperBase {
  def buttonList = List (Developer, CitedToList, PaperSignatures, BudgetSorter, Drafter, ReminderMails, PersonalPreferences)
}
