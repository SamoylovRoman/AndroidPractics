package com.example.moshi.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Score(
    @Json(name = "Source")
    val source: String,
    @Json(name = "Value")
    val value: String
)
