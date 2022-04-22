package com.example.recyclerviewlist

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


sealed class Staff : Parcelable {

    @Parcelize
    data class Employee(
        val fullName: String,
        val position: String,
        val isManagementTeam: Boolean,
        val phoneNumber: String,
        val photoLink: String
    ) : Staff()

    @Parcelize
    data class Manager(
        val fullName: String,
        val position: String,
        val isManagementTeam: Boolean,
        val phoneNumber: String,
        val emailAddress: String,
        val photoLink: String
    ) : Staff()
}