package com.android.practice.contentprovider.presentation.contact_detail_info

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.android.practice.contentprovider.databinding.FragmentContactDetailInfoBinding
import com.android.practice.contentprovider.presentation.utils.ViewModelFactory

private const val ARG_CONTACT_ID = "contactId"
private const val ARG_CONTACT_NAME = "contactName"
private const val ARG_CONTACT_PHONES_STRING = "contactEmailsString"

class ContactDetailInfoFragment : Fragment() {

    private var contactId: Long = 0
    private var contactName = ""
    private var contactPhonesString = ""

    companion object {
        fun newInstance(contactId: Long, contactName: String, contactPhonesString: String) =
            ContactDetailInfoFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_CONTACT_ID, contactId)
                    putString(ARG_CONTACT_NAME, contactName)
                    putString(ARG_CONTACT_PHONES_STRING, contactPhonesString)
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
            contactName = it.getString(ARG_CONTACT_NAME)!!
            contactPhonesString = it.getString(ARG_CONTACT_PHONES_STRING)!!
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
//            showContactDetail(contact)
        }
        viewModel.contactEmailsString.observe(viewLifecycleOwner) { emailsString ->
            showContactDetail(emailsString)
        }
    }

    private fun showContactDetail(emailsString: String) {
        binding.contactName.text = contactName
        binding.phonesListText.text = contactPhonesString
        binding.emailsListText.text = emailsString
    }

//    private fun showContactDetail() {
//        binding.contactName.text = contactName
//        binding.phonesListText.text = contactPhonesString
//        binding.emailsListText.text = contact.emails?.joinToString("\n")
//    }

    private fun loadContactDetail(contactId: Long) {
//        viewModel.getContactDetail(contactId)
        viewModel.getEmailsStringByContactId(contactId)
    }

}