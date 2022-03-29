package com.example.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.fragments.databinding.FragmentMainBinding

class MainFragment : Fragment(), ItemSelectListener {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater)
        showListFragment()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parentFragmentManager.fragments.forEach {
            Log.d("MainFragment parentFragmentManager onViewCreated", "$it")
        }
        childFragmentManager.fragments.forEach {
            Log.d("MainFragment childFragmentManager onViewCreated", "$it")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showListFragment() {
        childFragmentManager.commit {
            replace<ListFragment>(R.id.mainFragmentContainer)
            addToBackStack(ListFragment::class.java.simpleName)
        }
    }

    private fun showDetailFragment(imageLink: String, fullName: String, descriptionText: String) {
        val bundle = Bundle().apply {
            putString(ARG_IMAGE_LINK, imageLink)
            putString(ARG_FULL_NAME, fullName)
            putString(ARG_DESCRIPTION_TEXT, descriptionText)
        }
        childFragmentManager.commit {
            replace<DetailFragment>(containerViewId = R.id.mainFragmentContainer, args = bundle)
            addToBackStack(DetailFragment::class.java.simpleName)
        }
    }

    override fun onItemSelected(imageLink: String, fullName: String, descriptionText: String) {
        Log.d("onItemSelect: ", "$imageLink + $fullName + $descriptionText")
        showDetailFragment(imageLink, fullName, descriptionText)
    }

    companion object {
        const val ARG_IMAGE_LINK = "imageLink"
        const val ARG_FULL_NAME = "fullName"
        const val ARG_DESCRIPTION_TEXT = "descriptionText"
    }
}