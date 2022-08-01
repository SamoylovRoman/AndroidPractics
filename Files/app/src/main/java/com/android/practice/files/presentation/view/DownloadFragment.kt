package com.android.practice.files.presentation.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.practice.files.databinding.FragmentDownloadBinding
import com.android.practice.files.presentation.view_models.DownloadViewModel

class DownloadFragment : Fragment() {

    companion object {
        fun newInstance() = DownloadFragment()
    }

    private lateinit var binding: FragmentDownloadBinding
    private lateinit var viewModel: DownloadViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDownloadBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DownloadViewModel::class.java)
        initListeners()
    }

    private fun initListeners() {
        binding.saveButton.setOnClickListener {

        }
    }

}