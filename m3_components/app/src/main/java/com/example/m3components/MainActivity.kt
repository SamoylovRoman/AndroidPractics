package com.example.m3components

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.m3components.databinding.ActivityMainBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private var isStarted = false
    private var timerStartValue = 10
    private var timerCurrentProgressValue = 10
    private lateinit var binding: ActivityMainBinding
    private var jobTimer: Job? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setStartValues()
        initListeners()
    }

    private fun startTimer() {
        binding.sliderTimer.isEnabled = false
        binding.buttonStart.setText(R.string.button_text_stop)
        timerCurrentProgressValue = INDICATOR_START_PROGRESS_VALUE
        jobTimer = lifecycleScope.launch {
            for (sec in timerStartValue - 1 downTo 0) {
                binding.textInside.text = sec.toString()
                timerCurrentProgressValue -= INDICATOR_START_PROGRESS_VALUE / timerStartValue
                binding.circularProgress.progress = timerCurrentProgressValue
                delay(100)
            }
            Toast.makeText(applicationContext, "Timer task finished", Toast.LENGTH_SHORT).show()
            setStartValues()
        }
    }

    private fun stopTimer() {
        jobTimer?.let { job ->
            job.cancel()
            Toast.makeText(applicationContext, "Timer task finished", Toast.LENGTH_SHORT).show()
            setStartValues()
        }
    }

    private fun setStartValues() {
        isStarted = false
        with(binding) {
            buttonStart.setText(R.string.button_text_start)
            sliderTimer.isEnabled = true
            sliderTimer.value = timerStartValue.toFloat()
            textInside.text = timerStartValue.toString()
            circularProgress.progress = INDICATOR_START_PROGRESS_VALUE
        }
    }

    private fun initListeners() {
        binding.sliderTimer.addOnChangeListener { _, value, _ ->
            binding.textInside.text = value.toInt().toString()
        }

        binding.buttonStart.setOnClickListener {
            if (!isStarted) {
                isStarted = true
                timerStartValue = binding.sliderTimer.value.toInt()
                startTimer()
            } else {
                isStarted = false
                stopTimer()
            }
        }
    }

    companion object {
        private const val INDICATOR_START_PROGRESS_VALUE = 100
    }
}