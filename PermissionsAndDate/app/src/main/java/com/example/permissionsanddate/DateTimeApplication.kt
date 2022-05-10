package com.example.permissionsanddate

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen

class DateTimeApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }
}