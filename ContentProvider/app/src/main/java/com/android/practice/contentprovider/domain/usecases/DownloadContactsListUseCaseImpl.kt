package com.android.practice.contentprovider.domain.usecases

import com.android.practice.contentprovider.domain.mapper.toContactInListVO
import com.android.practice.contentprovider.domain.repository.ContactsRepository
import com.android.practice.contentprovider.presentation.view_objects.ContactInListVO

class DownloadContactsListUseCaseImpl(private val repository: ContactsRepository) : DownloadContactsListUseCase {

    override suspend fun downloadContacts(): List<ContactInListVO> {
        return repository.fetchAllContacts().map { contactDT ->
            contactDT.toContactInListVO()
        }
    }
}