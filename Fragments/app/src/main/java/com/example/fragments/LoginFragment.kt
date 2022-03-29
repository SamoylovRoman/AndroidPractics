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
import com.bumptech.glide.Glide
import com.example.fragments.databinding.FragmentLoginBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private var loginIsReady = false
    private var passwordIsReady = false
    private var agreeIsReady = false
    private val taga = "Intents LOG"
    private var state: FormState = FormState.EMPTY

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater)
        uploadImage()
        initListeners()
        return binding.root
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

    private fun tryToLogin() {
        if (state.valid) {
            (activity as? LoginButtonClickListener)?.onButtonClicked(MainActivity.ARG_TO_SHOW_MAIN_FRAGMENT)
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
        private const val URL_PATH =
            "https://www.pngkit.com/png/full/281-2812821_user-account-management-logo-user-icon-png.png"

/*        *//**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginFragment.
         *//*
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }*/
    }
}