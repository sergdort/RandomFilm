package com.randomfilm.sergdort.domain.Networking

import okhttp3.*

class APIKeyInterceptor(private val apiKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val url = chain.request().url().newBuilder().addQueryParameter(API_KEY, apiKey).build()
        val request = chain.request().newBuilder().url(url).build()
        return chain.proceed(request)
    }

    companion object {
        private const val API_KEY = "api_key"
    }
}
