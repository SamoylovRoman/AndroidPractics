package com.skillbox.multithreading.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.skillbox.multithreading.R
import com.skillbox.multithreading.databinding.FragmentDeadlockBinding
import com.skillbox.multithreading.extension.showToast

class DeadlockFragment : Fragment() {
    private var _binding: FragmentDeadlockBinding? = null
    private val binding get() = _binding!!

    private val mainHandler = Handler(Looper.getMainLooper())

    private val lock1 = Any()
    private val lock2 = Any()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeadlockBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    private fun initListeners() {
        with(binding) {
            startDeadLockButton.setOnClickListener {
                if (checkInputValues()) {
                    startIncrementWithDeadLock()
                }
            }
            startWithoutDeadLockButton.setOnClickListener {
                if (checkInputValues()) {
                    startIncrementWithoutDeadLock()
                }
            }
        }
    }

    private fun checkInputValues(): Boolean {
        if (binding.threadsDeadLockCount.text.isNotBlank()
            && binding.incrementDeadLockIterationCount.text.isNotBlank()
        ) {
            if (binding.threadsDeadLockCount.text.toString().toInt() > 0
                && binding.incrementDeadLockIterationCount.text.toString().toInt() > 0
            ) {
                return true
            }
        }
        showToast(R.string.text_no_values)
        return false
    }

    @SuppressLint("SetTextI18n")
    private fun startIncrementWithoutDeadLock() {
        val iterations = binding.incrementDeadLockIterationCount.text.toString().toLong()
        val startTime = System.currentTimeMillis()
        Thread {
            val value = simulateWithoutDeadLock(iterations)
            mainHandler.post {
                with(binding) {
                    expectedValueWODeadLock.text =
                        "${getString(R.string.text_expected_value)} ${START_VALUE + THREADS_COUNT * iterations}"
                    realValueWODeadLock.text = "${getString(R.string.text_real_value)} $value"
                    calculationTimeWODeadLock.text =
                        "${getString(R.string.text_calculation_time)} ${System.currentTimeMillis() - startTime} мс"
                }
            }
        }.start()
    }

    @SuppressLint("SetTextI18n")
    private fun startIncrementWithDeadLock() {
        val iterations = binding.incrementDeadLockIterationCount.text.toString().toLong()
        val startTime = System.currentTimeMillis()
        val value = simulateDeadLock(iterations)
        binding.expectedValueDeadLock.text =
            "${getString(R.string.text_expected_value)} ${START_VALUE + THREADS_COUNT * iterations}"
        binding.realValueDeadLock.text = "${getString(R.string.text_real_value)} $value"
        binding.calculationTimeDeadLock.text =
            "${getString(R.string.text_calculation_time)} ${System.currentTimeMillis() - startTime} мс"
    }

    private fun simulateWithoutDeadLock(iterations: Long): Long {
        var value = START_VALUE
        val thread1 = Thread {
            Log.d("No Deadlock", "Start1")

            repeat((0 until iterations).count()) {
                synchronized(lock1) {
                    value++
                }
            }
            Log.d("No Deadlock", "End1")
        }
        val thread2 = Thread {
            Log.d("No Deadlock", "Start2")
            repeat((0 until iterations).count()) {
                synchronized(lock1) {
                    value++
                }
            }
            Log.d("No Deadlock", "End2")
        }
        thread1.start()
        thread2.start()
        thread1.join()
        thread2.join()
        return value
    }

    private fun simulateDeadLock(iterations: Long): Long {
        var value = START_VALUE
        listOf(Thread {
            Log.d("Deadlock", "Start1")
            repeat((0 until iterations).count()) {
                synchronized(lock1) {
                    synchronized(lock2) {
                        value++
                    }
                }
            }
            Log.d("Deadlock", "End1")
        },
            Thread {
                Log.d("Deadlock", "Start2")
                repeat((0 until iterations).count()) {
                    synchronized(lock2) {
                        synchronized(lock1) {
                            value++
                        }
                    }
                }
                Log.d("Deadlock", "End2")
            }).forEach { thread ->
            thread.start()
        }
        return value
    }

    companion object {
        private const val START_VALUE = 1L
        private const val THREADS_COUNT = 2L
    }
}