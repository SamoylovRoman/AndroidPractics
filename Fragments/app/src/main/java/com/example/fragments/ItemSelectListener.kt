package com.example.fragments

import android.graphics.Bitmap

interface ItemSelectListener {
    fun onItemSelected(imageBitmap: Bitmap, fullName: String, descriptionText: String)
}