package com.android.practice.contentprovider.data.models

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import android.util.Log
import com.android.practice.contentprovider.domain.repository.ContactsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.regex.Pattern

class ContactsRepositoryImpl(private val context: Context) : ContactsRepository {

    private val phonePattern = Pattern.compile("^\\+?[0-9]{3}?[0-9]{6,12}\$")
    private val emailPattern = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )

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

    override suspend fun saveContact(name: String, phone: String, email: String) =
        withContext(Dispatchers.IO) {
            if (name.isBlank() || !phonePattern.matcher(phone).matches() || (!emailPattern.matcher(
                    email
                ).matches() && email.isNotBlank())
            ) {
                throw Exception("Incorrect data! Check it!")
            }
            val uri = saveRawContact()
        }

    private fun saveRawContact(): Long {
        val uri = context.contentResolver.insert(
            ContactsContract.RawContacts.CONTENT_URI,
            ContentValues()
        )
        Log.d("MyTag (saveRawContact): ", "uri = $uri")
        return uri?.lastPathSegment?.toLongOrNull() ?: error("Cannot save contact")
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