package com.skillbox.multithreading.data

import android.util.Log
import com.skillbox.multithreading.utils.Network
import java.util.*

class MovieRepository {

    fun getMovieById(id: String) = Network.getMovieById(id)

    fun fetchMovies(movieIds: List<String>, onMoviesFetched: (List<Movie>) -> Unit) {
        val allRequestedMovies = Collections.synchronizedList(mutableListOf<Movie>())
        Thread {
            val threads = movieIds.chunked(1).map { movieChunk ->
                Thread {
                    val requestedChunkMovies = movieChunk.mapNotNull { movieId ->
                        getMovieById(movieId).apply {
                            Log.d("fetchMovies", "${getMovieById(movieId)}")
                        }
                    }
                    allRequestedMovies.addAll(requestedChunkMovies)
                }
            }
            threads.forEach { thread ->
                thread.start()
            }
            threads.forEach { thread ->
                thread.join()
            }
            allRequestedMovies.sortByDescending { movie ->
                movie.rating
            }
            onMoviesFetched(allRequestedMovies)
        }.start()
    }

    fun fetchMovies(movieIds: List<String>): List<Movie> {
        val allRequestedMovies = Collections.synchronizedList(mutableListOf<Movie>())
        val threads = movieIds.chunked(1).map { movieChunk ->
            Thread {
                val requestedChunkMovies = movieChunk.mapNotNull { movieId ->
                    getMovieById(movieId).apply {
                        Log.d("fetchMovies", "${getMovieById(movieId)}")
                    }
                }
                allRequestedMovies.addAll(requestedChunkMovies)
            }
        }
        threads.forEach { thread ->
            thread.start()
        }
        threads.forEach { thread ->
            thread.join()
        }
        allRequestedMovies.sortByDescending { movie ->
            movie.rating
        }
        return allRequestedMovies
    }
}