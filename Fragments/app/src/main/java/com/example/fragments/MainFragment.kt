package com.example.fragments

import android.graphics.Bitmap
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            if (!isSmallestWidth600DP()) {
                showListFragment()
            }
        }
    }

    private fun isSmallestWidth600DP() =
        context?.resources?.configuration?.smallestScreenWidthDp!! >= 600

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

    private fun showDetailFragment(imageBitmap: Bitmap, fullName: String, descriptionText: String) {
        val detailFragment = DetailFragment.newInstance(imageBitmap, fullName, descriptionText)
        childFragmentManager.commit {
            setCustomAnimations(
                R.anim.slide_in,
                R.anim.slide_in_from_top,
                R.anim.slide_in_from_left,
                R.anim.fade_out
            )
            if (!isSmallestWidth600DP()) {
                addToBackStack(DetailFragment::class.java.simpleName)
            }
            replace(R.id.mainFragmentContainer, detailFragment)
        }
    }

    override fun onItemSelected(imageBitmap: Bitmap, fullName: String, descriptionText: String) {
        Log.d("onItemSelect: ", "$imageBitmap + $fullName + $descriptionText")
        showDetailFragment(imageBitmap, fullName, descriptionText)
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}