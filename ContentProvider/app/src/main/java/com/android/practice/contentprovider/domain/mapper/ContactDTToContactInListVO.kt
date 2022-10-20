package com.android.practice.contentprovider.domain.mapper

import com.android.practice.contentprovider.data.models.ContactDT
import com.android.practice.contentprovider.presentation.view_objects.ContactInListVO

fun ContactDT.toContactInListVO(): ContactInListVO =
    ContactInListVO(
        id = id,
        name = name,
        phonesString = phoneNumbers.joinToString("\n")
    )