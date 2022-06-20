package com.skillbox.github.data

import okhttp3.Interceptor
import okhttp3.Response

class CustomTokenInterceptor(
    private val headerName: String,
    private val headerValue: String
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val modifiedRequest =
            chain.request().newBuilder().addHeader(headerName, headerValue)
                .build()
        return chain.proceed(modifiedRequest)
    }
}