package com.android.practice.contentprovider.domain.usecases

import com.android.practice.contentprovider.domain.repository.ContactsRepository

class SaveNewContactUseCaseImpl(private val repository: ContactsRepository) :
    SaveNewContactUseCase {

    override suspend fun saveNewContact(name: String, phone: String, email: String) {
        return repository.saveContact(name, phone, email)
    }
}