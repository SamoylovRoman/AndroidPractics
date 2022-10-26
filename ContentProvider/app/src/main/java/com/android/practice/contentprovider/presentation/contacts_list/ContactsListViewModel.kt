package com.android.practice.contentprovider.presentation.contacts_list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.practice.contentprovider.domain.usecases.DownloadContactsListUseCase
import com.android.practice.contentprovider.presentation.view_objects.ContactInListVO
import kotlinx.coroutines.launch

class ContactsListViewModel(private val downloadContactsListUseCase: DownloadContactsListUseCase) :
    ViewModel() {

    private var _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _contactsInList = MutableLiveData<List<ContactInListVO>>()
    val contactsInList: LiveData<List<ContactInListVO>>
        get() = _contactsInList

    fun downloadContactsInList() {
        Log.d("AAA", "View Model downloadContactsInList")
        viewModelScope.launch {
            try {
                _contactsInList.postValue(downloadContactsListUseCase.downloadContacts())
            } catch (t: Throwable) {
                Log.d("Contacts downloading: ", "${t.message}")
                _contactsInList.postValue(emptyList())
            }
        }
    }
}