package com.android.practice.files.data.repository.storage

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SharedPreferencesStorageImpl(context: Context) : Storage {

    private val sharedPrefs = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)

    override suspend fun saveFirstStartFlag() =
        withContext(Dispatchers.IO) {
            sharedPrefs.edit().putBoolean(FIRST_APP_START_KEY, false).apply()
        }


    override suspend fun checkFirstStart(): Boolean = withContext(Dispatchers.IO) {
        sharedPrefs.getBoolean(FIRST_APP_START_KEY, true)
    }

    override suspend fun checkFileIsDownloaded(url: String): Boolean = withContext(Dispatchers.IO) {
        sharedPrefs.getString(url, null) != null
    }

    override suspend fun saveFileInfo(url: String, fileName: String) {
        sharedPrefs.edit()
            .putString(url, fileName)
            .apply()
    }

    companion object {
        private const val SHARED_PREFS_NAME = "Shared_prefs_name"
        private const val FIRST_APP_START_KEY = "first_start"
    }
}