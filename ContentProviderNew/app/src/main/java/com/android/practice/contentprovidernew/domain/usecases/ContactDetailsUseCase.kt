package com.android.practice.contentprovidernew.domain.usecases

interface ContactDetailsUseCase {

    suspend fun deleteCurrentContact(id: Long): Boolean
}