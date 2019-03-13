package pl.jermey.clean_boilerplate

import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.context.ModuleDefinition
import org.koin.dsl.module.Module
import org.koin.dsl.module.module
import pl.jermey.clean_boilerplate.viewmodel.ExampleViewModel
import pl.jermey.clean_boilerplate.viewmodel.ExampleViewModel2

val viewModelLocator = module {
    submodules(example, example2)
}

fun ModuleDefinition.submodules(vararg modules: Module) {
    subModules.addAll(modules.map { it(koinContext) })
}

val example = module { viewModel { ExampleViewModel(get()) } }
val example2 = module { viewModel { ExampleViewModel2(get()) } }