package com.android.practice.contentprovider

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.practice.contentprovider.presentation.contacts_list.ContactsListFragment
import com.android.practice.contentprovider.presentation.extensions.showToast
import permissions.dispatcher.PermissionRequest
import permissions.dispatcher.ktx.PermissionsRequester
import permissions.dispatcher.ktx.constructPermissionsRequest

class MainActivity : AppCompatActivity() {

//    private lateinit var showContacts: PermissionsRequester

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showContactsListFragment()
//        showContacts = constructPermissionsRequest(
//            android.Manifest.permission.READ_CONTACTS,
//            onShowRationale = ::onContactsPermissionShowRationale,
//            onPermissionDenied = ::onContactPermissionDenied,
//            onNeverAskAgain = ::onContactPermissionNeverAskAgain,
//            requiresPermission = {
//                showContactsListFragment()
//            }
//        )

    }

    override fun onResume() {
        super.onResume()
//        showContacts.launch()
    }

    private fun onContactsPermissionShowRationale(request: PermissionRequest) {
        request.proceed()
    }

    private fun onContactPermissionDenied() {
        showToast(R.string.reading_is_prohibited)
    }

    private fun onContactPermissionNeverAskAgain() {
        showToast(R.string.allow_contacts_reading)
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