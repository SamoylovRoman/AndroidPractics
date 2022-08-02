package com.android.practice.files.domain.repository

interface DownloadRepository {

    suspend fun downloadFile(url: String): Boolean

    suspend fun downloadStartFiles()

    fun checkFileIsDownloaded(url: String): Boolean
}