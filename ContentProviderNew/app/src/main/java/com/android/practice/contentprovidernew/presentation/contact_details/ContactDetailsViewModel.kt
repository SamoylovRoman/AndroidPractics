package com.android.practice.contentprovidernew.presentation.contact_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.practice.contentprovidernew.domain.usecases.ContactDetailsUseCase
import kotlinx.coroutines.launch

class ContactDetailsViewModel(private val contactDetailUseCase: ContactDetailsUseCase) :
    ViewModel() {

    private var _isDeleted = MutableLiveData<Boolean>()
    val isDeleted: LiveData<Boolean>
        get() = _isDeleted

    private var _isDialogShowing = MutableLiveData<Boolean>()
    val isDialogShowing: LiveData<Boolean>
        get() = _isDialogShowing

    fun deleteContact(id: Long) {
        viewModelScope.launch {
            _isDeleted.postValue(contactDetailUseCase.deleteCurrentContact(id))
        }
    }

    fun showAlertDialog() {
        _isDialogShowing.postValue(true)
    }

    fun hideAlertDialog() {
        _isDialogShowing.postValue(false)
    }
}