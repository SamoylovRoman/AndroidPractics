package com.skillbox.github.ui.repository_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skillbox.github.data.RemoteRepo
import com.skillbox.github.data.UserRepository

class RepositoryListViewModel : ViewModel() {
    private val repository = UserRepository()

    private var _repositoriesList = MutableLiveData(emptyList<RemoteRepo>())
    val repositoriesList: LiveData<List<RemoteRepo>>
        get() = _repositoriesList

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading
    private var _errorString = MutableLiveData<String>()
    val errorString: LiveData<String>
        get() = _errorString


    fun searchRepositories() {
        _isLoading.postValue(true)
        repository.searchRepositories({ repos ->
            _isLoading.postValue(false)
            _repositoriesList.postValue(repos)
        }, { error ->
            _isLoading.postValue(false)
            _errorString.postValue(error.message)
        })

    }
}