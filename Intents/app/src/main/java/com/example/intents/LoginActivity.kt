package com.example.intents

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.Patterns
import android.view.View
import androidx.core.widget.addTextChangedListener
import com.bumptech.glide.Glide
import com.example.intents.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private var loginIsReady = false
    private var passwordIsReady = false
    private var agreeIsReady = false
    private val tag = "Intents LOG"
    private var state: FormState = FormState.EMPTY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d(tag, "onCreate ${hashCode()}")

        restoreState(savedInstanceState)
        uploadImage()
        initListeners()
    }

    private fun restoreState(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            state = savedInstanceState.getParcelable(KEY_VALID)
                ?: error("Unexpected state")
            Log.d(tag, getString(state.message))
            binding.textViewValid.setTextColor(state.color)
            binding.textViewValid.setText(state.message)
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(tag, "onStart ${hashCode()}")
    }

    override fun onResume() {
        super.onResume()
        Log.d(tag, "onResume ${hashCode()}")
    }

    override fun onPause() {
        super.onPause()
        Log.d(tag, "onPause ${hashCode()}")
    }

    override fun onStop() {
        super.onStop()
        Log.d(tag, "onStop ${hashCode()}")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(tag, "onDestroy ${hashCode()}")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_VALID, state)
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

    private fun tryToLogin() {
        if (state.valid) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    /* string str. If str is e-mail return true */
    private fun checkEmailValid(str: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(str).matches()
    }

    private fun checkPassValid(str: String): Boolean {
        return str.length > PASSWORD_LENGTH
    }

    private fun showProgressBar() {
        with(binding) {
            resetEnabled(false, editTextLogin, editTextPassword, checkBoxAgree, buttonLogIn)
            val view = layoutInflater.inflate(R.layout.progress_bar, constraintContainer, false)
            constraintContainer.addView(view)
            Handler(Looper.getMainLooper()).postDelayed({
                constraintContainer.removeView(view)
                updateStateText()
                tryToLogin()
                resetEnabled(true, editTextLogin, editTextPassword, checkBoxAgree, buttonLogIn)
            }, DELAY_FOR_PROGRESS_BAR)
        }
    }

    private fun updateStateText() {
        if (checkEmailValid(binding.editTextLogin.text.toString())
            && checkPassValid(binding.editTextPassword.text.toString())
        ) {
            state = state.getValidState()
            Log.d(tag, "updateValidText -> ${state.message}")
            binding.textViewValid.setTextColor(state.color)
            binding.textViewValid.setText(state.message)
        } else {
            state = state.getInvalidState()
            Log.d(tag, "updateValidText -> ${state.message}")
            binding.textViewValid.setTextColor(state.color)
            binding.textViewValid.setText(state.message)
        }
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
        private const val DELAY_FOR_PROGRESS_BAR: Long = 2000
        private const val PASSWORD_LENGTH = 6
        private const val KEY_VALID = "valid"
        private const val URL_PATH =
            "https://www.pngkit.com/png/full/281-2812821_user-account-management-logo-user-icon-png.png"
    }
}