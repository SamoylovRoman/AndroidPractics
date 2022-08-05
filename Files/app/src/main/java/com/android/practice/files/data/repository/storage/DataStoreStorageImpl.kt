package com.android.practice.files.data.repository.storage

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class DataStoreStorageImpl(private val context: Context) : Storage {

    private val Context.dataStore by preferencesDataStore(name = DATASTORE_NAME)

    override suspend fun saveFirstStartFlag() {
        context.dataStore.edit {
            it[KEY] = FIRST_APP_START_KEY
        }
    }

    override suspend fun checkFirstStart(): Boolean {
        return context.dataStore.data
            .map {
                it[KEY]
            }.first().toBoolean()
    }

    override suspend fun checkFileIsDownloaded(url: String): Boolean {
        return context.dataStore.data
            .map {
                it[stringPreferencesKey(url)]
            }.first().toBoolean()
    }

    override suspend fun saveFileInfo(url: String, fileName: String) {
        context.dataStore.edit {
            it[stringPreferencesKey(url)] = fileName
        }
    }

    companion object {
        private const val DATASTORE_NAME = "data_store_name"
        private val KEY = stringPreferencesKey("key")
        private const val FIRST_APP_START_KEY = "first_start"
    }
}