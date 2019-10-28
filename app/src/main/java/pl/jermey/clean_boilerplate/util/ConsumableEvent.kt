package pl.jermey.clean_boilerplate.util

import io.reactivex.Observable
import java.util.concurrent.atomic.AtomicBoolean

@Suppress("unused")
class ConsumableEvent<T>(val event: T) {

  private val _consumed = AtomicBoolean(false)

  val consumed: Boolean
    get() = _consumed.get()

  fun consume() {
    _consumed.set(true)
  }

}

inline fun <reified T> Observable<ConsumableEvent<T>>.consumeAndMap(): Observable<T> =
  filter { it.consumed.not() }
    .doAfterNext { it.consume() }
    .map { it.event }