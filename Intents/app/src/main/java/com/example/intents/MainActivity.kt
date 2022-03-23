package com.example.intents

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.intents.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val dialLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.data == null) showToast(getString(R.string.text_no_data))
            else showToast(getString(R.string.text_data))
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListeners()
    }

    private fun initListeners() {
        binding.buttonToMakeDial.setOnClickListener {
            if (checkPhoneNumber(binding.editTextPhoneNumber.text.toString())) {
                val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:${binding.editTextPhoneNumber.text}")
                }
                if (dialIntent.resolveActivity(packageManager) != null) {
                    dialLauncher.launch(dialIntent)
                } else showToast(getString(R.string.text_no_required_app))
            } else showToast(getString(R.string.text_incorrect_phone_number))
        }
    }

    /* construction Patterns.PHONE.matcher(str).matches() checks
    * string str. If str is phone number return true, else return false */
    private fun checkPhoneNumber(str: String): Boolean {
        return Patterns.PHONE.matcher(str).matches()
    }

    private fun showToast(str: String) {
        Toast.makeText(
            applicationContext,
            str,
            Toast.LENGTH_SHORT
        ).show()
    }

}