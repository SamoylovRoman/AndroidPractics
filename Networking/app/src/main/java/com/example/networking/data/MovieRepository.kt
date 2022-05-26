package com.example.networking.data

import android.util.Log
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class MovieRepository {
    fun searchMovie(
        title: String,
        year: String,
        type: String,
        onSearchedCallback: (List<RemoteMovie>) -> Unit,
        errorCallback: (e: Throwable) -> Unit
    ): Call {
        return Network.getSearchMovieCall(title = title, year = year, type = type).apply {
            enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.e("Server", "execute request error = ${e.message}", e)
//                    onSearchedCallback(emptyList())
                    errorCallback(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val responseString = response.body?.string().orEmpty()
                        val movies = parseMovieResponse(responseString)
                        onSearchedCallback(movies)
                        Log.d("Server", "response string = $responseString")
                        Log.d("Server", "response successful = ${response.isSuccessful}")
                    } else {
                        errorCallback(Throwable("Wrong server answer"))
//                        onSearchedCallback(emptyList())
                    }
                }

            })
        }
    }

    private fun parseMovieResponse(responseBodyString: String): List<RemoteMovie> {
        try {
            val jsonObject = JSONObject(responseBodyString)
            val movieArray = jsonObject.getJSONArray("Search")
            val movies = (0 until movieArray.length()).map { index ->
                movieArray.getJSONObject(index)
            }.map { movieJsonObject ->
                val title = movieJsonObject.getString("Title")
                val year = movieJsonObject.getString("Year")
                val id = movieJsonObject.getString("imdbID")
                val type = movieJsonObject.getString("Type")
                val postLink = movieJsonObject.getString("Poster")
                RemoteMovie(id = id, title = title, year = year, type = type, posterLink = postLink)
            }
            return movies
        } catch (e: JSONException) {
            Log.e("Server", "parse response error = ${e.message}", e)
            return emptyList()
        }
    }
}