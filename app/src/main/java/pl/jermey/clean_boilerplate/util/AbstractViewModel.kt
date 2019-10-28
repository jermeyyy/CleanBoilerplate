package pl.jermey.clean_boilerplate.util

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import pl.jermey.clean_boilerplate.util.DisposablesOwner

abstract class AbstractViewModel : ViewModel(), DisposablesOwner {

  override val disposables = CompositeDisposable()

  @CallSuper
  override fun onCleared() {
    super.onCleared()
    clearDisposables()
  }
}