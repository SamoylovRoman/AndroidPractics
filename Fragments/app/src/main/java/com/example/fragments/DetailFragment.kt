package com.example.fragments

import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fragments.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!


    private var paramImageBitmap: Bitmap? = null
    private var paramFullName: String? = null
    private var paramDescriptionName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            paramImageBitmap = it.getParcelable(ARG_IMAGE_BITMAP)
            paramFullName = it.getString(ARG_FULL_NAME)
            paramDescriptionName = it.getString(ARG_DESCRIPTION_TEXT)
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
        binding.detailImage.setImageBitmap(paramImageBitmap)
        binding.detailFullName.text = paramFullName
        binding.detailDescription.text = paramDescriptionName
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val ARG_IMAGE_BITMAP = "imageLink"
        const val ARG_FULL_NAME = "fullName"
        const val ARG_DESCRIPTION_TEXT = "descriptionText"

        fun newInstance(paramImageBitmap: Bitmap, paramFullName: String, paramDescription: String) =
            DetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_IMAGE_BITMAP, paramImageBitmap)
                    putString(ARG_FULL_NAME, paramFullName)
                    putString(ARG_DESCRIPTION_TEXT, paramDescription)
                }
            }
    }
}