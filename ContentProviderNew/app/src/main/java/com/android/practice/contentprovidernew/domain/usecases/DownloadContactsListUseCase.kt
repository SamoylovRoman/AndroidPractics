package com.android.practice.contentprovidernew.domain.usecases

import com.android.practice.contentprovidernew.presentation.view_objects.ContactVO

interface DownloadContactsListUseCase {

    suspend fun downloadContacts(): List<ContactVO>
}