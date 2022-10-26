package com.android.practice.contentprovider.domain.mapper

import com.android.practice.contentprovider.data.models.ContactDT
import com.android.practice.contentprovider.presentation.view_objects.ContactDetailVO

fun ContactDT.toContactDetailVO(): ContactDetailVO =
    ContactDetailVO(
        id = id,
        name = name,
        phones = phoneNumbers,
        emails = emails
    )