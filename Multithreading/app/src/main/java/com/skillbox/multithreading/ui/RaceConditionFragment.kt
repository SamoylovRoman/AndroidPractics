package com.skillbox.multithreading.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.skillbox.multithreading.R
import com.skillbox.multithreading.databinding.FragmentRaceConditionBinding
import com.skillbox.multithreading.extension.showToast

class RaceConditionFragment : Fragment() {
    private var _binding: FragmentRaceConditionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRaceConditionBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    @SuppressLint("SetTextI18n")
    private fun initListeners() {
        with(binding) {
            startWithoutSyncButton.setOnClickListener {
                if (checkInputValues()) {
                    setCalculatedWOSValues()
                }
            }
            startWithSyncButton.setOnClickListener {
                if (checkInputValues()) {
                    setCalculatedWSValues()
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setCalculatedWOSValues() {
        with(binding) {
            val startTime = System.currentTimeMillis()
            val threads = threadsCount.text.toString().toLong()
            val iterations = incrementIterationCount.text.toString().toLong()
            expectedValueWOSync.text =
                "${getString(R.string.text_expected_value)} ${START_VALUE + threads * iterations}"
            realValueWOSync.text = "${getString(R.string.text_real_value)} ${
                incrementWithoutSync(
                    threads,
                    iterations
                )
            }"
            calculationTimeWOSync.text =
                "${getString(R.string.text_calculation_time)} ${System.currentTimeMillis() - startTime} мс"
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setCalculatedWSValues() {
        with(binding) {
            val startTime = System.currentTimeMillis()
            val threads = threadsCount.text.toString().toLong()
            val iterations = incrementIterationCount.text.toString().toLong()
            expectedValueWSync.text =
                "${getString(R.string.text_expected_value)} ${START_VALUE + threads * iterations}"
            realValueWSync.text = "${getString(R.string.text_real_value)} ${
                incrementWithSync(
                    threads,
                    iterations
                )
            }"
            calculationTimeWSync.text =
                "${getString(R.string.text_calculation_time)} ${System.currentTimeMillis() - startTime} мс"
        }
    }

    private fun checkInputValues(): Boolean {
        if (binding.threadsCount.text.isNotBlank()
            && binding.incrementIterationCount.text.isNotBlank()
        ) {
            if (binding.threadsCount.text.toString().toInt() > 0
                && binding.incrementIterationCount.text.toString().toInt() > 0
            ) {
                return true
            }
        }
        showToast(R.string.text_no_values)
        return false
    }

    private fun incrementWithoutSync(threads: Long, iterations: Long): Long {
        var value = START_VALUE
        (0 until threads).map {
            Thread {
                for (i in 0 until iterations) {
                    value++
                }
            }.apply {
                start()
            }
        }.map { thread ->
            thread.join()
        }
        return value
    }

    private fun incrementWithSync(threads: Long, iterations: Long): Long {
        var value = START_VALUE
        (0 until threads).map {
            Thread {
                synchronized(this) {
                    for (i in 0 until iterations) {
                        value++
                    }
                }
            }.apply {
                start()
            }
        }.map { thread ->
            thread.join()
        }
        return value
    }

    companion object {
        private const val START_VALUE = 1L
    }
}