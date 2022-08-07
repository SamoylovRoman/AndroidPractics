package com.android.practice.files.data.repository

import android.content.Context
import android.os.Environment
import android.util.Log
import com.android.practice.files.data.network.Networking
import com.android.practice.files.data.repository.storage.Storage
import com.android.practice.files.domain.repository.DownloadRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class DownloadRepositoryImpl(private val context: Context, private val storage: Storage) :
    DownloadRepository {

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
                OpenAssetsFileUtils().openAssetsFile(context, ASSETS_URLS_FILE_NAME)
                    .bufferedReader()
                    .use { bufReader ->
                        bufReader.readLines().forEach { url ->
                            downloadFile(url = url)
                        }
                    }
            }
            storage.saveFirstStartFlag()
        } catch (t: Throwable) {
            Log.e("Try to download start files: ", "${t.message}")
        }
        return
    }

    // check if the first application start
    override suspend fun checkFirstStart(): Boolean {
        return storage.checkFirstStart()
    }

    // check if file is already loaded
    override suspend fun checkFileIsDownloaded(url: String): Boolean {
        return storage.checkFileIsDownloaded(url = url)
    }

    // save file info in shared preferences
    private suspend fun saveFileInfo(url: String, fileName: String) {
        storage.saveFileInfo(url = url, fileName = fileName)
    }

    companion object {
        private const val FOLDER_NAME = "files_folder"
        private const val ASSETS_URLS_FILE_NAME = "urls.txt"
    }
}