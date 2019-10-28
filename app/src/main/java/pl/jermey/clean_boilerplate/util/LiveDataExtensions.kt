package pl.jermey.clean_boilerplate.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations

inline fun <P, N> LiveData<P>.map(crossinline transformation: (P) -> N): LiveData<N> =
  Transformations.map(this) { transformation(it) }

inline fun <P, N> LiveData<P>.switchMap(crossinline action: (P) -> LiveData<N>): LiveData<N> =
  Transformations.switchMap(this) { action(it) }

inline fun <T> LiveData<T>.filter(crossinline predicate: (T) -> Boolean): LiveData<T> {
  val mediator = MediatorLiveData<T>()
  mediator.addSource(this) { t -> if (predicate(t)) mediator.postValue(t) }
  return mediator
}
