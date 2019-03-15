package pl.jermey.domain.source

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import pl.jermey.domain.model.example.Post

interface ExampleDataSource {

    fun clearCachedData(): Completable

    fun saveExampleData(data: List<Post>): Completable

    fun getExampleData(): Flowable<List<Post>>

    fun isCached(): Single<Boolean>

    fun setLastCacheTime(lastCache: Long)

    fun isExpired(): Boolean

}