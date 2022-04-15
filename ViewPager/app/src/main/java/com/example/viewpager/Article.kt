package com.example.viewpager

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Article(
    @StringRes val title: Int,
    @DrawableRes val picture: Int,
    @StringRes val text: Int,
    val tags: List<ArticleTag>
)
