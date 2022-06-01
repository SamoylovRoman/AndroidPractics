package com.example.moshi.data

import com.squareup.moshi.FromJson
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

class RemoteMovieCustomAdapter {

    @FromJson
    fun fromJson(customRemoteMovie: CustomRemoteMovie): RemoteMovie {
/*        var map = emptyMap<String, String>()
        customRemoteMovie.ratings.forEach { score ->
            map = map.plus(Pair(score.source, score.value))
        }*/
        val map = customRemoteMovie.ratings.associate { score ->
            score.source to score.value
        }
        return RemoteMovie(
            id = customRemoteMovie.id,
            title = customRemoteMovie.title,
            year = customRemoteMovie.year,
            rated = customRemoteMovie.rated,
            genre = customRemoteMovie.genre,
            posterUrl = customRemoteMovie.posterUrl,
            ratings = map
        )
    }

    @JsonClass(generateAdapter = true)
    data class CustomRemoteMovie(
        @Json(name = "imdbID")
        val id: String,
        @Json(name = "Title")
        val title: String,
        @Json(name = "Year")
        val year: Int,
        @Json(name = "Rated")
        val rated: MovieRating = MovieRating.GENERAL,
        @Json(name = "Genre")
        val genre: String,
        @Json(name = "Poster")
        val posterUrl: String,
        @Json(name = "Ratings")
        var ratings: List<Score> = emptyList()
    )
}