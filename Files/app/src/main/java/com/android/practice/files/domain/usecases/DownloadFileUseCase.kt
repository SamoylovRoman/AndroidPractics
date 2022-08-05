package com.android.practice.files.domain.usecases

interface DownloadFileUseCase {

    suspend fun downloadFile(url: String): Boolean

    suspend fun checkFileIsDownloaded(url: String): Boolean
}