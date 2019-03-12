package pl.jermey.data.remote.example

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import org.buffer.android.boilerplate.data.source.ExampleDataSource
import pl.jermey.data.remote.example.mapper.ExampleDataMapper
import pl.jermey.data.remote.example.request.ExampleRequest
import pl.jermey.domain.model.example.ExampleModel

class ExampleRemoteDataSource(
    private val exampleService: ExampleService,
    private val exampleDataMapper: ExampleDataMapper
) : ExampleDataSource {

    override fun clearCachedData(): Completable {
        throw NotImplementedError()
    }

    override fun saveExampleData(data: List<ExampleModel>): Completable {
        return exampleService.saveData(ExampleRequest(data))
    }

    override fun getExampleData(): Flowable<List<ExampleModel>> {
        return exampleService.getData().map { exampleDataMapper.mapFromRemote(it) }
    }

    override fun isCached(): Single<Boolean> {
        throw NotImplementedError()
    }

    override fun setLastCacheTime(lastCache: Long) {
        throw NotImplementedError()
    }

    override fun isExpired(): Boolean {
        throw NotImplementedError()
    }

}