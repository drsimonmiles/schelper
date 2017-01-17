import schelper.core.SchelperConstants.ActionColour
import schelper.core.SchelperPreferences.getFile
import schelper.core.TextFileEditor

object PaperSignatures extends TextFileEditor (getFile ("PaperSignatures", ""), "Change paper signature", ActionColour)
