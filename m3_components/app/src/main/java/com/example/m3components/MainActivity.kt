package com.example.m3components

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.m3components.databinding.ActivityMainBinding



class MainActivity : AppCompatActivity() {
//    private var currentProgress: Int = 10
    private var isStarted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fun startTimer() {

            if (isStarted) {
                binding.slider.isEnabled = false
                binding.buttonStart.setText(R.string.button_text_stop)
                for (i in binding.slider.value.toInt() downTo 0) {
                    binding.textInside.text = i.toString()
                    Thread.sleep(1000)
                }
            } else {
                binding.slider.isEnabled = false
                binding.textInside.text = binding.slider.value.toInt().toString()
                binding.buttonStart.setText(R.string.button_text_start)
            }
        }

        binding.slider.addOnChangeListener { slider, value, fromUser ->
            binding.textInside.text = value.toInt().toString()

        }


        binding.buttonStart.setOnClickListener {

            if (!isStarted) {
                isStarted = true
                startTimer()
            } else {
                isStarted = false
                startTimer()
            }
        }


    }
}