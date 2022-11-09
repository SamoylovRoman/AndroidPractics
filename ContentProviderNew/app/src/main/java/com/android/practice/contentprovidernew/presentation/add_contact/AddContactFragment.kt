package com.android.practice.contentprovidernew.presentation.add_contact

import android.Manifest
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.practice.contentprovidernew.R
import com.android.practice.contentprovidernew.databinding.FragmentAddContactBinding
import com.android.practice.contentprovidernew.presentation.extensions.showToast
import com.android.practice.contentprovidernew.presentation.utils.ViewModelFactory
import permissions.dispatcher.PermissionRequest
import permissions.dispatcher.ktx.constructPermissionsRequest

class AddContactFragment : Fragment(R.layout.fragment_add_contact) {

    private val viewModel: AddContactViewModel by viewModels { ViewModelFactory(requireContext()) }
    private val binding: FragmentAddContactBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        bindViewModel()
    }

    private fun bindViewModel() {
        viewModel.isSavingSuccess.observe(viewLifecycleOwner) {
            findNavController().popBackStack()
        }
        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            showToast(message)
        }
    }

    private fun initListeners() {
        binding.saveNewContactButton.setOnClickListener {
            if (fieldsAreFilled()) {
                saveContactWithPermissionCheck()
            } else {
                showToast(R.string.fill_fields)
            }
        }
    }

    private fun saveContactWithPermissionCheck() {
        constructPermissionsRequest(
            Manifest.permission.WRITE_CONTACTS,
            onShowRationale = ::onContactPermissionShowRationale,
            onPermissionDenied = ::onContactPermissionDenied,
            onNeverAskAgain = ::onContactPermissionNeverAskAgain
        ) {
            saveContact()
        }.launch()
    }

    private fun onContactPermissionShowRationale(request: PermissionRequest) {
        request.proceed()
    }

    private fun onContactPermissionDenied() {
        showToast(R.string.writing_is_prohibited)
    }

    private fun onContactPermissionNeverAskAgain() {
        showToast(R.string.never_ask_writing)
    }

    private fun saveContact() {
        viewModel.saveContact(
            name = "${binding.firstNameText.editText?.text?.toString().orEmpty()} " +
                    binding.lastNameText.editText?.text?.toString().orEmpty(),
            phone = binding.phoneNumberText.editText?.text?.toString().orEmpty(),
            email = binding.emailAddressText.editText?.text?.toString()
        )
    }

    private fun fieldsAreFilled(): Boolean {
        if (binding.firstNameText.editText?.text!!.isBlank() ||
            binding.lastNameText.editText?.text!!.isBlank() ||
            binding.phoneNumberText.editText?.text!!.isBlank()
        ) {
            return false
        }
        return true
    }

}