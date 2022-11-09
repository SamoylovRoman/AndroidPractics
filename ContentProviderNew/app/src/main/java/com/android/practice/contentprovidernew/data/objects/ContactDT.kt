package com.android.practice.contentprovidernew.data.objects

data class ContactDT(
    val id: Long,
    val name: String,
    val phoneNumbers: List<String>,
    val emails: List<String> = emptyList()
)