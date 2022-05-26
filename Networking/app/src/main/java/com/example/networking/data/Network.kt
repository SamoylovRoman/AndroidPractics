package com.example.networking.data

import okhttp3.Call
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor

object Network {
    private val client = OkHttpClient.Builder()
        .addNetworkInterceptor(CustomApiInterceptor())
        .addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()

    fun getSearchMovieCall(
        title: String,
        year: String = "",
        type: String = "",
        page: String = ""
    ): Call {
        val url = HttpUrl.Builder()
            .scheme("http")
            .host("www.omdbapi.com")
            .addQueryParameter("s", title)
            .addQueryParameter("y", year)
            .addQueryParameter("type", type)
            .addQueryParameter("page", page)
            .build()
        val request = Request.Builder()
            .get()
            .url(url)
            .build()
        return client.newCall(request)
    }
}