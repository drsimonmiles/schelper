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
  "CCSWAJR" -> "CCSWAJRDirectory",
  "CCS1038" -> "CCS1038Directory",
  "CCS9621" -> "CCS9621Directory"
)) with Navigable {
  val linkName = "Set budget directories"
  val linkColour = ActionColour
}
