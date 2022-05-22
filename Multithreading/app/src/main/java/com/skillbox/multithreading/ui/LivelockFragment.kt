package com.skillbox.multithreading.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.skillbox.multithreading.R
import com.skillbox.multithreading.databinding.FragmentLivelockBinding
import com.skillbox.multithreading.extension.showToast
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock

class LivelockFragment : Fragment() {
    private var _binding: FragmentLivelockBinding? = null
    private val binding get() = _binding!!

    private val mainHandler = Handler(Looper.getMainLooper())
    private val lock1 = ReentrantLock(true)
    private val lock2 = ReentrantLock(true)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLivelockBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    private fun initListeners() {
        with(binding) {
            startLiveLockButton.setOnClickListener {
                if (checkInputValues()) {
                    startIncrementWithLiveLock()
                }
            }
            startWithoutLiveLockButton.setOnClickListener {
                if (checkInputValues()) {
                    startIncrementWithoutLiveLock()
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun startIncrementWithoutLiveLock() {
        val iterations = binding.incrementLiveLockIterationCount.text.toString().toLong()
        val startTime = System.currentTimeMillis()
        Thread {
            val value = simulateWithoutLiveLock(iterations)
            mainHandler.post {
                with(binding) {
                    expectedValueWOLiveLock.text =
                        "${getString(R.string.text_expected_value)} ${START_VALUE + THREADS_COUNT * iterations}"
                    realValueWOLiveLock.text = "${getString(R.string.text_real_value)} $value"
                    calculationTimeWOLiveLock.text =
                        "${getString(R.string.text_calculation_time)} ${System.currentTimeMillis() - startTime} мс"
                }
            }
        }.start()
    }

    private fun simulateWithoutLiveLock(iterations: Long): Long {
        var value = START_VALUE
        val thread1 = Thread {
            while (true) {
                lock1.tryLock(WAIT_LOCK_TIME, TimeUnit.MILLISECONDS)
                Log.d("simulateLiveLock", "lock1 acquired, trying to acquire lock2...")
                Thread.sleep(WAIT_LOCK_TIME)
                if (lock2.tryLock(WAIT_LOCK_TIME, TimeUnit.MILLISECONDS)) {
                    Log.d("simulateLiveLock", "lock2 acquired")
                } else {
                    Log.d("simulateLiveLock", "cannot acquire lock2, releasing lock1...")
                    lock1.unlock()
                    continue
                }
                Log.d("simulateLiveLock", "executing first operation...")
                (0 until iterations).forEach { _ ->
                    synchronized(lock1) {
                        synchronized(lock2) {
                            value++
                        }
                    }
                }
                break
            }
            lock2.unlock()
            lock1.unlock()
        }
        val thread2 = Thread {
            while (true) {
                lock2.tryLock(2 * WAIT_LOCK_TIME, TimeUnit.MILLISECONDS)
                Log.d("simulateLiveLock", "lock2 acquired, trying to acquire lock1...")
                Thread.sleep(WAIT_LOCK_TIME)
                if (lock1.tryLock(WAIT_LOCK_TIME, TimeUnit.MILLISECONDS)) {
                    Log.d("simulateLiveLock", "lock1 acquired")
                } else {
                    Log.d("simulateLiveLock", "cannot acquire lock1, releasing lock2...")
                    lock2.unlock()
                    continue
                }
                Log.d("simulateLiveLock", "executing second operation...")
                (0 until iterations).forEach { _ ->
                    synchronized(lock2) {
                        synchronized(lock1) {
                            value++
                        }
                    }
                }
                break
            }
            lock1.unlock()
            lock2.unlock()
        }
        thread1.start()
        thread2.start()
        thread1.join()
        thread2.join()
        return value
    }

    @SuppressLint("SetTextI18n")
    private fun startIncrementWithLiveLock() {
        val iterations = binding.incrementLiveLockIterationCount.text.toString().toLong()
        val startTime = System.currentTimeMillis()
        val value = simulateLiveLock()
        binding.expectedValueLiveLock.text =
            "${getString(R.string.text_expected_value)} ${START_VALUE + THREADS_COUNT * iterations}"
        binding.realValueLiveLock.text = "${getString(R.string.text_real_value)} $value"
        binding.calculationTimeLiveLock.text =
            "${getString(R.string.text_calculation_time)} ${System.currentTimeMillis() - startTime} мс"

    }

    private fun simulateLiveLock() {
        val thread1 = Thread {
            while (true) {
                lock1.tryLock(WAIT_LOCK_TIME, TimeUnit.MILLISECONDS)
                Log.d("simulateLiveLock", "lock1 acquired, trying to acquire lock2...")
                Thread.sleep(WAIT_LOCK_TIME)
                if (lock2.tryLock()) {
                    Log.d("simulateLiveLock", "lock2 acquired")
                } else {
                    Log.d("simulateLiveLock", "cannot acquire lock2, releasing lock1...")
                    lock1.unlock()
                    continue
                }
                Log.d("simulateLiveLock", "executing first operation...")
                break
            }
            lock2.unlock()
            lock1.unlock()
        }
        val thread2 = Thread {
            while (true) {
                lock2.tryLock(WAIT_LOCK_TIME, TimeUnit.MILLISECONDS)
                Log.d("simulateLiveLock", "lock2 acquired, trying to acquire lock1...")
                Thread.sleep(WAIT_LOCK_TIME)
                if (lock1.tryLock()) {
                    Log.d("simulateLiveLock", "lock1 acquired")
                } else {
                    Log.d("simulateLiveLock", "cannot acquire lock1, releasing lock2...")
                    lock2.unlock()
                    continue
                }
                Log.d("simulateLiveLock", "executing second operation...")
                break
            }
            lock1.unlock()
            lock2.unlock()
        }
        thread1.start()
        thread2.start()
        thread1.join()
        thread2.join()
    }

    private fun checkInputValues(): Boolean {
        if (binding.threadsLiveLockCount.text.isNotBlank()
            && binding.incrementLiveLockIterationCount.text.isNotBlank()
        ) {
            if (binding.threadsLiveLockCount.text.toString().toInt() > 0
                && binding.incrementLiveLockIterationCount.text.toString().toInt() > 0
            ) {
                return true
            }
        }
        showToast(R.string.text_no_values)
        return false
    }

    companion object {
        private const val START_VALUE = 1L
        private const val THREADS_COUNT = 2L
        private const val WAIT_LOCK_TIME = 50L
    }
}