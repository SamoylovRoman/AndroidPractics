package com.example.fragments

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.fragments.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), LoginButtonClickListener {
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
//            addToBackStack(null)
        }
    }

    override fun onBackPressed() {


        supportFragmentManager.fragments.forEach {
            Log.d("onBackPressed", it.toString())
        }
        super.onBackPressed()
        supportFragmentManager.fragments.forEach {
            Log.d("onBackPressed after", it.toString())
        }
    }

    private fun initListeners() {

    }

    private fun showToast(str: String) {
        Toast.makeText(
            applicationContext,
            str,
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onButtonClicked(arg: String) {
        if (arg == ARG_TO_SHOW_MAIN_FRAGMENT) {
            supportFragmentManager.commit {
                replace<MainFragment>(R.id.fragmentContainer)
                addToBackStack(MainFragment::class.java.simpleName)
            }
        }
    }

    companion object {
        const val ARG_TO_SHOW_MAIN_FRAGMENT = "Main Fragment"

    }
}