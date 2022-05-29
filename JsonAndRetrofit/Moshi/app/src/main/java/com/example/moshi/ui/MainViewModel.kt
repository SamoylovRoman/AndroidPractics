package com.example.moshi.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moshi.data.MovieRepository
import com.example.moshi.data.RemoteMovie
import okhttp3.Call

class MainViewModel : ViewModel() {
    private val repository = MovieRepository()

    private var currentCall: Call? = null

    private val _movie = MutableLiveData<RemoteMovie>()
    val movie: LiveData<RemoteMovie>
        get() = _movie

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _isAddingLoading = MutableLiveData<Boolean>()
    val isAddingLoading: LiveData<Boolean>
        get() = _isAddingLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

/*    private val _emptyList = SingleLiveEvent<Unit>()
    val emptyList: LiveData<Unit>
        get() = _emptyList*/

    fun search(title: String) {
        _isLoading.postValue(true)
        currentCall = repository.searchMovie(title, { movie ->
            if (movie != null) {
                _movie.postValue(movie)
            }
            _isLoading.postValue(true)
            currentCall = null
        }, { error ->
            _isLoading.postValue(false)
            _error.postValue(error.message)
        })
    }

    override fun onCleared() {
        super.onCleared()
        currentCall?.cancel()
    }
}