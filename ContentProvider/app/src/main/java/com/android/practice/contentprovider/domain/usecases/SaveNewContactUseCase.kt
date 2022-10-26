package com.android.practice.contentprovider.domain.usecases

interface SaveNewContactUseCase {

    suspend fun saveNewContact(name: String, phones: List<String>, emails: List<String>)
}