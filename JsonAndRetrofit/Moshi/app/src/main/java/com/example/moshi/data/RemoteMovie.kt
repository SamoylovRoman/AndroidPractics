package com.example.moshi.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.net.URL

@JsonClass(generateAdapter = true)
data class RemoteMovie(
    @Json(name = "imdbID")
    val id: String,
    @Json(name = "Title")
    val title:String,
    @Json(name = "Year")
    val year: Int,
    @Json(name = "Rated")
    val rated: MovieRating = MovieRating.GENERAL,
    @Json(name = "Genre")
    val genre: String,
    @Json(name = "Poster")
    val posterUrl: String,
    @Json(name = "Ratings")
    val ratings: List<Score> = emptyList()
)
