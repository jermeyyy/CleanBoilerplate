package pl.jermey.clean_boilerplate.viewmodel

import androidx.lifecycle.LiveData
import pl.jermey.clean_boilerplate.viewmodel.ExampleViewModel.ExampleState.*
import pl.jermey.domain.model.example.Post
import pl.jermey.domain.usecase.GetExampleDataUseCase

class ExampleViewModel(
        private val getExampleDataUseCase: GetExampleDataUseCase
) : StatefulViewModel<ExampleViewModel.ExampleState, ExampleViewModel.ExampleAction>(Loading) {

    val data: LiveData<String> = state.bind<DataLoaded, String> { state -> state?.data?.toString() }
    val error: LiveData<String> = state.bind<Error, String> { state -> state?.throwable?.toString() }
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
        getExampleDataUseCase.execute().subscribe(
                { data -> state.postValue(DataLoaded(data)) },
                { error -> state.postValue(Error(error)) }
        )
    }

    sealed class ExampleState : State {
        object Loading : ExampleState()
        data class DataLoaded(val data: List<Post>) : ExampleState()
        data class Error(val throwable: Throwable) : ExampleState()
    }

    sealed class ExampleAction : Action {
        object GetData : ExampleAction()
        data class PostData(val data: List<Post>) : ExampleAction()
    }

}
