package pl.jermey.clean_boilerplate.viewmodel

import androidx.annotation.CallSuper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

interface State
interface Action

abstract class StatefulViewModel<STATE : State, ACTION : Action>(initialState: STATE) : ViewModel() {

    private val disposables = CompositeDisposable()

    val state = MutableLiveData<STATE>()

    init {
        state.postValue(initialState)
    }

    fun launch(job: () -> Disposable) {
        disposables.add(job())
    }

    fun invokeAction(action: ACTION) {
        dispatchAction(action)
    }

    protected abstract fun dispatchAction(action: ACTION)

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

}