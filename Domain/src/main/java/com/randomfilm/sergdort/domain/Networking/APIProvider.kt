package com.randomfilm.sergdort.domain.Networking

import com.google.gson.*
import com.randofilm.sergdort.domain.Film.FilmsAPI
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class APIProvider {
    object Constats {
        val BASE_URL = "https://api.themoviedb.org"
        val API_KEY = "d4f0bdb3e246e2cb3555211e765c89e3"
    }
    private val client = OkHttpClient.Builder()
            .addInterceptor(APIKeyInterceptor(Constats.API_KEY))
            .build()

    private val gson = GsonBuilder()
            .setDateFormat("yyyy-MM-dd")
            .create()

    private val retrofit = Retrofit.Builder()
            .baseUrl(Constats.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()


    fun makeFilmsAPI(): FilmsAPI = retrofit.create(FilmsAPI::class.java)
}
