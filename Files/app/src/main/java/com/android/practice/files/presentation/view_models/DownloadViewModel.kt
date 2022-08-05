package com.android.practice.files.presentation.view_models

import androidx.lifecycle.*
import com.android.practice.files.domain.usecases.DownloadFileUseCase
import com.android.practice.files.domain.usecases.DownloadStartFilesUseCase
import com.android.practice.files.presentation.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class DownloadViewModel(
    private val downloadFileUseCase: DownloadFileUseCase,
    private val downloadStartFilesUseCase: DownloadStartFilesUseCase
) : ViewModel() {

    private var _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _showToast = SingleLiveEvent<Unit>()
    val showToast: LiveData<Unit>
        get() = _showToast

    fun loadFile(url: String) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            if (!downloadFileUseCase.checkFileIsDownloaded(url)) {
                downloadFileUseCase.downloadFile(url)
            } else {
                _showToast.postValue(Unit)
            }
            _isLoading.postValue(false)
        }
    }

    fun downloadStartFiles() {
        viewModelScope.launch {
            _isLoading.postValue(true)
            downloadStartFilesUseCase.downloadStartFiles()
            _isLoading.postValue(false)
        }
    }
}