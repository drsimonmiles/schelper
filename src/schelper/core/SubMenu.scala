package schelper.core

import schelper.navigate.Navigable
import scala.swing.Color

abstract class SubMenu (val linkName: String, val linkColour: Color, items: Seq[Navigable]) extends SchelperMenu (items) with Navigable
