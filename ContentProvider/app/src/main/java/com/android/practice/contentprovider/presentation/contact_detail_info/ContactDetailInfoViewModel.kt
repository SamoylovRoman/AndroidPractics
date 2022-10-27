package com.android.practice.contentprovider.presentation.contact_detail_info

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.practice.contentprovider.domain.usecases.GetContactDetailUseCase
import com.android.practice.contentprovider.presentation.view_objects.ContactDetailVO
import kotlinx.coroutines.launch

class ContactDetailInfoViewModel(private val getContactDetailUseCase: GetContactDetailUseCase) :
    ViewModel() {

    private val _contactDetail = MutableLiveData<ContactDetailVO>()
    val contactDetail: LiveData<ContactDetailVO>
        get() = _contactDetail

    private val _contactEmailsString = MutableLiveData<String>()
    val contactEmailsString: LiveData<String>
        get() = _contactEmailsString

    fun getContactDetail(id: Long) {
        viewModelScope.launch {
            try {
                _contactDetail.postValue(getContactDetailUseCase.getContactDetail(id))
            } catch (t: Throwable) {
                Log.d("Error: ", "${t.message}")
            }
        }
    }

    fun getEmailsStringByContactId(id: Long) {
        viewModelScope.launch {
            try {
                _contactEmailsString.postValue(getContactDetailUseCase.getEmailsStringByContactId(id))
            } catch (t: Throwable) {
                Log.d("Error: ", "${t.message}")
            }
        }
    }
}