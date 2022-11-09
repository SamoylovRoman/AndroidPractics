package com.android.practice.contentprovidernew.presentation.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.practice.contentprovidernew.data.repository.ContactsRepositoryImpl
import com.android.practice.contentprovidernew.domain.usecases.ContactDetailsUseCaseImpl
import com.android.practice.contentprovidernew.domain.usecases.DownloadContactsListUseCaseImpl
import com.android.practice.contentprovidernew.domain.usecases.SaveNewContactUseCaseImpl
import com.android.practice.contentprovidernew.presentation.add_contact.AddContactViewModel
import com.android.practice.contentprovidernew.presentation.contact_details.ContactDetailsViewModel
import com.android.practice.contentprovidernew.presentation.contacts_list.ContactsListViewModel

class ViewModelFactory(context: Context) : ViewModelProvider.Factory {

    private val contactsRepository by lazy(LazyThreadSafetyMode.NONE) {
        ContactsRepositoryImpl(context = context)
    }

    private val contactDetailUseCase by lazy(LazyThreadSafetyMode.NONE) {
        ContactDetailsUseCaseImpl(repository = contactsRepository)
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
        ContactDetailsViewModel::class.java -> ContactDetailsViewModel(
            contactDetailUseCase = contactDetailUseCase
        )
        else -> throw IllegalArgumentException("Unknown ViewModel class")
    } as T
}