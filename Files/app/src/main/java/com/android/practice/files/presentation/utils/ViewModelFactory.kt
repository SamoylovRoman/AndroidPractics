package com.android.practice.files.presentation.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.practice.files.data.repository.DownloadRepositoryImpl
import com.android.practice.files.data.repository.storage.DataStoreStorageImpl
import com.android.practice.files.data.repository.storage.SharedPreferencesStorageImpl
import com.android.practice.files.domain.interactors.DownloadFiledUseCaseImpl
import com.android.practice.files.domain.interactors.DownloadStartFilesUseCaseImpl
import com.android.practice.files.presentation.view_models.DownloadViewModel

class ViewModelFactory(context: Context) : ViewModelProvider.Factory {

    private val storage by lazy(LazyThreadSafetyMode.NONE) {
        SharedPreferencesStorageImpl(context = context)
//        DataStoreStorageImpl(context = context)
    }

    private val downloadRepository by lazy(LazyThreadSafetyMode.NONE) {
        DownloadRepositoryImpl(context = context, storage = storage)
    }

    private val downloadFileUseCase by lazy(LazyThreadSafetyMode.NONE) {
        DownloadFiledUseCaseImpl(repository = downloadRepository)
    }

    private val downloadStartFilesUseCase by lazy(LazyThreadSafetyMode.NONE) {
        DownloadStartFilesUseCaseImpl(repository = downloadRepository)
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DownloadViewModel(
            downloadFileUseCase,
            downloadStartFilesUseCase
        ) as T
    }
}
