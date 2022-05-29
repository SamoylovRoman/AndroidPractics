package com.example.moshi.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.moshi.R
import com.example.moshi.databinding.FragmentAddRatingBinding
import com.example.moshi.extensions.toast

class AddRatingFragment : Fragment() {

    private var _binding: FragmentAddRatingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddRatingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    private fun initListeners() {
        binding.addNewRatingButton.setOnClickListener {
            if (checkInputValues()) {
                val resourceName = binding.addResourceName.text.toString()
                val resourceValue = binding.addResourceValue.text.toString()
                parentFragmentManager.setFragmentResult(
                    REQUEST_CODE,
                    bundleOf(RESOURCE_NAME to resourceName, RESOURCE_VALUE to resourceValue)
                )
                findNavController().popBackStack()
            } else {
                toast(R.string.text_enter_values)
            }
        }
        binding.skipNewRatingButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun checkInputValues(): Boolean {
        return binding.addResourceName.text.isNotBlank() &&
                binding.addResourceValue.text.isNotBlank()
    }

    companion object {
        const val REQUEST_CODE = "Add rating fragment code"
        const val RESOURCE_NAME = "Resource name"
        const val RESOURCE_VALUE = "Resource value"
    }
}