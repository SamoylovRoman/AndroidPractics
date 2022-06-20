package com.skillbox.github.ui.repository_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skillbox.github.data.RemoteRepo
import com.skillbox.github.data.UserRepository
import kotlinx.coroutines.*

class RepositoryListViewModel : ViewModel() {
    private val repository = UserRepository()

    private var repositoryViewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

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
        repositoryViewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val newReposList = repository.searchRepositories()
                _repositoriesList.postValue(newReposList)
            } catch (t: Throwable) {
                _repositoriesList.postValue(emptyList())
                _errorString.postValue(t.message)
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        repositoryViewModelScope.coroutineContext.cancelChildren()
    }
}