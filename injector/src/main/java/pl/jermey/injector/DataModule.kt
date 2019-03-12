package pl.jermey.injector

import org.buffer.android.boilerplate.data.source.ExampleDataSource
import org.buffer.android.boilerplate.data.source.ExampleDataSourceFactory
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
            "http://example.com/",
            get("gsonConverter")
        )
    }

    single("remote") { ExampleRemoteDataSource(get(), get()) as ExampleDataSource }
    single("db") { ExampleRemoteDataSource(get(), get()) as ExampleDataSource }

    factory { ExampleDataMapper() }
    single { ExampleDataSourceFactory(get("remote"), get("db")) }
}