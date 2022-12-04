package com.android.practice.contentprovidernew.data.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import android.util.Log
import com.android.practice.contentprovidernew.data.objects.ContactDT
import com.android.practice.contentprovidernew.domain.repository.ContactsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.regex.Pattern

class ContactsRepositoryImpl(private val context: Context) : ContactsRepository {

    private val phonePattern = Pattern.compile("^\\+?[0-9]{3}?[0-9]{6,12}\$")

    override suspend fun getAllContacts(): List<ContactDT> = withContext(Dispatchers.IO) {
        context.contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            null
        )?.use {
            getContactsFromCursor(it)
        }.orEmpty()
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
                    emails = getEmailsForContact(contactId = id)
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

    override suspend fun fetchContactDetails(id: Long): ContactDT = withContext(Dispatchers.IO) {
        val nameIndex: Int
        var name = ""
        context.contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            ContactsContract.Contacts._ID + " = ?",
            arrayOf("$id"),
            null
        )?.use {
            it.moveToFirst()
            Log.d("count of rows = ", "${it.count}")
            it.columnNames.forEach { str ->
                Log.d("columnNames = ", str)
            }
            nameIndex = it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)
            Log.d("nameIndex = ", "$nameIndex")
            name = it.getString(nameIndex).orEmpty()
            Log.d("ContactDetail: name = ", "$nameIndex")
        }
        ContactDT(id = id, name = name, getPhonesForContact(id), getEmailsForContact(id))
    }

    override suspend fun deleteContact(id: Long): Boolean = withContext(Dispatchers.IO) {
        val contactUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, id.toString())
        return@withContext context.contentResolver.delete(contactUri, null, null) == 1
    }

    override suspend fun saveContact(name: String, phones: List<String>, emails: List<String>) =
        withContext(Dispatchers.IO) {
            if (name.isBlank() || !phones.all {
                    phonePattern.matcher(it).matches()
                }) {
                throw Exception("Incorrect data! Check it!")
            }
            val contactId = saveRawContact()
            saveContactName(contactId, name)
            saveContactPhones(contactId, phones)
            if (emails.isNotEmpty()) {
                saveContactEmails(contactId, emails)
            }
        }

    private fun saveRawContact(): Long {
        val uri = context.contentResolver.insert(
            ContactsContract.RawContacts.CONTENT_URI,
            ContentValues()
        )
        Log.d("saveRawContact): ", "uri = $uri")
        return uri?.lastPathSegment?.toLongOrNull() ?: error("Cannot save contact")
    }

    private fun saveContactName(contactId: Long, name: String) {
        val contentValues = ContentValues().apply {
            put(ContactsContract.Data.RAW_CONTACT_ID, contactId)
            put(
                ContactsContract.Data.MIMETYPE,
                ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE
            )
            put(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name)
        }
        context.contentResolver.insert(ContactsContract.Data.CONTENT_URI, contentValues)
    }

    private fun saveContactPhones(contactId: Long, phones: List<String>) {
        val contentValues = ContentValues().apply {
            put(ContactsContract.Data.RAW_CONTACT_ID, contactId)
            put(
                ContactsContract.Data.MIMETYPE,
                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
            )
            for (number in phones) {
                put(ContactsContract.CommonDataKinds.Phone.NUMBER, number)
            }
        }
        context.contentResolver.insert(ContactsContract.Data.CONTENT_URI, contentValues)
    }

    private fun saveContactEmails(contactId: Long, emails: List<String>) {
        val contentValues = ContentValues().apply {
            put(ContactsContract.Data.RAW_CONTACT_ID, contactId)
            put(
                ContactsContract.Data.MIMETYPE,
                ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE
            )
            for (email in emails) {
                put(ContactsContract.CommonDataKinds.Email.ADDRESS, email)
            }
        }
        context.contentResolver.insert(ContactsContract.Data.CONTENT_URI, contentValues)
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

    private fun getEmailsForContact(contactId: Long): List<String> {
        return context.contentResolver.query(
            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
            null,
            "${ContactsContract.CommonDataKinds.Email.CONTACT_ID} = ?",
            arrayOf(contactId.toString()),
            null
        )?.use {
            getEmailsFromCursor(it)
        }.orEmpty()
    }

    private fun getEmailsFromCursor(cursor: Cursor): List<String> {
        if (cursor.moveToFirst().not()) return emptyList()
        val emailsList = mutableListOf<String>()
        do {
            val numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS)
            val phone = cursor.getString(numberIndex)
            emailsList.add(phone)
        } while (cursor.moveToNext())
        return emailsList
    }
}