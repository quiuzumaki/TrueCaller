package com.example.truecaller

import android.Manifest
import android.app.Activity
import android.app.role.RoleManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.telecom.PhoneAccountHandle
import android.telecom.TelecomManager
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.truecaller.defaultcall.CallerDeflectorActivity
import com.example.truecaller.screencallapp.DialerActivity
import com.example.truecaller.screencallapp.CallerScreeningService
import com.example.truecaller.telecom.call.TelecomCallActivity
import com.example.truecaller.telecom.call.TelecomCallService
import com.example.truecaller.ui.theme.TrueCallerTheme

class MainActivity : ComponentActivity() {
    private val TAG: String = MainActivity::class.java.simpleName
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkPermission()
        setContent {
            TrueCallerTheme {
                MainScreen(onClick = {
                    Toast.makeText(this, "Start", Toast.LENGTH_LONG).show()
                    startActivity(
                        Intent(this, CallerDeflectorActivity::class.java)
                    )
                })
//                TelecomCallSample()
            }
        }
    }
    private fun requestAllPermisstions() {
        Log.d(TAG, "request all permission")
        val permissions = listOf(
            Manifest.permission.READ_PHONE_NUMBERS,
            Manifest.permission.MANAGE_OWN_CALLS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_CONTACTS
        )
        permissions.forEach {
            Log.d(TAG, "Request $it")

            if (ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Requested $it")
                ActivityCompat.requestPermissions(this, arrayOf(it), 101)
            }
        }
    }

    fun main() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            val roleManager = getSystemService(Context.ROLE_SERVICE) as RoleManager
            // Check if the app needs to register call redirection role.
            val shouldRequestRole = roleManager.isRoleAvailable(RoleManager.ROLE_CALL_REDIRECTION) &&
                    !roleManager.isRoleHeld(RoleManager.ROLE_CALL_REDIRECTION)
            if (shouldRequestRole) {
                val intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_CALL_REDIRECTION)
                startActivityForResult(intent, REDIRECT_ROLE_REQUEST_CODE)
            }
        }
    }

    fun CallActivity() {
        val intent = Intent(Intent.ACTION_CALL)
        intent.apply {
            setData(Uri.parse("tel:0123456789"))
        }
        checkPermission(Manifest.permission.CALL_PHONE)
        startActivity(intent)
    }

    fun StartDialerActivity() {
        val intent = Intent()
        intent.setClass(this, DialerActivity::class.java)
        startActivity(intent)
    }
    private fun checkPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_PHONE_NUMBERS
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this as Activity,
                arrayOf(Manifest.permission.READ_PHONE_NUMBERS, Manifest.permission.READ_PHONE_STATE),
                101
            )
        }
    }
    fun checkPermission(permission: String) {
        if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(permission), 101)
        }
    }

    fun checkContacts(phoneNumber: String): Boolean {
        checkPermission(Manifest.permission.READ_CONTACTS)
        val phoneProjection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        )
        val selection = "${ContactsContract.CommonDataKinds.Phone.NUMBER} = ?"
        val selectionArgs = arrayOf(phoneNumber)
        val cursor: Cursor? = contentResolver?.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            phoneProjection,
            selection,
            selectionArgs,
            null
        )

        cursor?.use {
            if (it.count > 0) {
                // Phone number exists in contacts
                Log.i("ContactCheck", "Phone number $phoneNumber exists in contacts.")
            } else {
                // Phone number does not exist in contacts
                Log.i("ContactCheck", "Phone number $phoneNumber does not exist in contacts.")
            }
        }

        return true
    }

    companion object {
        private const val REDIRECT_ROLE_REQUEST_CODE = 1

    }
}


