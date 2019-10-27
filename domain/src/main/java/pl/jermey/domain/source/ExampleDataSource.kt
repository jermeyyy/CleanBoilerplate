package pl.jermey.domain.source

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import pl.jermey.domain.model.example.PostData

interface ExampleDataSource {

    fun clearCachedData(): Completable

    fun saveExampleData(data: List<PostData>): Completable

    fun getExampleData(): Flowable<List<PostData>>

    fun isCached(): Single<Boolean>

    fun setLastCacheTime(lastCache: Long)

    fun isExpired(): Boolean

}