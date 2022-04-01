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

    /*If childFragmentManager of MainFragment contains more than one fragment entries,
    * pop fragment from childFragmentManager.
    * Else - run super.onBackPressed() realization. */
    override fun onBackPressed() {
        val childFragmentBackStack =
            supportFragmentManager.findFragmentById(R.id.fragmentContainer)?.childFragmentManager
        when {
            childFragmentBackStack != null && childFragmentBackStack.backStackEntryCount > 1 ->
                childFragmentBackStack.popBackStack()
            else -> super.onBackPressed()
        }
    }

    private fun showLoginFragment() {
        val loginFragment = LoginFragment.newInstance()
        supportFragmentManager.commit {
            replace(R.id.fragmentContainer, loginFragment)
        }
    }

    override fun showMainFragment() {
        val mainFragment = MainFragment.newInstance()
        supportFragmentManager.commit {
            replace(R.id.fragmentContainer, mainFragment)
        }
    }
}