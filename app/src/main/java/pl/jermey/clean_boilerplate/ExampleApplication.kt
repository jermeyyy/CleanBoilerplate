package pl.jermey.clean_boilerplate

import android.app.Application
import android.util.Log
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import pl.jermey.injector.dataModule
import pl.jermey.injector.domainModule
import timber.log.Timber
import timber.log.Timber.DebugTree



class ExampleApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ExampleApplication)
            modules(applicationModule, domainModule, dataModule, viewModelLocator)
        }
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        } else {
            Timber.plant(CrashReportingTree)
        }
    }
}

object CrashReportingTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority == Log.VERBOSE || priority == Log.DEBUG) {
            return
        }
        // TODO    }
    }
}