package com.example.intents

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

    fun setValidState(): FormState {
        return copy(
            valid = true, message = "You are logged in correctly",
            color = Color.GREEN
        )
    }

    fun setInvalidState(): FormState {
        return copy(
            valid = false, message = "Wrong pair e-mail and password",
            color = Color.RED
        )
    }
}
