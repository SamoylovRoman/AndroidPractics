package com.android.practice.contentprovider.data.models

import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import com.android.practice.contentprovider.domain.repository.ContactsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ContactsRepositoryImpl(private val context: Context) : ContactsRepository {

    override suspend fun fetchAllContacts(): List<ContactDT> = withContext(Dispatchers.IO) {
        context.contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )?.use {
            getContactsFromCursor(it)
        }.orEmpty()
    }

    override suspend fun fetchContactDetails(id: Long): ContactDT {
        return ContactDT(1234567, "null", emptyList(), emptyList())
    }

    override suspend fun removeContact(id: Long): Boolean {
        return false
    }

    override suspend fun addContact(contact: ContactDT): Boolean {
        return false
    }

    private fun getContactsFromCursor(cursor: Cursor): List<ContactDT> {
        if (cursor.moveToFirst().not()) return emptyList()
        val contactList = mutableListOf<ContactDT>()
        do {
            val nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)
            val name = cursor.getString(nameIndex).orEmpty()
            val idIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID)
            val id = cursor.getLong(idIndex)
            contactList.add(
                ContactDT(
                    id = id,
                    name = name,
                    phoneNumbers = getPhonesForContact(contactId = id),
                    emails = emptyList()
                )
            )
        } while (cursor.moveToNext())
        return contactList
    }

    private fun getPhonesForContact(contactId: Long): List<String> {
        return context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
            arrayOf(contactId.toString()),
            null
        )?.use {
            getPhonesFromCursor(it)
        }.orEmpty()
    }

    private fun getPhonesFromCursor(cursor: Cursor): List<String> {
        if (cursor.moveToFirst().not()) return emptyList()
        val phonesList = mutableListOf<String>()
        do {
            val numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            val phone = cursor.getString(numberIndex)
            phonesList.add(phone)
        } while (cursor.moveToNext())
        return phonesList
    }

}