package pl.jermey.clean_boilerplate.viewmodel

import androidx.lifecycle.LiveData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import pl.jermey.clean_boilerplate.util.Event
import pl.jermey.clean_boilerplate.util.State
import pl.jermey.clean_boilerplate.viewmodel.ExampleViewModel.ExampleState.*
import pl.jermey.domain.model.example.Post
import pl.jermey.domain.usecase.GetExampleDataUseCase
import java.util.concurrent.TimeUnit

class ExampleViewModel(
  private val getExampleDataUseCase: GetExampleDataUseCase,
  private val initialState: ExampleState = Empty
) : StatefulViewModel<ExampleViewModel.ExampleState, ExampleViewModel.ExampleEvent>(initialState) {

  val data: LiveData<String> = state.bind {
    instance<JustString> { state -> state.data }
    instance<DataLoaded> { state -> state.data.toString() }
    instance<Loading> { "Loading data" }
    default { "" }
  }

  val error: LiveData<String> =
    state.bindState<Error, String> { state -> state?.throwable?.toString() }
  val loading: LiveData<Boolean> = state.bindState<Loading, Boolean> { state -> state != null }

  override val stateGraph = stateGraph {
    state<Empty> {
      on<ExampleEvent.Action.GetData> {
        getData()
        transitionTo(Loading)
      }
    }
    state<Loading> {
      on<ExampleEvent.OnDataLoaded> { transitionTo(DataLoaded(it.data)) }
      on<ExampleEvent.OnError> { transitionTo(Error(it.error)) }
    }
    state<DataLoaded> {
      on<ExampleEvent.Action.PostData> {
        postData(it.data)
        transitionTo(Loading)
      }
      on<ExampleEvent.OnStringLoaded> {
        transitionTo(JustString(it.data))
      }
    }
    state<JustString> { }
    state<Error> { }
  }

  private fun postData(data: List<Post>) {

  }

  private fun getData() = launch {
    getExampleDataUseCase.execute()
      .subscribeBy(
        onNext = { invokeAction(ExampleEvent.OnDataLoaded(it)) },
        onError = { invokeAction(ExampleEvent.OnError(it)) }
      )

    // other data
    Observable.just("Hello")
      .delay(5, TimeUnit.SECONDS)
      .subscribeOn(Schedulers.newThread())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribeBy(
        onError = { invokeAction(ExampleEvent.OnError(it)) },
        onNext = { invokeAction(ExampleEvent.OnStringLoaded(it)) }
      )
  }

  sealed class ExampleState : State {
    object Empty : ExampleState()
    object Loading : ExampleState()
    data class JustString(val data: String) : ExampleState()
    data class DataLoaded(val data: List<Post>) : ExampleState()
    data class Error(val throwable: Throwable) : ExampleState()
  }

  sealed class ExampleEvent : Event {
    sealed class Action : ExampleEvent() {
      object GetData : Action()
      data class PostData(val data: List<Post>) : Action()
    }

    internal data class OnDataLoaded(val data: List<Post>) : ExampleEvent()
    internal data class OnStringLoaded(val data: String) : ExampleEvent()
    internal data class OnError(val error: Throwable) : ExampleEvent()
  }

}
