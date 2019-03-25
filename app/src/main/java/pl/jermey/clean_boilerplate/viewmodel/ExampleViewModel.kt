package pl.jermey.clean_boilerplate.viewmodel

import androidx.lifecycle.LiveData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import pl.jermey.clean_boilerplate.viewmodel.ExampleViewModel.ExampleState.*
import pl.jermey.domain.model.example.Post
import pl.jermey.domain.usecase.GetExampleDataUseCase
import java.util.concurrent.TimeUnit

class ExampleViewModel(
    private val getExampleDataUseCase: GetExampleDataUseCase
) : StatefulViewModel<ExampleViewModel.ExampleState, ExampleViewModel.ExampleAction>(Loading) {

    val data: LiveData<String> = state.bindings {
        bind<JustString> { state -> state.data }
        bind<Loading> { "Loading data" }
        default { "WTF" }
    }

    val error: LiveData<String> = state.bind<Error, String> { state -> state?.throwable.toString() }
    val loading: LiveData<Boolean> = state.bind<Loading, Boolean> { state -> state != null }

    override fun dispatchAction(action: ExampleAction) {
        when (action) {
            is ExampleAction.GetData -> getData()
            is ExampleAction.PostData -> postData(action.data)
        }
    }

    private fun postData(data: List<Post>) {

    }

    private fun getData() = launch {
        Observable.just("Hello")
            .delay(2, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError = {
                    state.postValue(Error(it))
                },
                onNext = {
                    state.postValue(JustString(it))
                }
            )
        getExampleDataUseCase.execute().subscribe(
            { data -> state.postValue(DataLoaded(data)) },
            { error -> state.postValue(Error(error)) }
        )
    }

    sealed class ExampleState : State {
        object Loading : ExampleState()
        data class JustString(val data: String) : ExampleState()
        data class DataLoaded(val data: List<Post>) : ExampleState()
        data class Error(val throwable: Throwable) : ExampleState()
    }

    sealed class ExampleAction : Action {
        object GetData : ExampleAction()
        data class PostData(val data: List<Post>) : ExampleAction()
    }

}
