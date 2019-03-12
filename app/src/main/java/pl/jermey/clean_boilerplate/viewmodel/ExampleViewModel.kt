package pl.jermey.clean_boilerplate.viewmodel

import androidx.lifecycle.ViewModel
import pl.jermey.domain.usecase.GetExampleDataUseCase

class ExampleViewModel(private val getData: GetExampleDataUseCase) : ViewModel() {

    fun getData() :String = "hello"
}