package com.example.viewpager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit
import com.example.viewpager.databinding.ActivityAppBinding

class AppActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAppBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            showMainFragment()
        }
    }

    private fun showMainFragment() {
        val mainFragment = MainFragment.newInstance()
        supportFragmentManager.commit {
            replace(R.id.fragmentContainer, mainFragment)
        }
    }
}