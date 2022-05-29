package com.example.moshi.data

import android.util.Log
import com.squareup.moshi.Moshi
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.lang.Exception

class MovieRepository {
    fun searchMovie(
        title: String,
        onSearchedCallback: (RemoteMovie?) -> Unit,
        errorCallback: (e: Throwable) -> Unit
    ): Call {
        return Network.getSearchMovieCall(
            title = title
        ).apply {
            enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.e("Server", "execute request error = ${e.message}", e)
                    errorCallback(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val responseString = response.body?.string().orEmpty()
                        val movie = parseMovieResponse(responseString, errorCallback)
                        onSearchedCallback(movie)
                        Log.d("Server", "response string = $responseString")
                        Log.d("Server", "response successful = ${response.isSuccessful}")
                    } else {
                        errorCallback(Throwable("Wrong server answer"))
                    }
                }

            })
        }
    }

    private fun parseMovieResponse(
        responseBodyString: String,
        errorCallback: (e: Throwable) -> Unit
    ): RemoteMovie? {
        val jsonObject = JSONObject(responseBodyString)
        val responseError = jsonObject.getString("Response")
        if (responseError == "True") {
            val moshi = Moshi.Builder().build()
            val adapter = moshi.adapter(RemoteMovie::class.java).nonNull()
            try {
                val movie = adapter.fromJson(responseBodyString)!!
                return RemoteMovie(
                    id = movie.id,
                    title = movie.title,
                    year = movie.year,
                    rated = movie.rated,
                    genre = movie.genre,
                    posterUrl = movie.posterUrl,
                    ratings = movie.ratings
                )
            } catch (e: Exception) {
                errorCallback(e)
            }
        } else {
            errorCallback(Exception(jsonObject.getString("Error")))
        }
        return null
    }
}