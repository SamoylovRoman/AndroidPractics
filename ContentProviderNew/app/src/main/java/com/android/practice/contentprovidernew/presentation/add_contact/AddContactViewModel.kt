package com.android.practice.contentprovidernew.presentation.add_contact

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.practice.contentprovidernew.domain.usecases.SaveNewContactUseCase
import com.android.practice.contentprovidernew.presentation.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class AddContactViewModel(private val saveNewContactUseCase: SaveNewContactUseCase) : ViewModel() {

    private var _isSavingSuccess = SingleLiveEvent<Unit>()
    val isSavingSuccess: LiveData<Unit>
        get() = _isSavingSuccess

    private var _errorMessage = SingleLiveEvent<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    fun saveContact(name: String, phone: String, email: String?) {
        viewModelScope.launch {
            try {
                saveNewContactUseCase.saveNewContact(name, listOf(phone), listOf(email!!))
                _isSavingSuccess.postValue(Unit)
            } catch (t: Throwable) {
                _errorMessage.postValue(t.message)
            }
        }
    }
}