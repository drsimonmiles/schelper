package schelper.develop

import java.io.File
import scala.swing.event.Event

case class DirectoryChanged (directory: File) extends Event
