package com.android.practice.contentprovider.presentation.contacts_list

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.practice.contentprovider.R
import com.android.practice.contentprovider.databinding.FragmentContactsListBinding
import com.android.practice.contentprovider.presentation.add_contact.AddContactFragment
import com.android.practice.contentprovider.presentation.contact_detail_info.ContactDetailInfoFragment
import com.android.practice.contentprovider.presentation.contacts_list.adapter.ContactsListAdapter
import com.android.practice.contentprovider.presentation.extensions.showToast
import com.android.practice.contentprovider.presentation.utils.ViewModelFactory
import permissions.dispatcher.PermissionRequest
import permissions.dispatcher.ktx.constructPermissionsRequest

class ContactsListFragment : Fragment() {

    companion object {
        fun newInstance() = ContactsListFragment()
    }

    private lateinit var binding: FragmentContactsListBinding
    private lateinit var contactsListAdapter: ContactsListAdapter
    private val viewModel: ContactsListViewModel by viewModels { ViewModelFactory(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContactsListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        initContactsList()
        bindViewModel()
//        downloadContactsList()
        getContactsWithPermissionCheck()
    }

    private fun downloadContactsList() {
        viewModel.downloadContactsInList()
    }


    private fun getContactsWithPermissionCheck() {
        Handler(Looper.getMainLooper()).post {
            constructPermissionsRequest(
                android.Manifest.permission.READ_CONTACTS,
                onShowRationale = ::onContactsPermissionShowRationale,
                onPermissionDenied = { onContactPermissionDenied(android.Manifest.permission.READ_CONTACTS) },
                onNeverAskAgain = { onContactPermissionNeverAskAgain(android.Manifest.permission.READ_CONTACTS) },
                requiresPermission = {
                    downloadContactsList()
                }
            ).launch()
        }
    }

    private fun addNewContactsWithPermissionCheck() {
        Handler(Looper.getMainLooper()).post {
            constructPermissionsRequest(
                android.Manifest.permission.WRITE_CONTACTS,
                onShowRationale = ::onContactsPermissionShowRationale,
                onPermissionDenied = { onContactPermissionDenied(android.Manifest.permission.WRITE_CONTACTS) },
                onNeverAskAgain = { onContactPermissionNeverAskAgain(android.Manifest.permission.WRITE_CONTACTS) },
                requiresPermission = {
                    showAddContactFragment()
                }
            ).launch()
        }
    }

    private fun onContactsPermissionShowRationale(request: PermissionRequest) {
        request.proceed()
    }

    private fun onContactPermissionDenied(permission: String) {
        when (permission) {
            android.Manifest.permission.READ_CONTACTS -> showToast(R.string.reading_is_prohibited)
            android.Manifest.permission.WRITE_CONTACTS -> showToast(R.string.writing_is_prohibited)
        }
    }

    private fun onContactPermissionNeverAskAgain(permission: String) {
        when (permission) {
            android.Manifest.permission.READ_CONTACTS -> showToast(R.string.never_ask_reading)
            android.Manifest.permission.WRITE_CONTACTS -> showToast(R.string.never_ask_writing)
        }
    }

    private fun bindViewModel() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
//            toggleProgress(isLoading)
        }
        viewModel.contactsInList.observe(viewLifecycleOwner) { newList ->
            newList.forEach { Log.d("AAA", "${it.name}: ${it.phonesString}") }
            contactsListAdapter.submitList(newList)
        }
    }

    private fun toggleProgress(loading: Boolean) {
        with(binding) {
            progressBar.isVisible = loading
            contactsList.isVisible = !loading
            addContactButton.isVisible = !loading
        }
    }

    private fun initListeners() {
        binding.addContactButton.setOnClickListener {
            addNewContactsWithPermissionCheck()
        }
    }

    private fun showAddContactFragment() {
        parentFragmentManager.beginTransaction()
            .replace(
                R.id.fragmentContainer,
                AddContactFragment.newInstance(),
                AddContactFragment::class.java.simpleName
            )
            .addToBackStack(null)
            .commit()
    }

    private fun initContactsList() {
        contactsListAdapter = ContactsListAdapter { contactId ->
            openContactDetailFragment(contactId)
        }
        with(binding.contactsList) {
            adapter = contactsListAdapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }
    }

    private fun openContactDetailFragment(contactId: Long) {
        parentFragmentManager.beginTransaction()
            .replace(
                R.id.fragmentContainer,
                ContactDetailInfoFragment.newInstance(contactId = contactId),
                ContactDetailInfoFragment::class.java.simpleName
            )
            .addToBackStack(null)
            .commit()
    }

}