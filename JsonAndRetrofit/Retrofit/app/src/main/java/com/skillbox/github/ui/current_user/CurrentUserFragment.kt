package com.skillbox.github.ui.current_user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.skillbox.github.R
import com.skillbox.github.data.RemoteUser
import com.skillbox.github.databinding.FragmentCurrentUserBinding
import com.skillbox.github.utils.toast

class CurrentUserFragment : Fragment() {
    private var _binding: FragmentCurrentUserBinding? = null
    private val binding get() = _binding!!

    private val userModel: CurrentUserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCurrentUserBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()
        searchUserInfo()
        initButtonListener()
    }

    private fun bindViewModel() {
        userModel.users.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                setUserInfo(user)
            }
        }
        userModel.errorString.observe(viewLifecycleOwner) { errorString ->
            binding.errorText.text = errorString
            binding.errorText.isVisible = true
        }
        userModel.isLoading.observe(viewLifecycleOwner, ::updateIsLoading)
    }

    private fun searchUserInfo() {
        userModel.searchUserInfo()
    }

    private fun initButtonListener() {
        binding.setNewCompanyButton.setOnClickListener {
            if (binding.newCompanyText.text.isNotBlank()) {
                updateCompany(binding.newCompanyText.text.toString())
            } else {
                toast(R.string.new_company)
            }
        }
    }

    private fun updateCompany(text: String) {
        userModel.updateCompany(text)
    }

    private fun updateIsLoading(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
    }

    private fun setUserInfo(user: RemoteUser) {
        with(binding) {
            Glide.with(root)
                .load(user.avatar)
                .placeholder(R.drawable.ic_download)
                .error(R.drawable.ic_error)
                .into(avatarImage)
            userLogin.text = user.username
            userId.text = user.id.toString()
            userCompany.text = user.company
        }
    }
}