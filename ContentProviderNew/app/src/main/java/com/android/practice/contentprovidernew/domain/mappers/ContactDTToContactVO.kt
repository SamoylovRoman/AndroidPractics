package com.android.practice.contentprovidernew.domain.mappers

import com.android.practice.contentprovidernew.data.objects.ContactDT
import com.android.practice.contentprovidernew.presentation.view_objects.ContactVO

fun ContactDT.toVO() = ContactVO(
    id = id,
    name = name,
    phoneNumbers = phoneNumbers,
    emails = emails
    )