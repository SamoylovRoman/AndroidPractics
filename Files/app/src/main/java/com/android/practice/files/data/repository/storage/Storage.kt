package com.android.practice.files.data.repository.storage

interface Storage {

    suspend fun saveFirstStartFlag()

    suspend fun checkFirstStart(): Boolean

    suspend fun checkFileIsDownloaded(url: String): Boolean

    suspend fun saveFileInfo(url: String, fileName: String)
}