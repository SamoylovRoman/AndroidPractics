package com.example.fragments

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.fragments.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), ActivityNavigator {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null)
            showLoginFragment()
    }

    private fun showLoginFragment() {
        supportFragmentManager.commit {
            replace<LoginFragment>(R.id.fragmentContainer)
        }
    }

    override fun showMainFragment() {
        supportFragmentManager.commit {
            replace<MainFragment>(R.id.fragmentContainer)
            addToBackStack(MainFragment::class.java.simpleName)
        }
    }
}