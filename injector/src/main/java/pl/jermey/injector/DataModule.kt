package pl.jermey.injector

import pl.jermey.domain.source.ExampleDataSource
import pl.jermey.domain.source.ExampleDataSourceFactory
import org.koin.dsl.module.module
import pl.jermey.data.remote.example.ExampleRemoteDataSource
import pl.jermey.data.remote.example.ExampleService
import pl.jermey.data.remote.example.mapper.ExampleDataMapper
import pl.jermey.data.remote.createGsonConverter
import pl.jermey.data.remote.createOkHttpClient
import pl.jermey.data.remote.createWebService

val dataModule = module {
    single("gsonConverter") { createGsonConverter() }
    single { createOkHttpClient() }
    single {
        createWebService<ExampleService>(
            get(),
            "https://jsonplaceholder.typicode.com/",
            get("gsonConverter")
        )
    }

    single("remote") { ExampleRemoteDataSource(get(), get()) as ExampleDataSource }
    single("db") { ExampleRemoteDataSource(get(), get()) as ExampleDataSource }

    factory { ExampleDataMapper() }
    single { ExampleDataSourceFactory(get("remote"), get("db")) }
}