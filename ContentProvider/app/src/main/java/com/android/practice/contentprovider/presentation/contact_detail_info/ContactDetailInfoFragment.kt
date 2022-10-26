package com.android.practice.contentprovider.presentation.contact_detail_info

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.android.practice.contentprovider.databinding.FragmentContactDetailInfoBinding
import com.android.practice.contentprovider.presentation.utils.ViewModelFactory
import com.android.practice.contentprovider.presentation.view_objects.ContactDetailVO

private const val ARG_CONTACT_ID = "contactId"

class ContactDetailInfoFragment : Fragment() {

    private var contactId: Long = 0

    companion object {
        fun newInstance(contactId: Long) =
            ContactDetailInfoFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_CONTACT_ID, contactId)
                }
            }
    }

    private lateinit var binding: FragmentContactDetailInfoBinding
    private val viewModel: ContactDetailInfoViewModel by viewModels {
        ViewModelFactory(
            requireContext()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            contactId = it.getLong(ARG_CONTACT_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContactDetailInfoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()
        loadContactDetail(contactId)
    }

    private fun bindViewModel() {
        viewModel.contactDetail.observe(viewLifecycleOwner) { contact ->
            showContactDetail(contact)
        }
    }

    private fun showContactDetail(contact: ContactDetailVO) {
        binding.contactName.text = contact.name
        binding.phonesListText.text = contact.phones.joinToString("\n")
        binding.emailsListText.text = contact.emails?.joinToString("\n")
    }

    private fun loadContactDetail(contactId: Long) {
        viewModel.getContactDetail(contactId)
    }

}