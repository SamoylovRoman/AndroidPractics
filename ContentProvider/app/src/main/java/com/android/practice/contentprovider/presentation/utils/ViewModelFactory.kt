package com.android.practice.contentprovider.presentation.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.practice.contentprovider.data.models.ContactsRepositoryImpl
import com.android.practice.contentprovider.domain.usecases.DownloadContactsListUseCaseImpl
import com.android.practice.contentprovider.presentation.contacts_list.ContactsListViewModel

class ViewModelFactory(context: Context) : ViewModelProvider.Factory {

    private val contactsRepository by lazy(LazyThreadSafetyMode.NONE) {
        ContactsRepositoryImpl(context = context)
    }

    private val downloadContactsListUseCase by lazy(LazyThreadSafetyMode.NONE) {
        DownloadContactsListUseCaseImpl(repository = contactsRepository)
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ContactsListViewModel(
            downloadContactsListUseCase = downloadContactsListUseCase
        ) as T
    }
}