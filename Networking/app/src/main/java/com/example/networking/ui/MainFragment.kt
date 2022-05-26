package com.example.networking.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.networking.R
import com.example.networking.databinding.FragmentMainBinding
import com.example.networking.extension.toast

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private var movieAdapter: RemoteMovieAdapter? = null
    private var loadedPages: Int = 0
    private var endOfList: Boolean = false

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
            movieAdapter?.submitList(moviesList)
            updateListState()
        }
        movieViewModel.isLoading.observe(viewLifecycleOwner, ::updateLoadingState)
        movieViewModel.isAddingLoading.observe(viewLifecycleOwner, ::updateAddingLoadState)
        movieViewModel.emptyList.observe(viewLifecycleOwner) {
            binding.emptyListText.visibility = View.VISIBLE
        }
        movieViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            updateErrorState(errorMessage)
        }
    }

    private fun updateListState() {
        with(binding) {
            emptyListText.visibility = View.GONE
            errorText.visibility = View.GONE
            tryAgainButton.visibility = View.GONE
        }
    }

    private fun updateErrorState(errorString: String) {
        with(binding) {
            moviesList.visibility = View.GONE
            emptyListText.visibility = View.GONE
            errorText.text = errorString
            errorText.visibility = View.VISIBLE
            tryAgainButton.visibility = View.VISIBLE
        }
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

    private fun updateAddingLoadState(isLoading: Boolean) {
        binding.linerProgressBar.isVisible = isLoading
    }

    private fun initMoviesList() {
        movieAdapter = RemoteMovieAdapter()
        with(binding.moviesList) {
            adapter = movieAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val visibleItemCount = (layoutManager as LinearLayoutManager).childCount
                    val totalItemCount = (layoutManager as LinearLayoutManager).itemCount
                    val firstVisibleItemPosition =
                        (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                        if (movieViewModel.movies.value?.size != movieViewModel.moviesCount.value) {
                            searchMovie()
                        } else {
                            if (!endOfList) {
                                endOfList = true
                                toast(R.string.text_no_more)
                            }
                        }
                    }
                }
            })
            setHasFixedSize(true)
        }
    }

    private fun initListeners() {
        binding.searchButton.setOnClickListener {
            if (checkInputData()) {
                loadedPages = 0
                endOfList = false
                searchMovie()
            } else {
                toast(R.string.text_at_least_year)
            }
        }
        binding.tryAgainButton.setOnClickListener {
            if (checkInputData()) {
                loadedPages = 0
                endOfList = false
                searchMovie()
                binding.tryAgainButton.visibility = View.GONE
                binding.errorText.visibility = View.GONE
            } else {
                toast(R.string.text_at_least_year)
            }
        }
        binding.floatUpButton.setOnClickListener {
            binding.moviesList.smoothScrollToPosition(0)
        }
    }

    private fun searchMovie() {
        movieViewModel.search(
            binding.movieNameText.text.toString(),
            binding.movieYearText.text.toString(),
            if (binding.movieTypeList.editText?.text.toString() != getString(R.string.text_not_selected)) {
                binding.movieTypeList.editText?.text.toString()
            } else "",
            (++loadedPages).toString()
        )
    }

    private fun initDropDownMenu() {
        val items = resources.getStringArray(R.array.movieTypes)
        val adapter = ArrayAdapter(requireContext(), R.layout.item_type_list, items)
        (binding.textField as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    private fun checkInputData(): Boolean {
        if (binding.movieNameText.text.isNotBlank()) {
            return true
        }
        return false
    }
}