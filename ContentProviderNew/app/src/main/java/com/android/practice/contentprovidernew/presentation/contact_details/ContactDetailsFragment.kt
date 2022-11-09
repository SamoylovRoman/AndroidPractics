package com.android.practice.contentprovidernew.presentation.contact_details

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.practice.contentprovidernew.R
import com.android.practice.contentprovidernew.databinding.FragmentContactDetailsBinding
import com.android.practice.contentprovidernew.presentation.extensions.showToast
import com.android.practice.contentprovidernew.presentation.utils.ViewModelFactory
import com.android.practice.contentprovidernew.presentation.view_objects.ContactVO

class ContactDetailsFragment : Fragment(R.layout.fragment_contact_details) {

    private val viewModel: ContactDetailsViewModel by viewModels { ViewModelFactory(requireContext()) }
    private val binding: FragmentContactDetailsBinding by viewBinding()
    private val args: ContactDetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (args.contact != null)
            bindContactDetails(args.contact!!)
        initListeners()
        bindViewModel()
    }

    private fun initListeners() {
        binding.deleteContactButton.setOnClickListener {
            viewModel.showAlertDialog()
        }
    }

    private fun bindContactDetails(contact: ContactVO) {
        binding.contactName.text = contact.name
        binding.phonesListText.text = contact.phoneNumbers.joinToString("\n")
        binding.emailsListText.text = contact.emails.joinToString("\n")
    }

    private fun bindViewModel() {
        viewModel.isDeleted.observe(viewLifecycleOwner) { isDeleted ->
            if (isDeleted) {
                findNavController().popBackStack()
            } else {
                showToast(R.string.cannot_be_deleted)
            }
        }
        viewModel.isDialogShowing.observe(viewLifecycleOwner) { isShowing ->
            if (isShowing) {
                showRemoveDialog()
            }
        }
    }

    private fun showRemoveDialog() {
        AlertDialog.Builder(requireContext())
            .setIcon(R.drawable.ic_report)
            .setMessage(getString(R.string.to_remove_contact))
            .setPositiveButton("Yes, I do") { dialog, _ ->
                viewModel.deleteContact(args.contact!!.id)
                dialog.cancel()
                viewModel.hideAlertDialog()
            }
            .setNegativeButton("No, I don't") { dialog, _ ->
                dialog.cancel()
                viewModel.hideAlertDialog()
            }
            .show()
    }
}