package pl.jermey.injector

import org.koin.dsl.module
import pl.jermey.domain.repository.ExampleDataRepository
import pl.jermey.domain.repository.ExampleRepository
import pl.jermey.domain.usecase.GetExampleDataUseCase

val domainModule = module {
    single { ExampleDataRepository(get()) as ExampleRepository }
    factory { GetExampleDataUseCase(get(), get(), get()) }
}