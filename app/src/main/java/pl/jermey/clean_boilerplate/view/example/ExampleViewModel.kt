package pl.jermey.clean_boilerplate.view.example

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.parcel.Parcelize
import pl.jermey.clean_boilerplate.util.state.*
import pl.jermey.clean_boilerplate.view.example.ExampleViewModel.ExampleState.*
import pl.jermey.clean_boilerplate.view.example.model.Post
import pl.jermey.domain.usecase.GetExampleDataUseCase
import java.util.concurrent.TimeUnit

class ExampleViewModel(
  private val getExampleDataUseCase: GetExampleDataUseCase,
  initialState: ExampleState = Empty,
  savedStateHandle: SavedStateHandle
) : StatefulViewModel<ExampleViewModel.ExampleState, ExampleViewModel.ExampleEvent>(
  initialState,
  savedStateHandle
) {

  companion object {
    const val SOME_DATA_KEY = "someData"
  }

  val data: LiveData<String> = state.bind {
    instance<JustString> { state -> state.data }
    instance<DataLoaded> { state -> state.data.toString() }
    instance<Loading> { "Loading data" }
    default { "" }
  }

  val error: LiveData<String> =
    state.bindState<Error, String> { state -> state?.throwable?.toString() }
  val loading: LiveData<Boolean> = state.bindState<Loading, Boolean> { state -> state != null }

  val event = SingleLiveEvent<String>()

  val someData: MutableLiveData<String> by SavedStateDelegate(SOME_DATA_KEY)

  override val stateGraph = stateGraph {
    globalEvents {
      on<ExampleEvent.OnError> { dontTransition() }
    }
    state<Empty> {
      on<ExampleEvent.Action.GetData> {
        transitionTo(Loading, SideEffect.of { getData() })
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
    state<JustString> {
      onEnter {
        event.postValue(data)
      }
    }
    state<Error> { }
  }

  private fun postData(data: List<Post>) {

  }

  private fun getData() = launch {
    getExampleDataUseCase.execute()
      .subscribeBy(
        onNext = { data ->
          val posts = data.map { Post(it.userId, it.id, it.title, it.body) }
          invokeAction(ExampleEvent.OnDataLoaded(posts))
        },
        onError = { error ->
          invokeAction(ExampleEvent.OnError(error))
        }
      )

    // other data
    Observable.just("Hello")
      .delay(5, TimeUnit.SECONDS)
      .subscribeOn(Schedulers.newThread())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribeBy(
        onError = { error ->
          invokeAction(ExampleEvent.OnError(error))
        },
        onNext = { data ->
          invokeAction(ExampleEvent.OnStringLoaded(data))
        }
      )
  }

  sealed class ExampleState : State {
    @Parcelize
    object Empty : ExampleState()

    @Parcelize
    object Loading : ExampleState()

    @Parcelize
    data class JustString(val data: String) : ExampleState()

    @Parcelize
    data class DataLoaded(val data: List<Post>) : ExampleState()

    @Parcelize
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
    internal data class NowPlayingLoadingSuccess(val data: String) : ExampleEvent()
  }

}
