package schelper.core

import scala.swing._

trait Audible extends Component with Publisher {
  def componentPublishers: Iterable[Publisher]
  def opened () {}
  def publishers = this :: componentPublishers.toList
}
