package com.android.practice.files.data.repository

import android.content.Context
import java.io.InputStream

class OpenAssetsFileUtils {
    fun openAssetsFile(context: Context, url: String): InputStream {
        return context.resources.assets.open(url)
    }
}