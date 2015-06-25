package personal

import schelper.core.PreferencesList
import schelper.core.SchelperConstants._
import schelper.navigate.Navigable

object BudgetDirectories extends PreferencesList (Map (
  "CCSAABR" -> "CCSAABRDirectory",
  "CCSLAHR" -> "CCSLAHRDirectory",
  "CCSYACR" -> "CCSYACRDirectory",
  "MHFYAKR" -> "MHFYAKRDirectory",
  "MHFLBAR" -> "MHFLBARDirectory",
  "MHFRBGR" -> "MHFRBGRDirectory",
  "CCSWAJR" -> "CCSWAJRDirectory"
)) with Navigable {
  val linkName = "Set budget directories"
  val linkColour = ActionColour
}
