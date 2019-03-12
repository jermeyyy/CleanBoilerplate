package pl.jermey.clean_boilerplate

import android.app.Application
import org.koin.android.ext.android.startKoin
import pl.jermey.injector.dataModule
import pl.jermey.injector.domainModule

class ExampleApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(applicationModule, viewModelLocator, domainModule, dataModule))
    }
}