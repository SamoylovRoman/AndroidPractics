package com.android.practice.contentprovidernew.domain.repository

import com.android.practice.contentprovidernew.data.objects.ContactDT

interface ContactsRepository {

    suspend fun getAllContacts(): List<ContactDT>

    suspend fun fetchContactDetails(id: Long): ContactDT

    suspend fun deleteContact(id: Long): Boolean

    suspend fun saveContact(
        name: String,
        phones: List<String>,
        emails: List<String> = emptyList()
    )
}