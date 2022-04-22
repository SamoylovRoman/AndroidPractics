package com.example.recyclerviewlist

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.recyclerviewlist.databinding.DialogAddStaffBinding

class AddStaffDialogFragment : DialogFragment() {
    private var _binding: DialogAddStaffBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogAddStaffBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    private fun initListeners() {
        binding.addButton.setOnClickListener {
            if (!checkForm()) {
                showWarningDialog()
            } else {
                addNewStaff()
                dismiss()
            }
        }

        binding.checkboxManagementTeam.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.emailEditText.visibility = View.VISIBLE
            } else binding.emailEditText.visibility = View.GONE
        }

        binding.skipButton.setOnClickListener {
            dismiss()
        }
    }

    private fun showWarningDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.text_warning))
            .setMessage(getString(R.string.text_warning_message))
            .setPositiveButton(getString(R.string.text_fill_in)) { _, _ -> }
            .create()
            .show()
    }

    private fun addNewStaff() {
        if (binding.checkboxManagementTeam.isChecked) {
            (parentFragment as AddDialogListener)
                .onAddButtonClicked(
                    Staff.Manager(
                        fullName = binding.fullNameEditText.text.toString(),
                        position = binding.positionEditText.text.toString(),
                        isManagementTeam = true,
                        phoneNumber = binding.phoneNumberEditText.text.toString(),
                        emailAddress = binding.emailEditText.text.toString(),
                        photoLink = binding.photoLinkEditText.text.toString()
                    )
                )
        } else {
            (parentFragment as AddDialogListener)
                .onAddButtonClicked(
                    Staff.Employee(
                        fullName = binding.fullNameEditText.text.toString(),
                        position = binding.positionEditText.text.toString(),
                        isManagementTeam = true,
                        phoneNumber = binding.phoneNumberEditText.text.toString(),
                        photoLink = binding.photoLinkEditText.text.toString()
                    )
                )
        }
    }

    private fun checkForm(): Boolean {
        if (
            binding.fullNameEditText.text.isNotBlank()
            && binding.positionEditText.text.isNotBlank()
            && binding.phoneNumberEditText.text.isNotBlank()
            && binding.photoLinkEditText.text.isNotBlank()
        ) {
            if (
                (binding.checkboxManagementTeam.isChecked && binding.emailEditText.text.isNotBlank())
                || !binding.checkboxManagementTeam.isChecked
            ) {
                return true
            }
        }
        return false
    }

    companion object {
        const val DIALOG_TAG = "Add staff dialog tag"
    }
}