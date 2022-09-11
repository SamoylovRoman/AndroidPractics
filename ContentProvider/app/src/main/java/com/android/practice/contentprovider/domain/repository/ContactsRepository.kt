package com.android.practice.contentprovider.domain.repository

import com.android.practice.contentprovider.data.models.ContactDT

interface ContactsRepository {

    suspend fun fetchAllContacts(): List<ContactDT>

    suspend fun fetchContactDetails(id: Long): ContactDT

    suspend fun removeContact(id: Long): Boolean

    suspend fun addContact(contact: ContactDT): Boolean
}