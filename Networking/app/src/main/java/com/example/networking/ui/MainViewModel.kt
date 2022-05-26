package com.example.networking.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.networking.R
import com.example.networking.data.MovieRepository
import com.example.networking.data.RemoteMovie
import com.example.networking.utils.SingleLiveEvent
import okhttp3.Call

class MainViewModel : ViewModel() {
    private val repository = MovieRepository()

    private var currentCall: Call? = null

    private val _movies = MutableLiveData(emptyList<RemoteMovie>())
    val movies: LiveData<List<RemoteMovie>>
        get() = _movies

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _isAddingLoading = MutableLiveData<Boolean>()
    val isAddingLoading: LiveData<Boolean>
        get() = _isAddingLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private val _emptyList = SingleLiveEvent<Unit>()
    val emptyList: LiveData<Unit>
        get() = _emptyList

    private val _moviesCount = MutableLiveData<Int>()
    val moviesCount: LiveData<Int>
        get() = _moviesCount

    fun search(title: String, year: String, type: String, page: String) {
        if (page.toInt() > 1) {
            _isAddingLoading.postValue(true)
        } else _isLoading.postValue(true)
        currentCall = repository.searchMovie(title, year, type, page, { movies ->
            if (page.toInt() > 1) {
                _movies.postValue(_movies.value!! + createItemsColor(movies))
            } else {
                _movies.postValue(createItemsColor(movies))
            }
            if (page.toInt() > 1) {
                _isAddingLoading.postValue(false)
            } else _isLoading.postValue(false)
            if (movies.isEmpty()) {
                _emptyList.postValue(Unit)
            }
            currentCall = null
        }, { error ->
            _isLoading.postValue(false)
            _error.postValue(error.message)
        }, { count ->
            _moviesCount.postValue(count)
        })
    }

    private fun createItemsColor(movieList: List<RemoteMovie>): List<RemoteMovie> {
        return movieList.mapIndexed { index, remoteMovie ->
            if (index % 2 != 0) {
                remoteMovie.copy(itemColor = R.color.colorItemGray)
            } else {
                remoteMovie.copy(itemColor = R.color.colorItemWhite)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        currentCall?.cancel()
    }
}