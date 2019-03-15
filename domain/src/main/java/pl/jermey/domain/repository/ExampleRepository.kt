package pl.jermey.domain.repository

import io.reactivex.Completable
import io.reactivex.Flowable
import pl.jermey.domain.model.example.Post

interface ExampleRepository {

    fun clearData(): Completable

    fun saveData(data: List<Post>): Completable

    fun getData(): Flowable<List<Post>>

}