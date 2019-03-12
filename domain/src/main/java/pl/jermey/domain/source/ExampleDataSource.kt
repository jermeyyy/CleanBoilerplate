package org.buffer.android.boilerplate.data.source

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import pl.jermey.domain.model.example.ExampleModel

interface ExampleDataSource {

    fun clearCachedData(): Completable

    fun saveExampleData(data: List<ExampleModel>): Completable

    fun getExampleData(): Flowable<List<ExampleModel>>

    fun isCached(): Single<Boolean>

    fun setLastCacheTime(lastCache: Long)

    fun isExpired(): Boolean

}