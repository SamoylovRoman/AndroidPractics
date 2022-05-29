package com.example.moshi.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moshi.data.MovieRepository
import com.example.moshi.data.RemoteMovie
import com.squareup.moshi.Moshi
import okhttp3.Call
import java.lang.Exception

class MainViewModel : ViewModel() {
    private val repository = MovieRepository()

    private var currentCall: Call? = null

    private val _movie = MutableLiveData<RemoteMovie>()
    val movie: LiveData<RemoteMovie>
        get() = _movie

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    fun addScore(score: Pair<String, String>) {
        _movie.value?.ratings = _movie.value?.ratings!! + score
    }

    fun printJsonObjectInLog() {
        val moshi = Moshi.Builder()
            .build()
        val adapter = moshi.adapter(RemoteMovie::class.java).nonNull()
        try {
            val jsonMovie = adapter.toJson(_movie.value)
            Log.d("printJsonObjectInLog: ", jsonMovie.toString())
        } catch (e: Exception) {
            _error.postValue(e.message)
        }
    }

    fun search(title: String) {
        currentCall = repository.searchMovie(title, { movie ->
            if (movie != null) {
                _movie.postValue(movie)
            }
            currentCall = null
        }, { error ->
            _error.postValue(error.message)
        })
    }

    override fun onCleared() {
        super.onCleared()
        currentCall?.cancel()
    }
}