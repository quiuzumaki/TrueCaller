package com.example.truecaller

import android.Manifest
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
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.ui.unit.Constraints
import androidx.core.app.ActivityCompat
import com.example.truecaller.defaultapp.CallerConnectionService
import com.example.truecaller.screencallapp.DialerActivity
import com.example.truecaller.screencallapp.CallerScreeningService
import com.example.truecaller.ui.theme.TrueCallerTheme

class MainActivity : ComponentActivity() {
    private val TAG: String = MainActivity::class.java.simpleName
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startService(Intent(this, CallerScreeningService::class.java))
        val intent = Intent()
        intent.apply {
            setClass(this@MainActivity, ContactsManager::class.java)
            putExtra(ContactsManager.PHONE_NUMBER_ID, "0912345678")
        }
        startActivity(intent)
        setContent {
            TrueCallerTheme {
                MainScreen(onClick = {StartDialerActivity()})
            }
        }
    }

    fun CreateActivity() {
        val telecomManager = getSystemService(Context.TELECOM_SERVICE) as TelecomManager
        val uri = Uri.fromParts("tel", "12345", null)
        val extras = Bundle()
        val connectionManagerPhoneAccount: PhoneAccountHandle? = null
        extras.putParcelable(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, connectionManagerPhoneAccount)
        checkPermission(Manifest.permission.READ_PHONE_STATE)
        telecomManager.placeCall(uri, extras)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun StartReceiver() {
        val r = CallerConnectionReceiver()
        Log.d(TAG, "hello")
//        val i = IntentFilter("android.permission.READ_PHONE_STATE")
//        this.registerReceiver(r, i, RECEIVER_EXPORTED);
    }
    fun StartService() {
        val intent = Intent(this, CallerConnectionService::class.java)
        startService(intent)
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


