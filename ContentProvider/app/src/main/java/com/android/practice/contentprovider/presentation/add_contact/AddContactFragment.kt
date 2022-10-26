package com.android.practice.contentprovider.presentation.add_contact

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.android.practice.contentprovider.R
import com.android.practice.contentprovider.databinding.FragmentAddContactBinding
import com.android.practice.contentprovider.presentation.extensions.showToast
import com.android.practice.contentprovider.presentation.utils.ViewModelFactory

class AddContactFragment : Fragment() {

    companion object {
        fun newInstance() = AddContactFragment()
    }

    private lateinit var binding: FragmentAddContactBinding
    private val viewModel: AddContactViewModel by viewModels {
        ViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddContactBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    private fun initListeners() {
        with(binding) {
            saveNewContactButton.setOnClickListener {
                if (fieldsAreFilled()) {
                    viewModel.saveNewContact(
                        "${firstNameText.editText?.text} ${lastNameText.editText?.text}",
                        listOf(phoneNumberText.editText?.text.toString()),
                        listOf(emailAddressText.editText?.text.toString())
                    )
                    parentFragmentManager.popBackStack()
                } else {
                    showToast(R.string.fill_fields)
                }
            }
        }
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