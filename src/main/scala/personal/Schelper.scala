import personal.RandomNumber
import schelper.core.SchelperBase
import schelper.develop.Developer

object Schelper extends SchelperBase {
  def buttonList = List (Developer, CitedToList, PaperSignatures, BudgetSorter, RandomNumber, Drafter,
    ReminderMails, PersonalPreferences)
}
