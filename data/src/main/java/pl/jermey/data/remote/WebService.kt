package pl.jermey.data.remote

import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


inline fun <reified T> createWebService(
    okHttpClient: OkHttpClient,
    url: String,
    converterFactory: Converter.Factory
): T {
    val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .client(okHttpClient)
        .addConverterFactory(converterFactory)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    return retrofit.create(T::class.java)
}

fun createOkHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
        .apply {
            // TODO apply additional config
        }
        .build()
}

fun createGsonConverter(): Converter.Factory = GsonConverterFactory.create(Gson())