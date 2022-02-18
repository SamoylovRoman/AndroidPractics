package com.example.buildtypesandflavours

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.buildtypesandflavours.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textInformation.text = """
    Flavor = ${BuildConfig.FLAVOR}
    Build type = ${BuildConfig.BUILD_TYPE}
    Version code = ${BuildConfig.VERSION_CODE}
    Version name = ${BuildConfig.VERSION_NAME}
""".trimIndent()
    }
}