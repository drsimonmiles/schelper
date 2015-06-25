package schelper.core

import scala.swing.TextField
import schelper.core.SchelperConstants.{ActionColour, NavigateColour}
import schelper.core.SchelperPreferences.{getString, putString}
import schelper.navigate.BackButton

class PreferencesList (labelToPreference: Map[String, String]) extends BasicForm {
  val fields: Map[String, TextField] =
    for ((label, preference) <- labelToPreference) yield
      (preference, field (label, getString (preference).getOrElse ("")))
  val saveButton = button (new BackButton ("Save", () => save (), ActionColour))
  val cancelButton = button (new BackButton ("Cancel", NavigateColour))
  val componentPublishers = saveButton :: cancelButton :: fields.values.toList

  def save (): Unit =
    for (preference <- labelToPreference.values)
      putString (preference, fields (preference).text)
}
