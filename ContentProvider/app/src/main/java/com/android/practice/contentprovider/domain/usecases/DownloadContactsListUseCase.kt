package com.android.practice.contentprovider.domain.usecases

import com.android.practice.contentprovider.presentation.view_objects.ContactInListVO

interface DownloadContactsListUseCase {

    suspend fun downloadContacts(): List<ContactInListVO>
}