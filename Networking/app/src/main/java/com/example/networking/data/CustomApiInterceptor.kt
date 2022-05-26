package com.example.networking.data

import okhttp3.Interceptor
import okhttp3.Response

class CustomApiInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val url = chain.request().url
            .newBuilder()
            .addQueryParameter("apikey", MOVIE_API_KEY)
            .build()
        val modifiedRequest = chain.request().newBuilder().url(url = url).build()
        return chain.proceed(modifiedRequest)
    }

    companion object {
        private const val MOVIE_API_KEY = "eed99f49"
    }
}