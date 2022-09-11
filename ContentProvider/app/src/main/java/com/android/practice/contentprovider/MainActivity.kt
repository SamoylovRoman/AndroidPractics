package com.android.practice.contentprovider

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.practice.contentprovider.presentation.contacts_list.ContactsListFragment

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }



    private fun showContactsListFragment() {
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragmentContainer,
                ContactsListFragment.newInstance(),
                ContactsListFragment::class.java.simpleName
            )
            .commit()
    }
}