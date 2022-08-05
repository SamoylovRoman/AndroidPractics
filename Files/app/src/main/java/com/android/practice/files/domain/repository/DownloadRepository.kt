package com.android.practice.files.domain.repository

interface DownloadRepository {

    suspend fun downloadFile(url: String): Boolean

    suspend fun downloadStartFiles()

    suspend fun checkFileIsDownloaded(url: String): Boolean

    suspend fun checkFirstStart(): Boolean
}