package pl.jermey.clean_boilerplate.util

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

interface DisposablesOwner {

  val disposables: CompositeDisposable

  fun clearDisposables() = disposables.clear()

  fun removeDisposable(disposable: Disposable): Boolean = disposables.remove(disposable)

  fun launch(job: () -> Disposable): Disposable {
    val disposable = job()
    disposables.add(disposable)
    return disposable
  }
}