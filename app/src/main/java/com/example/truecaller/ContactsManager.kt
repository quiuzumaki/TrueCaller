package com.example.truecaller

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.provider.ContactsContract.CommonDataKinds.*

class ContactsManager: Activity() {

    private val READ_CONTACTS_PERMISSION: String = Manifest.permission.READ_CONTACTS
    private val CONTENT_URI: Uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI

    private val TAG = ContactsManager::class.java.simpleName

    companion object {
        val PHONE_NUMBER_ID: String = "phone_number"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val phoneNumber = getIntent().getStringExtra(PHONE_NUMBER_ID)
        Log.d(TAG, phoneNumber!!)
        Log.d(TAG, phoneLookup(phoneNumber)!!.toString())
    }

    fun phoneLookup(phoneNumber: String): Contact? {
        requestPermission()
        val phoneProjection = arrayOf(
            Phone.DISPLAY_NAME,
            Phone.NUMBER,
            Phone._ID
        )
        val phoneCursor = contentResolver?.query(
            CONTENT_URI,
            phoneProjection,
            "${Phone.NUMBER} = ?",
            arrayOf(phoneNumber),
            null
        )!!
        var phoneNumbers: ArrayList<String>? = null
        if (phoneCursor.moveToFirst()) {
            val idIndex = phoneCursor.getColumnIndex(Phone._ID)
            val nameIndex = phoneCursor.getColumnIndex(Phone.DISPLAY_NAME)

            val contactId = phoneCursor.getString(idIndex)
            val contactName = phoneCursor.getString(nameIndex)

            phoneNumbers = getPhoneNumbersForContact(contactId)
        }
        phoneCursor.close()
        return Contact("quido", phoneNumbers)
    }

    private fun getPhoneNumbersForContact(contactId: String?): ArrayList<String> {
        val phoneNumbers: ArrayList<String> = arrayListOf()
        val projection = arrayOf(Phone.NUMBER)
        val selection = "${Phone.CONTACT_ID} = ?"

        val phone = contentResolver?.query(CONTENT_URI, projection, selection, arrayOf(contactId), null)!!

        if (phone.moveToFirst()) {
            val phoneNumberIndex = phone.getColumnIndex(Phone.NUMBER)

            while (!phone.isAfterLast) {
                val number = phone.getString(phoneNumberIndex)
                phoneNumbers.add(number)
                phone.moveToNext()
            }
        }
        phone.close()
        return phoneNumbers
    }

    private fun requestPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                READ_CONTACTS_PERMISSION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(READ_CONTACTS_PERMISSION), 101)
        }
    }

    data class Contact(
        val phoneName: String,
        val phoneNumbers: ArrayList<String>?
    ) {
        override fun toString() = "Contanct(name = $phoneName, phone = [${phoneNumbers!!.joinToString { ", " }}])"
    }
}


