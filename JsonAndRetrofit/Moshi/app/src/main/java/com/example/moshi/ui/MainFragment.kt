package com.example.moshi.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.moshi.R
import com.example.moshi.data.RemoteMovie
import com.example.moshi.data.MovieRating
import com.example.moshi.data.Score
import com.example.moshi.databinding.FragmentMainBinding
import com.example.moshi.extensions.toast
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.lang.Exception

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val movieViewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        observeViewModelState()
    }

    private fun initListeners() {
        binding.searchButton.setOnClickListener {
            if (binding.movieNameInputText.text.isNotBlank()) {
                searchMovie()
            } else toast(R.string.text_at_least_name)
        }
    }

    private fun searchMovie() {
        movieViewModel.search(binding.movieNameInputText.text.toString())
    }


    private fun observeViewModelState() {
        movieViewModel.movie.observe(viewLifecycleOwner) { movie ->
            if (movie != null) {
                setMovieInfo(movie)
            }
        }
        movieViewModel.error.observe(viewLifecycleOwner) { text ->
            setErrorInfo(text)

        }
    }

    private fun setErrorInfo(text: String?) {
        with(binding) {
            notSearchedText.text = text
            notSearchedText.isVisible = true
            movieNameText.isVisible = false
            movieYearText.isVisible = false
            movieGenreText.isVisible = false
            posterImage.isVisible = false
        }
    }

    private fun setMovieInfo(movie: RemoteMovie) {
        with(binding) {
            notSearchedText.isVisible = false
            movieNameText.text = movie.title
            movieNameText.isVisible = true
            movieYearText.text = movie.year.toString()
            movieYearText.isVisible = true
            movieGenreText.text = movie.genre
            movieGenreText.isVisible = true
            Glide.with(root)
                .load(movie.posterUrl)
                .placeholder(R.drawable.ic_download)
                .error(R.drawable.ic_error)
                .into(posterImage)
            posterImage.isVisible = true
        }
    }
}

/*    private val simpleMovie = """
        {
            "Title":"xXx",
            "Year":"2002"
            ,"imdbID":"tt0295701"
        }
        """.trimIndent()

    private val movieList = """
        [
            {
                "Title":"xXx",
                "Year":"2002",
                "rating":"PG",
                "imdbID":"tt0295701"
            },
            {
                "Title":"xXx 2",
                "Year":"2004",
                "rating":"G",
                "imdbID":"tt0295702"
            }
        ]
        """.trimIndent()

    private val movieListWithRating = """
        [
            {
                "Title":"xXx",
                "Year":"2002",
                "rating":"PG",
                "imdbID":"tt0295701",
                "scores": [
                {"source":"Internet Movie DataBase","value":"8.6/10"},
                {"source":"Rotten tomatoes","value":"92%"},
                {"source":"Metacritic","value":"90/100"}
                ]
            },
            {
                "Title":"xXx 2",
                "Year":"2004",
                "rating":"G",
                "imdbID":"tt0295702",
                "scores": [
                    {"source":"Internet Movie DataBase","value":"8.2/10"},
                    {"source":"Rotten tomatoes","value":"90%"},
                    {"source":"Metacritic","value":"91/100"}
                ]
            }
        ]
        """.trimIndent()



    override fun onResume() {
        super.onResume()
//        convertSimpleMovieJsonToInstance()
//        convertMovieListJsonToInstance()
//        convertMovieWithScoreJsonToInstance()
        convertMovieWithScoreInstanceToJson()
    }

    private fun convertSimpleMovieJsonToInstance() {
        val moshi = Moshi.Builder().build()
        val adapter = moshi.adapter(RemoteMovie::class.java).nonNull()
        try {
            val movie = adapter.fromJson(simpleMovie)
            binding.mainText.text = movie.toString()
        } catch (e: Exception) {
            binding.mainText.text = "Parse error = ${e.message}"
        }
    }

    private fun convertMovieListJsonToInstance() {
        val moshi = Moshi.Builder().build()
        val movieListType = Types.newParameterizedType(
            List::class.java,
            RemoteMovie::class.java
        )
        val adapter = moshi.adapter<List<RemoteMovie>>(movieListType).nonNull()
        try {
            val movies = adapter.fromJson(movieList)
            binding.mainText.text = movies.toString()
        } catch (e: Exception) {
            binding.mainText.text = "Parse error = ${e.message}"
        }
    }

    private fun convertMovieWithScoreJsonToInstance() {
        val moshi = Moshi.Builder().build()
        val movieListType = Types.newParameterizedType(
            List::class.java,
            RemoteMovie::class.java
        )
        val adapter = moshi.adapter<List<RemoteMovie>>(movieListType).nonNull()
        try {
            val movies = adapter.fromJson(movieListWithRating)
            binding.mainText.text = movies.toString()
        } catch (e: Exception) {
            binding.mainText.text = "Parse error = ${e.message}"
        }
    }

    private fun convertMovieWithScoreInstanceToJson() {
        val moviesToSerialize = listOf(
            RemoteMovie(
                id = "tt0295701",
                title = "xXx",
                year = 2002,
                rating = MovieRating.PG,
                scores = listOf(
                    Score(
                        source = "Internet Movie DataBase", value = "8.6 / 10"
                    ),
                    Score(
                        source = "Rotten tomatoes", value = "92%"
                    )
                )
            ),
            RemoteMovie(
                id = "tt0295701",
                title = "xXx 2",
                year = 2002,
                rating = MovieRating.PG,
                scores = listOf(
                    Score(
                        source = "Internet Movie DataBase", value = "8.6 / 10"
                    ),
                    Score(
                        source = "Rotten tomatoes", value = "92%"
                    )
                )
            )
        )
        val moshi = Moshi.Builder().build()
        val movieListType = Types.newParameterizedType(
            List::class.java,
            RemoteMovie::class.java
        )
        val adapter = moshi.adapter<List<RemoteMovie>>(movieListType).nonNull()
        try {
            val movies = adapter.toJson(moviesToSerialize)
            binding.mainText.text = movies.toString()
        } catch (e: Exception) {
            binding.mainText.text = "Parse error = ${e.message}"
        }

    }*/