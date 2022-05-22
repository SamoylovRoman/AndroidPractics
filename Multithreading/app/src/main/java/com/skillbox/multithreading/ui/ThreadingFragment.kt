package com.skillbox.multithreading.ui

import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.skillbox.multithreading.R
import com.skillbox.multithreading.adapters.MovieAdapter
import com.skillbox.multithreading.databinding.FragmentThreadingBinding
import com.skillbox.multithreading.data.Movie
import com.skillbox.multithreading.extension.showToast
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class ThreadingFragment : Fragment() {
    private var _binding: FragmentThreadingBinding? = null
    private val binding get() = _binding!!

    private var movieAdapter: MovieAdapter? = null

    private val movieViewModel: ThreadingViewModel by viewModels()

    private val mainHandler = Handler(Looper.getMainLooper())
    private lateinit var backgroundHandler: Handler

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentThreadingBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMoviesList()
        observeViewModelState()
        initListeners()
        if (movieViewModel.isEmptyLiveData()) {
            requestMovies()
        }
    }

    private fun requestMovies() {
        movieViewModel.requestMovies()
    }

    private fun initListeners() {
        with(binding) {
            threadCallbackButton.setOnClickListener {
                movieViewModel.requestMovies()
            }
            threadHandlerButton.setOnClickListener {
                requestMoviesWithHandler()
            }
            threadPoolButton.setOnClickListener {
                requestMoviesWithThreadPool()
            }
            swipeContainer.setOnRefreshListener {
                requestMovies()
                swipeContainer.isRefreshing = false
                mainHandler.postDelayed({
                    movieViewModel.getShowToast()
                }, THREAD_DELAY)
            }
        }

    }

    private fun requestMoviesWithThreadPool() {
        val coreCount = Runtime.getRuntime().availableProcessors()
        val executor = Executors.newFixedThreadPool(coreCount)
        val movieList = Collections.synchronizedList(mutableListOf<Movie>())

        movieViewModel.movieIds.forEach { movieId ->
            executor.execute {
                val movie = movieViewModel.requestMovie(movieId)
                movieList.add(movie)
            }
        }
        executor.shutdown()
        try {
            executor.awaitTermination(5, TimeUnit.SECONDS)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        movieViewModel.updateMoviesLiveData(movieList.sortedByDescending { movie ->
            movie.rating
        })
        movieAdapter?.submitList(movieList) {
            binding.moviesList.scrollToPosition(0)
        }
        mainHandler.postDelayed({
            movieViewModel.getShowToast()
        }, THREAD_DELAY)
    }

    private fun requestMoviesWithHandler() {
        val backgroundThread = HandlerThread("Background thread").apply {
            start()
        }
        backgroundHandler = Handler(backgroundThread.looper)
        backgroundHandler.post {
            val newMoviesList = movieViewModel.requestMoviesList()
            mainHandler.post {
                movieViewModel.updateMoviesLiveData(newMoviesList)
                movieAdapter?.submitList(newMoviesList) {
                    binding.moviesList.scrollToPosition(0)
                }
            }
            mainHandler.postDelayed({
                movieViewModel.getShowToast()
            }, THREAD_DELAY)
        }
    }

    private fun initMoviesList() {
        movieAdapter = MovieAdapter()
        with(binding.moviesList) {
            adapter = movieAdapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }
    }

    private fun observeViewModelState() {
        movieViewModel.movies
            .observe(viewLifecycleOwner) { newMovieList ->
                movieAdapter?.submitList(newMovieList) {
                    binding.moviesList.scrollToPosition(0)
                }
            }
        movieViewModel.showToast
            .observe(viewLifecycleOwner) {
                showToast(R.string.text_list_updated)
            }
    }

    companion object {
        const val THREAD_DELAY = 1000L
    }
}