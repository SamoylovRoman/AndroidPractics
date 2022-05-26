package com.example.networking.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.view.isEmpty
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.networking.R
import com.example.networking.adapters.MovieAdapter
import com.example.networking.adapters.RemoteMovieAdapter
import com.example.networking.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    //    private var movieAdapter: MovieAdapter? = null
    private var movieAdapter: RemoteMovieAdapter? = null

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
        initDropDownMenu()
        initListeners()
        initMoviesList()
        observeViewModelState()
    }

    private fun observeViewModelState() {
        movieViewModel.movies.observe(viewLifecycleOwner) { moviesList ->
            movieAdapter?.submitList(moviesList) {
                binding.moviesList.scrollToPosition(0)
            }
            binding.emptyListText.visibility = View.GONE
            binding.errorText.visibility = View.GONE
            binding.tryAgainButton.visibility = View.GONE
        }
        movieViewModel.isLoading.observe(viewLifecycleOwner, ::updateLoadingState)
        movieViewModel.emptyList.observe(viewLifecycleOwner) {
            binding.emptyListText.visibility = View.VISIBLE
        }
        movieViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            updateErrorState(errorMessage)
        }
    }

    private fun updateEmptyTextState() {
/*        if (binding.moviesList.isEmpty()) {
            binding.emptyListText.visibility = View.VISIBLE
        } else {
            binding.emptyListText.visibility = View.GONE
        }*/
    }

    private fun updateErrorState(errorString: String) {
        binding.moviesList.visibility = View.GONE
        binding.emptyListText.visibility = View.GONE
        binding.errorText.text = errorString
        binding.errorText.visibility = View.VISIBLE
        binding.tryAgainButton.visibility = View.VISIBLE
    }

    private fun updateLoadingState(isLoading: Boolean) {
        with(binding) {
            moviesList.isVisible = isLoading.not()
            progressBar.isVisible = isLoading
            searchButton.isEnabled = isLoading.not()
            movieNameText.isEnabled = isLoading.not()
            movieYearText.isEnabled = isLoading.not()
            movieTypeList.isEnabled = isLoading.not()
        }
    }

    private fun initMoviesList() {
//        movieAdapter = MovieAdapter()
        movieAdapter = RemoteMovieAdapter()
        with(binding.moviesList) {
            adapter = movieAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun initListeners() {
        binding.searchButton.setOnClickListener {
            searchMovie()
        }
        binding.tryAgainButton.setOnClickListener {
            searchMovie()
            binding.tryAgainButton.visibility = View.GONE
            binding.errorText.visibility = View.GONE
        }
    }

    private fun searchMovie() {
        movieViewModel.search(
            binding.movieNameText.text.toString(),
            binding.movieYearText.text.toString(),
            if (binding.movieTypeList.editText?.text.toString() != getString(R.string.text_not_selected)) {
                binding.movieTypeList.editText?.text.toString()
            } else ""
        )
    }

    private fun initDropDownMenu() {
        val items = resources.getStringArray(R.array.movieTypes)
        val adapter = ArrayAdapter(requireContext(), R.layout.item_type_list, items)
        (binding.textField as? AutoCompleteTextView)?.setAdapter(adapter)
    }

}