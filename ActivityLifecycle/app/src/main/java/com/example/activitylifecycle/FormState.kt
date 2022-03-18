package com.example.activitylifecycle

import android.graphics.Color
import android.graphics.Color.BLACK
import android.os.Parcel
import android.os.Parcelable
import androidx.core.graphics.toColorInt
import kotlinx.parcelize.Parcelize


@Parcelize
data class FormState(
    val valid: Boolean,
    val message: String,
    val color: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readByte() != 0.toByte(),
        parcel.readString().toString(),
        parcel.readString()!!.toInt()
    ) {
    }

    fun setValid(): FormState {
        return copy(
            valid = true, message = "login and password are correct!",
            color = Color.GREEN
        )
    }

    fun setNotValid(): FormState {
        return copy(
            valid = false, message = "login or password is wrong!",
            color = Color.RED
        )
    }
}
