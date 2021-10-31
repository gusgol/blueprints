package tech.hiregus.trendingprojects.data.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import tech.hiregus.trendingprojects.BuildConfig
import tech.hiregus.trendingprojects.utils.LocalDateTimeAdapter

object GithubApi {
    const val HOST = "https://api.github.com/"
}

fun provideGithubApi(): Retrofit {
    val client = OkHttpClient.Builder().apply {
        if (BuildConfig.DEBUG) {
            addInterceptor(
                HttpLoggingInterceptor().apply {
                    setLevel(HttpLoggingInterceptor.Level.BODY)
                }
            )
        }
        addInterceptor { chain ->
            var request = chain.request()
            val url = request.url.newBuilder().build()
            request = request.newBuilder().url(url).build()
            chain.proceed(request)
        }
    }
    return Retrofit.Builder()
        .baseUrl(GithubApi.HOST)
        .addConverterFactory(
            MoshiConverterFactory.create(
                Moshi.Builder()
                    .add(KotlinJsonAdapterFactory())
                    .add(LocalDateTimeAdapter()).build()
            )
        )
        .client(client.build())
        .build()
}