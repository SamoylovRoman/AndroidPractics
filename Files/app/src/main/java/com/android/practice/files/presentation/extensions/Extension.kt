package com.android.practice.files.presentation.extensions

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

fun Fragment.showToast(@StringRes resId: Int) {
    Toast.makeText(requireContext(), getString(resId), Toast.LENGTH_LONG).show()
}