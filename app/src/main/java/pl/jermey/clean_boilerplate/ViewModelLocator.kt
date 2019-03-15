package pl.jermey.clean_boilerplate

import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.context.ModuleDefinition
import org.koin.dsl.module.Module
import org.koin.dsl.module.module
import pl.jermey.clean_boilerplate.viewmodel.ExampleViewModel

val viewModelLocator = module {
    submodules(example)
}

fun ModuleDefinition.submodules(module: Module) {
    definitions.addAll(module(koinContext).definitions)
}

val example = module { viewModel { ExampleViewModel(get()) } }
