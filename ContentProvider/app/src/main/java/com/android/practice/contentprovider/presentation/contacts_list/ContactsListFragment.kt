package com.android.practice.contentprovider.presentation.contacts_list

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.practice.contentprovider.R

class ContactsListFragment : Fragment() {

    companion object {
        fun newInstance() = ContactsListFragment()
    }

    private lateinit var viewModel: ContactsListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contacts_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ContactsListViewModel::class.java)
        // TODO: Use the ViewModel
    }

}