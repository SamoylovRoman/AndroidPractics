package com.example.permissionsanddate

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

fun Context?.showToast(@StringRes resId: Int) {
    Toast.makeText(this, this?.resources?.getText(resId), Toast.LENGTH_SHORT).show()
}