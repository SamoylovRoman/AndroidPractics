package com.android.practice.files.domain.interactors

import com.android.practice.files.domain.repository.DownloadRepository
import com.android.practice.files.domain.usecases.DownloadFileUseCase

class DownloadFiledUseCaseImpl(private val repository: DownloadRepository) : DownloadFileUseCase {

    override suspend fun downloadFile(url: String): Boolean {
        return repository.downloadFile(url)
    }

    override suspend fun checkFileIsDownloaded(url: String): Boolean {
        return repository.checkFileIsDownloaded(url)
    }
}