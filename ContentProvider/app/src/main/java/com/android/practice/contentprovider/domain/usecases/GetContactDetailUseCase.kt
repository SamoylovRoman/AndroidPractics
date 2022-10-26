package com.android.practice.contentprovider.domain.usecases

import com.android.practice.contentprovider.presentation.view_objects.ContactDetailVO

interface GetContactDetailUseCase {

    suspend fun getContactDetail(id: Long): ContactDetailVO
}