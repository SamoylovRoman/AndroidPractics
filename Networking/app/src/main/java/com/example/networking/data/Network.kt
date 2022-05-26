package com.example.networking.data

import okhttp3.Call
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor

object Network {
    //    private const val MOVIE_API_KEY = "eed99f49"
    private val client = OkHttpClient.Builder()
        .addNetworkInterceptor(CustomApiInterceptor())
        .addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()

    //        http://www.omdbapi.com/?apikey=[yourkey]&

    fun getSearchMovieCall(title: String, year: String = "", type: String = ""): Call {
        val url = HttpUrl.Builder()
            .scheme("http")
            .host("www.omdbapi.com")
//            .addQueryParameter("apikey", MOVIE_API_KEY)
            .addQueryParameter("s", title)
            .addQueryParameter("page", year)
            .addQueryParameter("type", type)
            .build()
        val request = Request.Builder()
            .get()
            .url(url)
            .build()
        return client.newCall(request)
    }
}