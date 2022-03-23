package com.example.intents

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.intents.databinding.ActivityDeepLinkBinding

class DeepLinkActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDeepLinkBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeepLinkBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleIntentData()
    }

    private fun handleIntentData() {
        intent.data?.lastPathSegment?.let {
            binding.textViewDeepLink.text = it
        }
    }
}