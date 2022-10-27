package com.android.practice.contentprovider.domain.usecases

import com.android.practice.contentprovider.domain.mapper.toContactDetailVO
import com.android.practice.contentprovider.domain.repository.ContactsRepository
import com.android.practice.contentprovider.presentation.view_objects.ContactDetailVO

class GetContactDetailUseCaseImpl(private val repository: ContactsRepository) :
    GetContactDetailUseCase {
    override suspend fun getContactDetail(id: Long): ContactDetailVO {
        return repository.fetchContactDetails(id).toContactDetailVO()
    }

    override suspend fun getEmailsStringByContactId(contactId: Long): String {
        return repository.getEmailsStringByContactId(contactId)
    }
}