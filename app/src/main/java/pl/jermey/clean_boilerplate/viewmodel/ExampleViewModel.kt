package pl.jermey.clean_boilerplate.viewmodel

import androidx.lifecycle.LiveData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import pl.jermey.clean_boilerplate.util.StateMachine
import pl.jermey.clean_boilerplate.viewmodel.ExampleViewModel.ExampleState.*
import pl.jermey.domain.model.example.Post
import pl.jermey.domain.usecase.GetExampleDataUseCase
import java.util.concurrent.TimeUnit

class ExampleViewModel(
    private val getExampleDataUseCase: GetExampleDataUseCase
) : StatefulViewModel<ExampleViewModel.ExampleState, ExampleViewModel.ExampleEvent>(Empty) {

    val data: LiveData<String> = state.bind {
        state<JustString> { state -> state.data }
        state<DataLoaded> { state -> state.data.toString() }
        state<Loading> { "Loading data" }
        default { "" }
    }

    val error: LiveData<String> = state.bindState<Error, String> { state -> state?.throwable.toString() }
    val loading: LiveData<Boolean> = state.bindState<Loading, Boolean> { state -> state != null }

    override val stateMachine: StateMachine<ExampleState, ExampleEvent, Nothing> = StateMachine.create {
        initialState(initialState)
        state<ExampleState.Empty> {
            on<ExampleEvent.Action.GetData> {
                getData()
                transitionTo(Loading)
            }
        }
        state<ExampleState.Loading> {
            on<ExampleEvent.OnDataLoaded> { transitionTo(DataLoaded(it.data)) }
            on<ExampleEvent.OnError> { transitionTo(ExampleState.Error(it.error)) }
        }
        state<ExampleState.DataLoaded> {
            on<ExampleEvent.Action.PostData> {
                postData(it.data)
                transitionTo(Loading)
            }
            on<ExampleEvent.OnStringLoaded> {
                transitionTo(JustString(it.data))
            }
        }
        state<ExampleState.JustString> { }
        state<ExampleState.Error> { }
        onValidTransition {
            state.postValue(it.toState)
        }
    }

    private fun postData(data: List<Post>) {

    }

    private fun getData() = launch {
        getExampleDataUseCase.execute()
            .subscribeBy(
                onNext = { stateMachine.transition(ExampleEvent.OnDataLoaded(it)) },
                onError = { stateMachine.transition(ExampleEvent.OnError(it)) }
            )

        // other data
        Observable.just("Hello")
            .delay(5, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError = { stateMachine.transition(ExampleEvent.OnError(it)) },
                onNext = { stateMachine.transition(ExampleEvent.OnStringLoaded(it)) }
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
