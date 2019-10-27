package pl.jermey.domain.repository

import io.reactivex.Completable
import io.reactivex.Flowable
import pl.jermey.domain.source.ExampleDataSourceFactory
import pl.jermey.domain.model.example.PostData

class ExampleDataRepository(private val factory: ExampleDataSourceFactory) : ExampleRepository {
    override fun clearData(): Completable {
        return factory.retrieveCacheDataStore().clearCachedData()
    }

    override fun saveData(data: List<PostData>): Completable {
        return factory.retrieveCacheDataStore().saveExampleData(data)
    }

    override fun getData(): Flowable<List<PostData>> {
        return factory.retrieveCacheDataStore().isCached()
            .flatMapPublisher {
                factory.retrieveDataStore(it).getExampleData()
            }
            .flatMap {
                saveData(it).toSingle { it }.toFlowable()
            }
    }
}