package personal

import scala.util.Random
import schelper.core.BasicForm
import schelper.core.SchelperConstants._
import schelper.navigate.{BackButton, Navigable, SideEffectButton}

object RandomNumber extends BasicForm with Navigable {
  val linkName = "Random numbers"
  val linkColour = ActionColour

  val minimum = field ("Minimum", "1")
  val maximum = field ("Maximum", "10")
  val generated = field ("Generated", "1")
  generated.enabled = false
  val chooser = new Random ()
  val generate = button (new SideEffectButton ("Generate", () => {
    generated.text = (chooser.nextInt (maximum.text.toInt - minimum.text.toInt) + minimum.text.toInt).toString
  }, ActionColour))
  val close = button (new BackButton ("Close", NavigateColour))

  val componentPublishers = Seq (generate, close)
}
