package com.example.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toDrawable
import com.example.fragments.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private var paramImageLink: String? = null
    private var paramFullName: String? = null
    private var paramDescriptionName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            paramImageLink = it.getString(MainFragment.ARG_IMAGE_LINK)
            paramFullName = it.getString(MainFragment.ARG_FULL_NAME)
            paramDescriptionName = it.getString(MainFragment.ARG_DESCRIPTION_TEXT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.detailImage.setImageDrawable(R.drawable.png_log_in.toDrawable())
        binding.detailFullName.text = paramFullName
        binding.detailDescription.text = paramDescriptionName
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}