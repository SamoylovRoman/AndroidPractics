package com.android.practice.contentprovidernew

import android.app.Application
import android.os.StrictMode
import android.util.Log
import androidx.viewbinding.BuildConfig

class ContentProviderApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Log.d("APP", "OnCreate")
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()
                    .penaltyDeath()
                    .build()
            )
        }
    }
}