package com.android.practice.contentprovider.presentation.extensions

import android.app.Activity
import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.showToast(resId: Int) {
    Toast.makeText(requireContext(), getString(resId), Toast.LENGTH_LONG).show()
}

fun Activity.showToast(resId: Int) {
    Toast.makeText(applicationContext, getString(resId), Toast.LENGTH_LONG).show()
}