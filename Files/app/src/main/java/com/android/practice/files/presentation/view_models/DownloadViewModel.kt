package com.android.practice.files.presentation.view_models

import android.app.Application
import androidx.lifecycle.*
import com.android.practice.files.data.repository.DownloadRepositoryImpl
import com.android.practice.files.presentation.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class DownloadViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = DownloadRepositoryImpl(application)

    private var _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _showToast = SingleLiveEvent<Unit>()
    val showToast: LiveData<Unit>
        get() = _showToast

    fun loadFile(url: String) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            if (!repository.checkFileIsDownloaded(url)) {
                repository.downloadFile(url)
            } else {
                _showToast.postValue(Unit)
            }
            _isLoading.postValue(false)
        }
    }

    fun downloadStartFiles() {
        viewModelScope.launch {
            _isLoading.postValue(true)
            repository.downloadStartFiles()
            _isLoading.postValue(false)
        }
    }
}