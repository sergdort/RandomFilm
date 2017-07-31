package com.randofilm.sergdort.platform.Networking

import com.google.gson.GsonBuilder
import com.randofilm.sergdort.platform.Film.Film
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

internal class APIProvider {
    object Constants {
        val BASE_URL = "https://api.themoviedb.org"
        val API_KEY = "d4f0bdb3e246e2cb3555211e765c89e3"
    }
    private val client = OkHttpClient.Builder()
            .addInterceptor(APIKeyInterceptor(Constants.API_KEY))
            .build()

    private val gson = GsonBuilder()
            .setDateFormat("yyyy-MM-dd")
            .create()

    private val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()


    fun makeDiscoverAPI(): DiscoverAPI = retrofit.create(DiscoverAPI::class.java)

    fun makeFilmsAPI(): FilmsAPI = retrofit.create(FilmsAPI::class.java)
}
