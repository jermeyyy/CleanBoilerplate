package pl.jermey.clean_boilerplate.viewmodel

import pl.jermey.domain.model.example.Post
import pl.jermey.domain.usecase.GetExampleDataUseCase

class ExampleViewModel(
        private val getExampleDataUseCase: GetExampleDataUseCase
) : StatefulViewModel<ExampleViewModel.ExampleState, ExampleViewModel.ExampleAction>(ExampleState.Loading) {


    override fun dispatchAction(action: ExampleAction) {
        when (action) {
            is ExampleAction.GetData -> getData()
            is ExampleAction.PostData -> postData(action.data)
        }
    }

    private fun postData(data: List<Post>) {
        
    }

    fun getData() = launch {
        getExampleDataUseCase.execute().subscribe(
                { data -> state.postValue(ExampleState.DataLoaded(data)) },
                { error -> state.postValue(ExampleState.Error(error)) }
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

fun ExampleViewModel.ExampleState.errorMessage(): String =
        (this as? ExampleViewModel.ExampleState.Error)?.throwable?.message ?: ""


fun ExampleViewModel.ExampleState.data(): String =
        (this as? ExampleViewModel.ExampleState.DataLoaded)?.data?.toString() ?: ""