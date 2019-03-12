package pl.jermey.data.remote.example

import io.reactivex.Completable
import io.reactivex.Flowable
import pl.jermey.data.remote.example.request.ExampleRequest
import pl.jermey.data.remote.example.response.ExampleResponse
import retrofit2.http.GET
import retrofit2.http.POST

interface ExampleService {

    @POST("save")
    fun saveData(request: ExampleRequest): Completable

    @GET("get")
    fun getData(): Flowable<ExampleResponse>

}