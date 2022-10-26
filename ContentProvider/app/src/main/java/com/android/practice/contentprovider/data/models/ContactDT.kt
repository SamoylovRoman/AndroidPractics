package com.android.practice.contentprovider.data.models

data class ContactDT(
    val id: Long,
    val name: String?,
    val phoneNumbers: List<String>,
    val emails: List<String> = emptyList()
)
