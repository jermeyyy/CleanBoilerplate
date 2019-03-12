package pl.jermey.clean_boilerplate

import org.koin.dsl.module.module
import pl.jermey.clean_boilerplate.util.UiThread
import pl.jermey.domain.executor.JobExecutor
import pl.jermey.domain.executor.PostExecutionThread
import pl.jermey.domain.executor.ThreadExecutor

val applicationModule = module {
    single { JobExecutor() as ThreadExecutor }
    single { UiThread() as PostExecutionThread }
}