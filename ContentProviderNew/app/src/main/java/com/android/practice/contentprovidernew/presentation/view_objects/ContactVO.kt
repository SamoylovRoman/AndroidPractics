package com.android.practice.contentprovidernew.presentation.view_objects

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ContactVO(
    val id: Long,
    val name: String,
    val phoneNumbers: List<String>,
    val emails: List<String> = emptyList()
): Parcelable