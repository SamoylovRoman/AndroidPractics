package com.skillbox.github.ui.repository_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.skillbox.github.data.RemoteRepo
import com.skillbox.github.databinding.FragmentRepositoryListBinding

class RepositoryListFragment : Fragment() {
    private var _binding: FragmentRepositoryListBinding? = null
    private val binding get() = _binding!!

    private val repoViewModel: RepositoryListViewModel by viewModels()

    private var repoAdapter: RepoAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRepositoryListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRepoList()
        bindViewModel()
        searchRepositories()
    }

    private fun initRepoList() {
        repoAdapter = RepoAdapter { repo ->
            findNavController().navigate(
                RepositoryListFragmentDirections.actionRepositoryListFragmentToDetailRepoFragment(
                    repo
                )
            )
        }
        with(binding.repositoriesList) {
            adapter = repoAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }

    private fun bindViewModel() {
        repoViewModel.isLoading.observe(viewLifecycleOwner, ::updateIsLoading)
        repoViewModel.errorString.observe(viewLifecycleOwner) { errorString ->
            binding.listErrorText.text = errorString
            binding.listErrorText.isVisible = true
        }
        repoViewModel.repositoriesList.observe(viewLifecycleOwner) { newList ->
            updateRepoList(newList)
        }
    }

    private fun searchRepositories() {
        repoViewModel.searchRepositories()
    }

    private fun updateRepoList(newList: List<RemoteRepo>?) {
        repoAdapter?.submitList(newList) {
            binding.repositoriesList.smoothScrollToPosition(0)
        }
    }

    private fun updateIsLoading(isLoading: Boolean) {
        binding.listProgressBar.isVisible = isLoading
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        repoAdapter = null
    }
}