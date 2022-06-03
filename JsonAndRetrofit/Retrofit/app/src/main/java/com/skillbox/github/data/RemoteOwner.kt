package com.skillbox.github.data

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
class RemoteOwner(
    @Json(name = "avatar_url")
    val avatar: String,
    @Json(name = "login")
    val ownerName: String
) : Parcelable