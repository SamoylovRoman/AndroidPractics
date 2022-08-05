package com.android.practice.files.domain.interactors

import com.android.practice.files.domain.repository.DownloadRepository
import com.android.practice.files.domain.usecases.DownloadStartFilesUseCase

class DownloadStartFilesUseCaseImpl(private val repository: DownloadRepository) :
    DownloadStartFilesUseCase {
    override suspend fun downloadStartFiles() {
        if (repository.checkFirstStart()) {
            repository.downloadStartFiles()
        }
    }
}