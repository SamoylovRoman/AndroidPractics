package com.skillbox.multithreading.data

import androidx.annotation.ColorRes
import com.google.gson.annotations.SerializedName

data class Movie(
    @ColorRes
    val color: Int,
    @SerializedName("imdbID")
    val id: String,
    @SerializedName("Poster")
    val iconLink: String,
    @SerializedName("Title")
    val title: String,
    @SerializedName("Year")
    val year: Int,
    @SerializedName("Genre")
    val genre: String,
    @SerializedName("imdbRating")
    val rating: String,
)