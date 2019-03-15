package pl.jermey.data.remote.example

import io.reactivex.Completable
import io.reactivex.Flowable
import pl.jermey.data.remote.example.request.ExampleRequest
import pl.jermey.data.remote.example.response.ExampleResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ExampleService {

    @POST("posts")
    fun saveData(@Body request: ExampleRequest): Completable

    @GET("posts")
    fun getData(): Flowable<ExampleResponse>

}