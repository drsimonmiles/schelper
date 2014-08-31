import java.io.File
import schelper.core.SchelperConstants.ActionColour
import schelper.core.SchelperPreferences.getString
import schelper.core.TextFileEditor

object PaperSignatures extends TextFileEditor (new File (getString ("PaperSignatures").getOrElse ("")), "Change paper signature", ActionColour)
