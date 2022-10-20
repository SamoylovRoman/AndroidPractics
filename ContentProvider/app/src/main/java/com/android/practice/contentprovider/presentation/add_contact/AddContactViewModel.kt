package com.android.practice.contentprovider.presentation.add_contact

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.practice.contentprovider.domain.usecases.SaveNewContactUseCase
import kotlinx.coroutines.launch

class AddContactViewModel(private val saveNewContactUseCase: SaveNewContactUseCase) : ViewModel() {

    fun saveNewContact(name: String, phone: String, email: String = "") {
        viewModelScope.launch {
            try {
                saveNewContactUseCase.saveNewContact(name, phone, email)
            } catch (t: Throwable) {
                Log.d("Error: ", "${t.message}")
            }
        }
    }
}