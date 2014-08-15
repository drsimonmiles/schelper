package schelper.navigate

import scala.swing.BorderPanel

object History {
  private type T = BorderPanel
  private val memory = 20
  private val history = Array.fill[Option[T]] (memory) (None)
  private var next = 0

  def recorded (screen: T) = {
    history (next) = Some (screen)
    next = (next + 1) % memory
    history (next) = None
    screen
  }

  def reverse (): Option[T] = {
    val previous = if (next <= 1) memory - 2 + next else next - 2
    if (history (previous).isDefined) next = (previous + 1) % memory
    history (previous)
  }
}
