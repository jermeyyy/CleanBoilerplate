package pl.jermey.domain.repository

import io.reactivex.Completable
import io.reactivex.Flowable
import org.buffer.android.boilerplate.data.source.ExampleDataSourceFactory
import pl.jermey.domain.model.example.ExampleModel

class ExampleDataRepository(private val factory: ExampleDataSourceFactory) : ExampleRepository {
    override fun clearData(): Completable {
        return factory.retrieveCacheDataStore().clearCachedData()
    }

    override fun saveData(data: List<ExampleModel>): Completable {
        return factory.retrieveCacheDataStore().saveExampleData(data)
    }

    override fun getData(): Flowable<List<ExampleModel>> {
        return factory.retrieveCacheDataStore().isCached()
            .flatMapPublisher {
                factory.retrieveDataStore(it).getExampleData()
            }
            .flatMap {
                saveData(it).toSingle { it }.toFlowable()
            }
    }
}