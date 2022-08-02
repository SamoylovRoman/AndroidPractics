package com.android.practice.files.presentation.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.android.practice.files.R
import com.android.practice.files.databinding.FragmentDownloadBinding
import com.android.practice.files.presentation.extensions.showToast
import com.android.practice.files.presentation.view_models.DownloadViewModel

class DownloadFragment : Fragment() {

    companion object {
        fun newInstance() = DownloadFragment()
    }

    private lateinit var binding: FragmentDownloadBinding
    private val viewModel: DownloadViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDownloadBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        initViewModelObservers()
        downloadStartFiles()
    }

    private fun downloadStartFiles() {
        viewModel.downloadStartFiles()
    }

    private fun initViewModelObservers() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            toggleProgress(isLoading)
        }
        viewModel.showToast.observe(viewLifecycleOwner) {
            showToast(R.string.file_is_download)
        }
    }

    private fun initListeners() {
        binding.saveButton.setOnClickListener {
            viewModel.loadFile(binding.inputText.text.toString())
        }
    }

    private fun toggleProgress(isVisible: Boolean) {
        with(binding) {
            progressBar.isVisible = isVisible
            inputText.isVisible = !isVisible
            saveButton.isVisible = !isVisible
        }
    }
}