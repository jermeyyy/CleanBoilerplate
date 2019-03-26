package pl.jermey.clean_boilerplate

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.jermey.clean_boilerplate.viewmodel.ExampleViewModel


val viewModelLocator = module {
    viewModel { ExampleViewModel(get()) }
}


