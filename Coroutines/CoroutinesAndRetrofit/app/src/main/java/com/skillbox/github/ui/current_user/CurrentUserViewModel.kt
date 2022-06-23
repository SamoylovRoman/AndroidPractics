package com.skillbox.github.ui.current_user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skillbox.github.data.RemoteFollowing
import com.skillbox.github.data.RemoteUser
import com.skillbox.github.data.UserRepository
import kotlinx.coroutines.*

class CurrentUserViewModel : ViewModel() {
    private val repository = UserRepository()

    private var _userInfo = MutableLiveData<RemoteUser>()
    val userInfo: LiveData<RemoteUser>
        get() = _userInfo

    private var _errorString = MutableLiveData<String>()
    val errorString: LiveData<String>
        get() = _errorString

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private var _followings = MutableLiveData(emptyList<RemoteFollowing>())
    val followings: LiveData<List<RemoteFollowing>>
        get() = _followings

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _errorString.postValue(throwable.message)
        _isLoading.postValue(false)
    }

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main + exceptionHandler)

    fun updateCompany(newCompany: String) {
        _isLoading.postValue(true)
        repository.updateCompany(userInfo.value!!.copy(company = newCompany), { user ->
            _userInfo.postValue(user)
            _isLoading.postValue(false)
        }, { error ->
            _errorString.postValue(error.message)
            _isLoading.postValue(false)
        })
    }

    fun searchUserInfo() {
        _isLoading.postValue(true)
        scope.launch {
            val userInfo = withContext(Dispatchers.Default) {
                repository.searchUserInfo()
            }
            val followingList =
                withContext(Dispatchers.Default) {
                    repository.searchUsersFollowings()
                }
            _userInfo.postValue(userInfo)
            _followings.postValue(followingList)
            _isLoading.postValue(false)
        }
    }

    override fun onCleared() {
        super.onCleared()
        scope.coroutineContext.cancelChildren()
    }
}