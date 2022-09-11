package com.android.practice.contentprovider.presentation.extensions

import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.showToast(resId: Int) {
    Toast.makeText(requireContext(), getString(resId), Toast.LENGTH_LONG).show()
}