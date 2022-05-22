package com.skillbox.multithreading.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.skillbox.multithreading.R
import com.skillbox.multithreading.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.threading.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_threadingFragment)
        }
        binding.deadlock.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_deadlockFragment)
        }
        binding.raceCondition.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_raceConditionFragment)
        }
        binding.livelock.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_livelockFragment)
        }
    }
}