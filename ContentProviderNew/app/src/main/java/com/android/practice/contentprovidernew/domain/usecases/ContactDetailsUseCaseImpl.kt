package com.android.practice.contentprovidernew.domain.usecases

import com.android.practice.contentprovidernew.domain.repository.ContactsRepository

class ContactDetailsUseCaseImpl(private val repository: ContactsRepository) :
    ContactDetailsUseCase {

    override suspend fun deleteCurrentContact(id: Long): Boolean {
        return repository.deleteContact(id)
    }
}