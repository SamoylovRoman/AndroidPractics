package com.skillbox.github.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RemoteFollowing(
    @Json(name = "id")
    val id: Long,
    @Json(name = "login")
    val login: String,
    @Json(name = "avatar_url")
    val avatarUrl: String
)