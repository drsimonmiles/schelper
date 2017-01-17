package settings4scala

import java.lang.System._
import scala.swing._

class Preferences {
  private lazy val preferences = java.util.prefs.Preferences.userNodeForPackage (this.getClass)
  private val LastRecordOpen = "LAST_RECORD_OPEN"
  val OpenInstanceDelay = 60000

  def getString (kind: String) =
    Option (preferences.get (kind, null))
  def getList (kind: String, delimiter: Char) =
    getString (kind).map (_.split (delimiter).toList).getOrElse (List[String] ())
  def getObject[T] (kind: String, parts: Int, default: String = "") (compose: List[String] => T): T =
    compose ((1 to parts).toList.map (p => getString (kind + "_" + p).getOrElse (default)))
  def getObjects[T] (kind: String, parts: Int, delimiter: Char, default: String = "") (compose: List[String] => T): List[T] =
    (1 to parts).map (p => getList (kind + "_" + p, delimiter)).toList.transpose.map (compose).toList
  def isOpenInstance =
    preferences.getLong (LastRecordOpen, 0) >= currentTimeMillis - OpenInstanceDelay
  def loadPosition (kind: String, window: MainFrame) {
    val x = preferences.getInt (kind + "_WINDOW_X", 0)
    val y = preferences.getInt (kind + "_WINDOW_Y", 0)
    val width = preferences.getInt (kind + "_WINDOW_WIDTH", 400)
    val height = preferences.getInt (kind + "_WINDOW_HEIGHT", 400)
    window.location = new Point (x, y)
    window.size = new Dimension (width, height)
  }
  def putString (kind: String, value: String) =
    preferences.put (kind, value)
  def putList (kind: String, items: Seq[String], delimiter: Char) {
    putString (kind, items.mkString (delimiter.toString))
  }
  def putObject[T] (kind: String, obj: T) (decompose: T => Seq[String]) {
    decompose (obj).zipWithIndex.foreach (p => putString (kind + "_" + p._2, p._1))
  }
  def putObjects[T] (kind: String, objects: Seq[T], delimiter: Char) (decompose: T => Seq[String]) {
    objects.map (decompose).transpose.zipWithIndex.map (parts => putList (kind + "_" + parts._2, parts._1, delimiter))
  }
  def recordCloseInstance () {
    preferences.putLong (LastRecordOpen, 0)
  }
  def recordOpenInstance () {
    preferences.putLong (LastRecordOpen, currentTimeMillis)
  }
  def savePosition (kind: String, window: MainFrame) {
    preferences.putInt (kind + "_WINDOW_X", window.location.x)
    preferences.putInt (kind + "_WINDOW_Y", window.location.y)
    preferences.putInt (kind + "_WINDOW_WIDTH", window.size.width)
    preferences.putInt (kind + "_WINDOW_HEIGHT", window.size.height)
  }
}
