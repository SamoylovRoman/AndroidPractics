package com.example.fragments

import android.graphics.Color
import android.os.Parcelable
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class FormState(
    val valid: Boolean,
    @StringRes val message: Int,
    val color: Int
) : Parcelable {

    fun getValidState(): FormState = FormState(
        valid = true, message = R.string.text_logged_correctly,
        color = Color.GREEN
    )

    fun getInvalidState(): FormState = FormState(
        valid = false, message = R.string.text_logged_wrong,
        color = Color.RED
    )

    companion object {
        val EMPTY = FormState(
            valid = false,
            message = R.string.text_empty,
            color = Color.BLACK
        )
    }
}
