package com.example.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import com.bumptech.glide.Glide
import com.example.fragments.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private var loginIsReady = false
    private var passwordIsReady = false
    private var agreeCheckIsReady = false
    private var state: FormState = FormState.EMPTY

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_VALID, state)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater)
        uploadImage()
        initListeners()
        restoreValidLoginState(savedInstanceState)
        restoreLoginButtonActiveState()
        return binding.root
    }

    private fun restoreValidLoginState(savedState: Bundle?) {
        if (savedState != null) {
            state = savedState.getParcelable(KEY_VALID) ?: error("Unexpected state")
            binding.textViewValid.setTextColor(state.color)
            binding.textViewValid.setText(state.message)
        }
    }

    private fun restoreLoginButtonActiveState() {
        if (binding.editTextLogin.text.isNotEmpty()
            && binding.editTextPassword.text.isNotEmpty()
            && binding.checkBoxAgree.text.isNotEmpty()
        ) {
            binding.buttonLogIn.isEnabled = true
            loginIsReady = true
            passwordIsReady = true
            agreeCheckIsReady = true
        }
    }

    private fun initListeners() {
        binding.checkBoxAgree.setOnCheckedChangeListener { _, b ->
            agreeCheckIsReady = b
            clearValidStateTextAfterChanges()
            tryToMakeLogInButtonActive()
        }

        binding.editTextLogin.addTextChangedListener {
            loginIsReady = it?.isNotEmpty()!!
            clearValidStateTextAfterChanges()
            tryToMakeLogInButtonActive()
        }

        binding.editTextPassword.addTextChangedListener {
            passwordIsReady = it?.isNotEmpty()!!
            clearValidStateTextAfterChanges()
            tryToMakeLogInButtonActive()
        }

        binding.buttonLogIn.setOnClickListener {
            showProgressBar()
        }
    }

    private fun clearValidStateTextAfterChanges() {
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED))
            binding.textViewValid.text = ""
    }

    private fun showProgressBar() {
        with(binding) {
            resetEnabled(false, editTextLogin, editTextPassword, checkBoxAgree, buttonLogIn)
            val view = layoutInflater.inflate(R.layout.progress_bar, constraintContainer, false)
            constraintContainer.addView(view)
            Handler(Looper.getMainLooper()).postDelayed({
                constraintContainer.removeView(view)
                updateStateText()
                if (state.valid) {
                    logIn()
                }
                resetEnabled(true, editTextLogin, editTextPassword, checkBoxAgree, buttonLogIn)
            }, DELAY_FOR_PROGRESS_BAR)
        }
    }

    private fun logIn() {
        (activity as? ActivityNavigator)?.showMainFragment()
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

    /* string str. If str is e-mail return true */
    private fun checkEmailValid(str: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(str).matches()
    }

    private fun checkPassValid(str: String): Boolean {
        return str.length > PASSWORD_LENGTH
    }

    private fun resetEnabled(state: Boolean, vararg views: View) {
        views.forEach {
            it.isEnabled = state
        }
    }

    private fun tryToMakeLogInButtonActive() {
        binding.buttonLogIn.isEnabled = agreeCheckIsReady && loginIsReady && passwordIsReady
    }

    private fun uploadImage() {
        Glide.with(this)
            .load(URL_PATH)
            .placeholder(R.drawable.png_download_arrow)
            .error(R.drawable.png_download_arrow)
            .into(binding.imageViewTopPicture)
    }

    companion object {
        private const val KEY_VALID = "valid"
        private const val DELAY_FOR_PROGRESS_BAR: Long = 2000
        private const val PASSWORD_LENGTH = 6
        private const val URL_PATH =
            "https://www.pngkit.com/png/full/281-2812821_user-account-management-logo-user-icon-png.png"

        fun newInstance() = LoginFragment()
    }
}