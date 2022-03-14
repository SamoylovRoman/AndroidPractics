package com.example.constraintlayout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.bumptech.glide.Glide
import com.example.constraintlayout.R
import com.example.constraintlayout.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var loginIsReady = false
    private var passwordIsReady = false
    private var agreeIsReady = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        uploadImage()
        initListeners()

    }

    private fun initListeners() {
        binding.checkBoxAgree.setOnCheckedChangeListener { _, b ->
            agreeIsReady = b
            tryToMakeLogInButtonActive()
        }

        binding.editTextLogin.addTextChangedListener {
            loginIsReady = it?.isNotEmpty()!!
            tryToMakeLogInButtonActive()
        }

        binding.editTextPassword.addTextChangedListener {
            passwordIsReady = it?.isNotEmpty()!!
            tryToMakeLogInButtonActive()
        }

        binding.buttonLogIn.setOnClickListener {
            showProgressBar()
        }
    }

    private fun showProgressBar() {
        with(binding) {
/*            lifecycleScope.launch {
                resetEnabled(editTextLogin, editTextPassword, checkBoxAgree, buttonLogIn)
                val view = layoutInflater.inflate(R.layout.progress_bar, frameContainer, false)
                frameContainer.addView(view)
                delay(2000)
                frameContainer.removeView(view)
                showToast()
                resetEnabled(editTextLogin, editTextPassword, checkBoxAgree, buttonLogIn)
            }*/
            resetEnabled(false, editTextLogin, editTextPassword, checkBoxAgree, buttonLogIn)
            val view = layoutInflater.inflate(R.layout.progress_bar, constraintContainer, false)
            constraintContainer.addView(view)
            Handler(Looper.getMainLooper()).postDelayed({
                constraintContainer.removeView(view)
                showToast()
                resetEnabled(true, editTextLogin, editTextPassword, checkBoxAgree, buttonLogIn)
            }, 2000)
        }
    }

    private fun showToast() {
        Toast.makeText(
            applicationContext,
            getString(R.string.text_logged_correctly),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun resetEnabled(state: Boolean, vararg views: View) {
        views.forEach {
            it.isEnabled = state
        }
    }

    private fun tryToMakeLogInButtonActive() {
        binding.buttonLogIn.isEnabled = agreeIsReady && loginIsReady && passwordIsReady
    }

    private fun uploadImage() {
        Glide.with(this)
            .load(URL_PATH)
            .placeholder(R.drawable.png_download_arrow)
            .error(R.drawable.png_download_arrow)
            .into(binding.imageViewTopPicture)
    }

    companion object {
        private const val URL_PATH =
            "https://www.pngkit.com/png/full/281-2812821_user-account-management-logo-user-icon-png.png"
    }
}