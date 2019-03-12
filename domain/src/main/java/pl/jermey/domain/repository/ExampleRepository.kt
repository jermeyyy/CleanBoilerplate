package pl.jermey.domain.repository

import io.reactivex.Completable
import io.reactivex.Flowable
import pl.jermey.domain.model.example.ExampleModel

interface ExampleRepository {

    fun clearData(): Completable

    fun saveData(data: List<ExampleModel>): Completable

    fun getData(): Flowable<List<ExampleModel>>

}