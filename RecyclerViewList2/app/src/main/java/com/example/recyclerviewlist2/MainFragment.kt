package com.example.recyclerviewlist2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import com.example.recyclerviewlist2.databinding.FragmentMainBinding

class MainFragment : Fragment() {
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
        initListeners()
    }

    private fun initListeners() {
        binding.linearLayoutButton.setOnClickListener {
            val listFragment = StaffListFragment.newInstance(StaffListFragment.LINEAR_LAYOUT_TO_SHOW)
            parentFragmentManager.commit {
                addToBackStack(StaffListFragment::class.java.simpleName)
                replace(R.id.fragmentContainer, listFragment)
            }
        }

        binding.gridLayoutButton.setOnClickListener {
            val listFragment = StaffListFragment.newInstance(StaffListFragment.GRID_LAYOUT_TO_SHOW)
            parentFragmentManager.commit {
                addToBackStack(StaffListFragment::class.java.simpleName)
                replace(R.id.fragmentContainer, listFragment)
            }
        }

        binding.staggeredLayoutButton.setOnClickListener {
            val listFragment = StaffListFragment.newInstance(StaffListFragment.STAGGERED_GRID_LAYOUT_TO_SHOW)
            parentFragmentManager.commit {
                addToBackStack(StaffListFragment::class.java.simpleName)
                replace(R.id.fragmentContainer, listFragment)
            }
        }
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}