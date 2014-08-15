package schelper.core

import scala.swing._

trait Audible extends Component with Publisher {
  def publishers = this :: componentPublishers.toList
  def componentPublishers: Iterable[Publisher]
}
