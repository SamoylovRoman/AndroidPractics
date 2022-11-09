package com.android.practice.contentprovidernew.presentation.contacts_list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.practice.contentprovidernew.domain.usecases.DownloadContactsListUseCase
import com.android.practice.contentprovidernew.presentation.view_objects.ContactVO
import kotlinx.coroutines.launch

class ContactsListViewModel(private val downloadContactsListUseCase: DownloadContactsListUseCase) :
    ViewModel() {

    private var _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _contactsInList = MutableLiveData<List<ContactVO>>()
    val contactsInList: LiveData<List<ContactVO>>
        get() = _contactsInList

    fun downloadContactsInList() {
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                _contactsInList.postValue(downloadContactsListUseCase.downloadContacts())
                _isLoading.postValue(false)
            } catch (t: Throwable) {
                Log.d("Contacts downloading: ", "${t.message}")
                _isLoading.postValue(false)
                _contactsInList.postValue(emptyList())
            }
        }
    }
}