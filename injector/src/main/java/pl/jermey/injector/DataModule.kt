package pl.jermey.injector

import org.koin.core.qualifier.named
import org.koin.dsl.module
import pl.jermey.data.remote.createGsonConverter
import pl.jermey.data.remote.createOkHttpClient
import pl.jermey.data.remote.createWebService
import pl.jermey.data.remote.example.ExampleRemoteDataSource
import pl.jermey.data.remote.example.ExampleService
import pl.jermey.data.remote.example.mapper.ExampleDataMapper
import pl.jermey.domain.source.ExampleDataSource
import pl.jermey.domain.source.ExampleDataSourceFactory

val dataModule = module {
    single(named("gsonConverter")) { createGsonConverter() }
    single { createOkHttpClient() }
    single {
        createWebService<ExampleService>(
            get(),
            "https://jsonplaceholder.typicode.com/",
            get(named("gsonConverter"))
        )
    }

    single(named("remote")) { ExampleRemoteDataSource(get(), get()) as ExampleDataSource }
    single(named("db")) { ExampleRemoteDataSource(get(), get()) as ExampleDataSource }

    factory { ExampleDataMapper() }
    single { ExampleDataSourceFactory(get(named("remote")), get(named("db"))) }
}