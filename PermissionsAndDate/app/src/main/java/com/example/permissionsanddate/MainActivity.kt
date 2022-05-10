package com.example.permissionsanddate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit
import com.example.permissionsanddate.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
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