package com.android.practice.contentprovider.domain.repository

import com.android.practice.contentprovider.data.models.ContactDT

interface ContactsRepository {

    suspend fun fetchAllContacts(): List<ContactDT>

    suspend fun fetchContactDetails(id: Long): ContactDT

    suspend fun removeContact(id: Long): Boolean

    suspend fun getEmailsStringByContactId(contactId: Long): String

    suspend fun saveContact(
        name: String,
        phones: List<String>,
        emails: List<String> = emptyList()
    )
}