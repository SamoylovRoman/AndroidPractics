package com.android.practice.files.data.repository

import android.content.Context
import android.os.Environment
import android.util.Log
import com.android.practice.files.data.network.Networking
import com.android.practice.files.domain.repository.DownloadRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class DownloadRepositoryImpl(private val context: Context) : DownloadRepository {

    private val sharedPrefs = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
    private val filesFolder = context.getExternalFilesDir(FOLDER_NAME)

    // download file in external storage
    override suspend fun downloadFile(url: String): Boolean = withContext(Dispatchers.IO) {
        if (!filesFolder?.exists()!!) {
            filesFolder.mkdirs()
        }
        val fileName = "${System.currentTimeMillis()}_${url.substringAfterLast("/")}"
        val file = File(filesFolder, fileName)
        if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
            return@withContext false
        }
        try {
            Networking.api.getFile(url)
                .byteStream()
                .use { inputStream ->
                    inputStream.copyTo(file.outputStream())
                }
            saveFileInfo(url, fileName)
        } catch (t: Throwable) {
            Log.e("Try to load file: ", "${t.message}")
            file.delete()
            return@withContext false
        }
        return@withContext true
    }

    // download all start files with first start
    override suspend fun downloadStartFiles() {
        try {
            withContext(Dispatchers.IO) {
                context.resources.assets.open(ASSETS_URLS_FILE_NAME)
                    .bufferedReader()
                    .use { bufReader ->
                        bufReader.readLines().forEach { url ->
                            downloadFile(url = url)
                        }
                    }
            }
            sharedPrefs.edit().putBoolean(FIRST_START, false).apply()
        } catch (t: Throwable) {
            Log.e("Try to download start files: ", "${t.message}")
        }
        return
    }

    // check if the first application start
    override fun checkFirstStart(): Boolean {
        return sharedPrefs.getBoolean(FIRST_START, true)
    }

    // check if file is already loaded
    override fun checkFileIsDownloaded(url: String): Boolean {
        return sharedPrefs.getString(url, null) != null
    }

    // save file info in shared preferences
    private fun saveFileInfo(url: String, fileName: String) {
        sharedPrefs.edit()
            .putString(url, fileName)
            .apply()
    }

    companion object {
        private const val SHARED_PREFS_NAME = "Shared_prefs_name"
        private const val FOLDER_NAME = "files_folder"
        private const val FIRST_START = "first_start"
        private const val ASSETS_URLS_FILE_NAME = "urls.txt"
    }
}