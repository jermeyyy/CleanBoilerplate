package pl.jermey.clean_boilerplate.util.state

import androidx.lifecycle.MutableLiveData
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class SavedStateDelegate<T>(private val key: String) :
  ReadOnlyProperty<StatefulViewModel<*, *>, MutableLiveData<T>> {

  override fun getValue(
    thisRef: StatefulViewModel<*, *>,
    property: KProperty<*>
  ): MutableLiveData<T> =
    thisRef.handle?.getLiveData<T>(key) ?: throw RuntimeException("No SavedStateHandle provided")

}