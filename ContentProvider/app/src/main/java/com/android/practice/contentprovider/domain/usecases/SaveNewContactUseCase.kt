package com.android.practice.contentprovider.domain.usecases

interface SaveNewContactUseCase {

    suspend fun saveNewContact(name: String, phone: String, email: String)
}