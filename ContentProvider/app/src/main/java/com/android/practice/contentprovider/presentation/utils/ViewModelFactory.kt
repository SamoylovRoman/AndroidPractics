package com.android.practice.contentprovider.presentation.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.practice.contentprovider.data.models.ContactsRepositoryImpl
import com.android.practice.contentprovider.domain.usecases.DownloadContactsListUseCaseImpl
import com.android.practice.contentprovider.domain.usecases.GetContactDetailUseCaseImpl
import com.android.practice.contentprovider.domain.usecases.SaveNewContactUseCaseImpl
import com.android.practice.contentprovider.presentation.add_contact.AddContactViewModel
import com.android.practice.contentprovider.presentation.contact_detail_info.ContactDetailInfoViewModel
import com.android.practice.contentprovider.presentation.contacts_list.ContactsListViewModel

class ViewModelFactory(context: Context) : ViewModelProvider.Factory {

    private val contactsRepository by lazy(LazyThreadSafetyMode.NONE) {
        ContactsRepositoryImpl(context = context)
    }

    private val getContactDetailUseCase by lazy(LazyThreadSafetyMode.NONE) {
        GetContactDetailUseCaseImpl(repository = contactsRepository)
    }

    private val downloadContactsListUseCase by lazy(LazyThreadSafetyMode.NONE) {
        DownloadContactsListUseCaseImpl(repository = contactsRepository)
    }

    private val saveNewContactUseCase by lazy(LazyThreadSafetyMode.NONE) {
        SaveNewContactUseCaseImpl(repository = contactsRepository)
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when (modelClass) {
        ContactsListViewModel::class.java -> ContactsListViewModel(
            downloadContactsListUseCase = downloadContactsListUseCase
        )
        AddContactViewModel::class.java -> AddContactViewModel(
            saveNewContactUseCase = saveNewContactUseCase
        )
        ContactDetailInfoViewModel::class.java -> ContactDetailInfoViewModel(
            getContactDetailUseCase = getContactDetailUseCase
        )
        else -> throw IllegalArgumentException("Unknown ViewModel class")
    } as T
}