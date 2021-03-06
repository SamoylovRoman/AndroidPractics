package com.example.intents

import android.content.Intent
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

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent!!.data?.lastPathSegment?.let { endOfURL ->
            binding.textViewDeepLink.text = endOfURL
        }
    }

    private fun handleIntentData() {
        intent.data?.lastPathSegment?.let { endOfURL ->
            binding.textViewDeepLink.text = endOfURL
        }
    }
}