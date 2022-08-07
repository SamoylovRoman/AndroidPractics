package com.android.practice.files.data.repository.storage

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.firstOrNull

class DataStoreStorageImpl(private val context: Context) : Storage {

    private val Context.dataStore by preferencesDataStore(name = DATASTORE_NAME)

    override suspend fun saveFirstStartFlag() {
        context.dataStore.edit {
            it[stringPreferencesKey(FIRST_APP_START_KEY)] = false.toString()
        }
    }

    override suspend fun checkFirstStart(): Boolean {
        val isFirstStart = context.dataStore.data
            .firstOrNull()?.get(stringPreferencesKey(FIRST_APP_START_KEY))
        return isFirstStart == null
    }

    override suspend fun checkFileIsDownloaded(url: String): Boolean {
        val fileName = context.dataStore.data
            .firstOrNull()?.get(stringPreferencesKey(url))
        return fileName != null
    }

    override suspend fun saveFileInfo(url: String, fileName: String) {
        context.dataStore.edit {
            it[stringPreferencesKey(url)] = fileName
        }
    }

    companion object {
        private const val DATASTORE_NAME = "data_store_name"
        private const val FIRST_APP_START_KEY = "first_start"
    }
}