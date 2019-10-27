package pl.jermey.domain.usecase

import io.reactivex.Flowable
import pl.jermey.domain.executor.PostExecutionThread
import pl.jermey.domain.executor.ThreadExecutor
import pl.jermey.domain.interactor.FlowableUseCase
import pl.jermey.domain.model.example.PostData
import pl.jermey.domain.repository.ExampleRepository

class GetExampleDataUseCase(
    private val repository: ExampleRepository,
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread
) : FlowableUseCase<List<PostData>, Nothing>(threadExecutor, postExecutionThread) {

    override fun buildUseCaseObservable(params: Nothing?): Flowable<List<PostData>> {
        return repository.getData()
    }

}