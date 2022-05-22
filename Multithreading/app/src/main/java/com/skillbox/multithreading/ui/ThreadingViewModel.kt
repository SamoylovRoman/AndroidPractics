package com.skillbox.multithreading.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skillbox.multithreading.R
import com.skillbox.multithreading.data.Movie
import com.skillbox.multithreading.data.MovieRepository
import com.skillbox.multithreading.utils.SingleLiveEvent

class ThreadingViewModel : ViewModel() {
    private val repository = MovieRepository()

    private val _movies = MutableLiveData(emptyList<Movie>())
    val movies: LiveData<List<Movie>?>
        get() = _movies

    private val _showToast = SingleLiveEvent<Unit>()
    val showToast: LiveData<Unit>
        get() = _showToast

    val movieIds = listOf(
        "tt0111161",
        "tt0068646",
        "tt0468569",
        "tt0071562",
        "tt0050083",
        "tt0108052",
        "tt0167260",
        "tt0110912",
        "tt0120737",
        "tt0060196",
        "tt0109830",
        "tt0137523",
        "tt1375666",
        "tt0167261",
        "tt0080684",
        "tt0133093",
        "tt0099685",
        "tt0073486",
        "tt0114369",
        "tt0047478"
    )

    fun requestMovie(id: String) = repository.getMovieById(id)

    fun updateMoviesLiveData(newMoviesList: List<Movie>) {
        _movies.postValue(newMoviesList.mapIndexed { index, movie ->
            if (index % 2 != 0) {
                movie.copy(color = R.color.colorItemGray)
            } else {
                movie.copy(color = R.color.colorItemWhite)
            }
        })
    }

    fun requestMovies() {
        repository.fetchMovies(movieIds) { fetchedMoviesList ->
            updateMoviesLiveData(fetchedMoviesList)
        }
    }

    fun requestMoviesList(): List<Movie> {
        return repository.fetchMovies(movieIds)
    }

    fun getShowToast() {
        _showToast.postValue(Unit)
    }

    fun isEmptyLiveData(): Boolean = _movies.value!!.isEmpty()
}