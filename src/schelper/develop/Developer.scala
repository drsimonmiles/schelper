package schelper.develop

import schelper.core.SchelperConstants._
import schelper.core.SubMenu

object Developer extends SubMenu ("Develop", SectionColour, List (new SourceDirectories (Sources), new SourceDirectories (Libraries), Compiler))
