package pl.jermey.domain.repository

import io.reactivex.Completable
import io.reactivex.Flowable
import pl.jermey.domain.model.example.PostData

interface ExampleRepository {

    fun clearData(): Completable

    fun saveData(data: List<PostData>): Completable

    fun getData(): Flowable<List<PostData>>

}