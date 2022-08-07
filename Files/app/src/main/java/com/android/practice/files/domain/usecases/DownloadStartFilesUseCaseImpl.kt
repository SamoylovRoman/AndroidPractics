package com.android.practice.files.domain.usecases

import com.android.practice.files.domain.repository.DownloadRepository

class DownloadStartFilesUseCaseImpl(private val repository: DownloadRepository) :
    DownloadStartFilesUseCase {
    override suspend fun downloadStartFiles() {
        if (repository.checkFirstStart()) {
            repository.downloadStartFiles()
        }
    }
}