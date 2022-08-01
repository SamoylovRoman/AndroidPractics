package com.android.practice.files

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.practice.files.presentation.view.DownloadFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragmentContainer,
                DownloadFragment.newInstance(),
                DownloadFragment::class.java.simpleName
            )
            .commit()
    }
}