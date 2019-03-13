package pl.jermey.clean_boilerplate.viewmodel

import androidx.lifecycle.MutableLiveData
import pl.jermey.domain.model.example.ExampleModel
import pl.jermey.domain.usecase.GetExampleDataUseCase

class ExampleViewModel2(
    private val getExampleDataUseCase: GetExampleDataUseCase
) : AbstractViewModel() {

    val state = MutableLiveData<State>()

    fun getData() = launch {
        getExampleDataUseCase.execute().subscribe(
            { data -> state.postValue(State.DataLoaded(data)) },
            { error -> state.postValue(State.Error(error)) }
        )
    }

    sealed class State {
        data class DataLoaded(val data: List<ExampleModel>) : State()
        data class Error(val throwable: Throwable) : State()
    }

}