package com.android.practice.contentprovidernew.domain.usecases

interface SaveNewContactUseCase {

    suspend fun saveNewContact(name: String, phones: List<String>, emails: List<String>)
}