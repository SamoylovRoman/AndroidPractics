package com.android.practice.contentprovider.presentation.view_objects

data class ContactDetailVO(
    val id: Long,
    val name: String?,
    val phones: List<String>,
    val emails: List<String>?
)
