package com.example.truecaller

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.provider.ContactsContract.CommonDataKinds.*

class ContactsManager private constructor(val context: Context) {

    private val CONTENT_URI: Uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI

    private val TAG = ContactsManager::class.java.simpleName
    companion object {
        @Volatile private var INSTANCE: ContactsManager? = null
        val READ_CONTACTS_PERMISSION: String = Manifest.permission.READ_CONTACTS
        fun getInstance(ctx: Context): ContactsManager {
            return INSTANCE ?: ContactsManager(ctx)
        }
    }

    @SuppressLint("Range")
    fun phoneLookup(phoneNumber: String): Contact? {
        val phoneProjection = arrayOf(
            Phone.DISPLAY_NAME,
            Phone.NUMBER,
            Phone._ID
        )
        val phoneCursor = context.contentResolver.query(
            CONTENT_URI,
            phoneProjection,
            "${Phone.NUMBER} = ?",
            arrayOf(phoneNumber),
            null
        )!!
        var phoneNumbers: ArrayList<String>? = null
        if (phoneCursor.moveToFirst()) {
//            val idIndex = phoneCursor.getColumnIndex(Phone._ID)
//            val nameIndex = phoneCursor.getColumnIndex(Phone.DISPLAY_NAME)

            val contactId = phoneCursor.getString(phoneCursor.getColumnIndex(Phone._ID))
            val contactName = phoneCursor.getString(phoneCursor.getColumnIndex(Phone.DISPLAY_NAME))

//            phoneNumbers = getPhoneNumbersForContact(contactId)
        }
        phoneCursor.close()
        return Contact("quido", phoneNumber)
    }

    fun search(phoneNumber: String): Contact? {
        val projection = arrayOf(
            Phone.DISPLAY_NAME,
            Phone.NUMBER
        )
        val selection = "${Phone.NUMBER} LIKE ?"
        val selectionArgs = arrayOf("%$phoneNumber%")

        val cursor: Cursor? = context.contentResolver.query(
            CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            null
        )

        var contactName: String? = null

        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                contactName = it.getString(nameIndex)
            }
        } ?: return null

        return Contact(contactName!!, phoneNumber)
    }

    private fun getPhoneNumbersForContact(contactId: String?): ArrayList<String> {
        val phoneNumbers: ArrayList<String> = arrayListOf()
        val projection = arrayOf(Phone.NUMBER)
        val selection = "${Phone.CONTACT_ID} = ?"

        val phone = context.contentResolver.query(CONTENT_URI, projection, selection, arrayOf(contactId), null)!!

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

    data class Contact(
        val phoneName: String?,
        val phoneNumber: String?
    ) {
        override fun toString() = "Contact(name = ${phoneName!!}, phone = ${phoneNumber!!})"
    }
}


