package com.example.networking.data

import com.example.networking.R

data class RemoteMovie(
    val id: String,
    val title: String,
    val year: String,
    val type: String,
    val posterLink: String,
    val itemColor: Int = R.color.colorItemWhite
)
