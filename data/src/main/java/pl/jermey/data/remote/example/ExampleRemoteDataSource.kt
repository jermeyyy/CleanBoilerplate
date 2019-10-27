package pl.jermey.data.remote.example

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import pl.jermey.domain.source.ExampleDataSource
import pl.jermey.data.remote.example.mapper.ExampleDataMapper
import pl.jermey.data.remote.example.request.ExampleRequest
import pl.jermey.domain.model.example.PostData

class ExampleRemoteDataSource(
    private val exampleService: ExampleService,
    private val exampleDataMapper: ExampleDataMapper
) : ExampleDataSource {

    override fun clearCachedData(): Completable {
        throw NotImplementedError()
    }

    override fun saveExampleData(data: List<PostData>): Completable {
        return exampleService.saveData(ExampleRequest(data))
    }

    override fun getExampleData(): Flowable<List<PostData>> {
        return exampleService.getData().map { exampleDataMapper.mapFromRemote(it) }
    }

    override fun isCached(): Single<Boolean> {
        return Single.just<Boolean>(false)
    }

    override fun setLastCacheTime(lastCache: Long) {
        throw NotImplementedError()
    }

    override fun isExpired(): Boolean {
        throw NotImplementedError()
    }

}