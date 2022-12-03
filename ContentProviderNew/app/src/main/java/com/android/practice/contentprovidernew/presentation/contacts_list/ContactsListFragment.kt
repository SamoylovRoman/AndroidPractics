package com.android.practice.contentprovidernew.presentation.contacts_list

import android.Manifest
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.practice.contentprovidernew.R
import com.android.practice.contentprovidernew.databinding.FragmentContactsListBinding
import com.android.practice.contentprovidernew.presentation.contacts_list.adapters.ContactListAdapter
import com.android.practice.contentprovidernew.presentation.extensions.showToast
import com.android.practice.contentprovidernew.presentation.utils.ViewModelFactory
import com.android.practice.contentprovidernew.presentation.utils.autoCleared
import com.android.practice.contentprovidernew.presentation.view_objects.ContactVO
import permissions.dispatcher.PermissionRequest
import permissions.dispatcher.ktx.constructPermissionsRequest

class ContactsListFragment : Fragment(R.layout.fragment_contacts_list) {

    private val viewModel: ContactsListViewModel by viewModels { ViewModelFactory(requireContext()) }
    private var contactAdapter: ContactListAdapter by autoCleared()
    private val binding: FragmentContactsListBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar()
        initContactsList()
        bindViewModel()
        initListeners()
        getContactsWithPermissionCheck()
    }

    private fun getContactsWithPermissionCheck() {
        Handler(Looper.getMainLooper()).post {
            constructPermissionsRequest(
                Manifest.permission.READ_CONTACTS,
                onShowRationale = ::onContactPermissionShowRationale,
                onPermissionDenied = ::onContactPermissionDenied,
                onNeverAskAgain = ::onContactPermissionNeverAskAgain,
                requiresPermission = { downloadContactsList() }
            )
                .launch()
        }
    }

    private fun onContactPermissionShowRationale(request: PermissionRequest) {
        request.proceed()
    }

    private fun onContactPermissionDenied() {
        showToast(R.string.contact_list_permission_denied)
    }

    private fun onContactPermissionNeverAskAgain() {
        showToast(R.string.contact_list_permission_never_ask_again)
    }

    private fun downloadContactsList() {
        viewModel.downloadContactsInList()
    }

    private fun bindViewModel() {
        viewModel.contactsInList.observe(viewLifecycleOwner) { list ->
            contactAdapter.items = list
        }
        viewModel.isLoading.observe(viewLifecycleOwner) { isVisible ->
            toggleProgress(isVisible)
        }
    }

    private fun initContactsList() {
        contactAdapter = ContactListAdapter { contact ->
            goToFragmentContactDetails(contact)
        }
        with(binding.contactList) {
            adapter = contactAdapter
            setHasFixedSize(true)
        }
    }

    private fun goToFragmentContactDetails(contact: ContactVO) {
        findNavController().navigate(
            ContactsListFragmentDirections.actionContactsListFragmentToContactDetailsFragment(
                contact
            )
        )
    }

    private fun initListeners() {
        binding.addContactButton.setOnClickListener {
            goToFragmentAddContact()
        }
    }

    private fun goToFragmentAddContact() {
        findNavController().navigate(R.id.action_contactsListFragment_to_addContactFragment)
    }

    private fun initToolBar() {
        binding.appBar.toolbar.setTitle(R.string.contact_list_title)
    }

    private fun toggleProgress(isVisible: Boolean) {
        with(binding) {
            progressBar.isVisible = isVisible
            contactList.isVisible = !isVisible
            addContactButton.isVisible = !isVisible
        }
    }

    companion object {
        const val TO_DETAILS = "to_fragment_details"
    }
}