package pl.jermey.clean_boilerplate

import androidx.lifecycle.SavedStateHandle
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.definition.BeanDefinition
import org.koin.core.definition.Definition
import org.koin.core.module.Module
import org.koin.dsl.module
import pl.jermey.clean_boilerplate.util.state.StatefulViewModel
import pl.jermey.clean_boilerplate.view.example.ExampleViewModel


val viewModelLocator = module {
  savedStateViewModel { (handle: SavedStateHandle) ->
    ExampleViewModel(
      getExampleDataUseCase = get(),
      savedStateHandle = handle
    )
  }
}

inline fun <reified T : StatefulViewModel<*, *>> Module.savedStateViewModel(noinline definition: Definition<T>): BeanDefinition<T> {
  return viewModel(useState = true, definition = definition)
}

