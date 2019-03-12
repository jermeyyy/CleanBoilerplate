package pl.jermey.clean_boilerplate.util

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import pl.jermey.domain.executor.PostExecutionThread

class UiThread : PostExecutionThread {

    override val scheduler: Scheduler
        get() = AndroidSchedulers.mainThread()

}