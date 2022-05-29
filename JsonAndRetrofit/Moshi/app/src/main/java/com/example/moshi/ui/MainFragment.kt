package com.example.moshi.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.moshi.R
import com.example.moshi.data.RemoteMovie
import com.example.moshi.data.Score
import com.example.moshi.databinding.FragmentMainBinding
import com.example.moshi.extensions.toast

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val movieViewModel: MainViewModel by viewModels()

    private var scoreAdapter: ScoreAdapter? = null

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
        initScoreList()
        observeViewModelState()
    }

    private fun initScoreList() {
        scoreAdapter = ScoreAdapter()
        with(binding.ratingsList) {
            adapter = scoreAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun initListeners() {
        binding.searchButton.setOnClickListener {
            if (binding.movieNameInputText.text.isNotBlank()) {
                searchMovie()
            } else toast(R.string.text_at_least_name)
        }
        binding.addRatingButton.setOnClickListener {
            showAddRatingDialog()
        }
        parentFragmentManager.setFragmentResultListener(
            AddRatingFragment.REQUEST_CODE,
            viewLifecycleOwner
        ) { _, data ->
            val resourceName = data.getString(AddRatingFragment.RESOURCE_NAME)!!
            val resourceValue = data.getString(AddRatingFragment.RESOURCE_VALUE)!!
            movieViewModel.addScore(Pair(resourceName, resourceValue))
            movieViewModel.printJsonObjectInLog()
        }
    }

    private fun showAddRatingDialog() {
        findNavController().navigate(R.id.action_mainFragment_to_addRatingFragment)
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
            addRatingButton.isVisible = false
            movieRatingsText.isVisible = false
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
            addRatingButton.isVisible = true
            movieRatingsText.isVisible = true
            scoreAdapter?.submitList(movie.ratings.map { entry ->
                Score(entry.key, entry.value)
            })
        }
    }
}