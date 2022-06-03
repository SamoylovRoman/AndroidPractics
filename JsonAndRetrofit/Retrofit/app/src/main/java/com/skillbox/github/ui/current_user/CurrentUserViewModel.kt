package com.skillbox.github.ui.current_user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skillbox.github.data.RemoteUser
import com.skillbox.github.data.UserRepository

class CurrentUserViewModel : ViewModel() {
    private val repository = UserRepository()

    private var _users = MutableLiveData<RemoteUser>()
    val users: LiveData<RemoteUser>
        get() = _users

    private var _errorString = MutableLiveData<String>()
    val errorString: LiveData<String>
        get() = _errorString

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    fun updateCompany(newCompany: String) {
        _isLoading.postValue(true)
        repository.updateCompany(users.value!!.copy(company = newCompany), { user ->
            _users.postValue(user)
            _isLoading.postValue(false)
        }, { error ->
            _errorString.postValue(error.message)
            _isLoading.postValue(false)
        })
    }

    fun searchUserInfo() {
        _isLoading.postValue(true)
        repository.searchUserInfo({ user ->
            _users.postValue(user)
            _isLoading.postValue(false)
        }, { error ->
            _errorString.postValue(error.message)
            _isLoading.postValue(false)
        })
    }
}