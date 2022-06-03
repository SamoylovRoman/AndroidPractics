package com.skillbox.github.ui.detail_repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skillbox.github.data.RemoteRepo
import com.skillbox.github.data.UserRepository

class DetailRepoViewModel : ViewModel() {
    private val repository = UserRepository()

    private var _repo = MutableLiveData<RemoteRepo>()
    val repo: LiveData<RemoteRepo>
        get() = _repo

    private var _repoIsStarred = MutableLiveData<Boolean>()
    val repoIsStarred: LiveData<Boolean>
        get() = _repoIsStarred

    private var _errorString = MutableLiveData<String>()
    val errorString: LiveData<String>
        get() = _errorString

    fun setRepoIsStarred(owner: String, repo: String) {
        repository.setRepoIsStarred(owner, repo, { isStarred ->
            _repoIsStarred.postValue(isStarred)
        }, { error ->
            _errorString.postValue(error.message)
        })
    }

    fun unsetRepoIsStarred(owner: String, repo: String) {
        repository.unsetRepoIsStarred(owner, repo, { isNotStarred ->
            _repoIsStarred.postValue(!isNotStarred)
        }, { error ->
            _errorString.postValue(error.message)
        })
    }

    fun checkRepoIsStarred(owner: String, repo: String) {
        repository.checkRepoIsStarred(owner, repo, { isStarred ->
            _repoIsStarred.postValue(isStarred)
        }, { error ->
            _errorString.postValue(error.message)
        })
    }
}