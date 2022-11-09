package com.android.practice.contentprovidernew.domain.usecases

import com.android.practice.contentprovidernew.domain.repository.ContactsRepository

class SaveNewContactUseCaseImpl(private val repository: ContactsRepository) :
    SaveNewContactUseCase {

    override suspend fun saveNewContact(name: String, phones: List<String>, emails: List<String>) {
        return repository.saveContact(name, phones, emails)
    }
}