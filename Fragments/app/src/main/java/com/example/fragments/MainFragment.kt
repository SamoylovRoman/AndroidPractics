package com.example.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import com.example.fragments.databinding.FragmentMainBinding

class MainFragment : Fragment(), ItemSelectListener {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater)
        if (savedInstanceState == null)
            showListFragment()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showListFragment() {
        val listFragment = ListFragment.newInstance()
        childFragmentManager.commit {
            addToBackStack(ListFragment::class.java.simpleName)
            replace(R.id.mainFragmentContainer, listFragment)
        }
    }

    private fun showDetailFragment(imageLink: String, fullName: String, descriptionText: String) {
        val detailFragment = DetailFragment.newInstance(imageLink, fullName, descriptionText)
        childFragmentManager.commit {
            replace(R.id.mainFragmentContainer, detailFragment)
            addToBackStack(DetailFragment::class.java.simpleName)
        }
    }

    override fun onItemSelected(imageLink: String, fullName: String, descriptionText: String) {
        Log.d("onItemSelect: ", "$imageLink + $fullName + $descriptionText")
        showDetailFragment(imageLink, fullName, descriptionText)
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}