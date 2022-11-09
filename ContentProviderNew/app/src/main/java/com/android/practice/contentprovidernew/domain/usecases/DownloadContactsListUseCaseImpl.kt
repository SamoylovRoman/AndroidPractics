package com.android.practice.contentprovidernew.domain.usecases

import com.android.practice.contentprovidernew.domain.mappers.toVO
import com.android.practice.contentprovidernew.domain.repository.ContactsRepository
import com.android.practice.contentprovidernew.presentation.view_objects.ContactVO

class DownloadContactsListUseCaseImpl(private val repository: ContactsRepository) :
    DownloadContactsListUseCase {

    override suspend fun downloadContacts(): List<ContactVO> {
        return repository.getAllContacts().map { contactDT ->
            contactDT.toVO()
        }
    }
}