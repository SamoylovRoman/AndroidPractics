package com.example.activitylifecycle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.Patterns
import android.view.View
import androidx.core.widget.addTextChangedListener
import com.bumptech.glide.Glide
import com.example.activitylifecycle.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var loginIsReady = false
    private var passwordIsReady = false
    private var agreeIsReady = false
    private val tag = "Activity lifecycle LOG"
    private var state: FormState = FormState.EMPTY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d(tag, "onCreate ${hashCode()}")

        restoreState(savedInstanceState)
        uploadImage()
        initListeners()
    }

    private fun restoreState(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            state = savedInstanceState.getParcelable<FormState>(KEY_VALID)
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
        binding.buttonSimulationANR.setOnClickListener {
            simulateANR()
        }
    }

    /* If str is e-mail return true */
    private fun islValid(str: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(str).matches()
    }

    private fun isValid(str: String): Boolean {
        return str.length > PASSWORD_LENGTH
    }

    private fun simulateANR() {
        Thread.sleep(ANR_DELAY_MILE_SECOND)
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
                updateStateText()
                resetEnabled(true, editTextLogin, editTextPassword, checkBoxAgree, buttonLogIn)
            }, 2000)
        }
    }

    private fun updateStateText() {
        if (islValid(binding.editTextLogin.text.toString())
            && isValid(binding.editTextPassword.text.toString())
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
        private const val ANR_DELAY_MILE_SECOND: Long = 10000
        private const val PASSWORD_LENGTH = 6
        private const val KEY_VALID = "valid"
        private const val URL_PATH =
            "https://www.pngkit.com/png/full/281-2812821_user-account-management-logo-user-icon-png.png"
    }
}