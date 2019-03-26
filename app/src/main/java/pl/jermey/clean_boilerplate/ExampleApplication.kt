package pl.jermey.clean_boilerplate

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import pl.jermey.injector.dataModule
import pl.jermey.injector.domainModule

class ExampleApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ExampleApplication)
            modules(applicationModule, domainModule, dataModule, viewModelLocator)
        }
    }
}