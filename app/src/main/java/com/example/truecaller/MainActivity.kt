package com.example.truecaller

import android.Manifest
import android.app.role.RoleManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.telecom.PhoneAccountHandle
import android.telecom.TelecomManager
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.example.truecaller.defaultapp.CallerConnectionService
import com.example.truecaller.defaultapp.DialerActivity
import com.example.truecaller.ui.theme.TrueCallerTheme


class MainActivity : ComponentActivity() {
    private val TAG: String = MainActivity::class.java.simpleName
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TrueCallerTheme {
                MainScreen(onClick = {})
            }
        }
    }

    fun CreateActivity() {
        val telecomManager = getSystemService(Context.TELECOM_SERVICE) as TelecomManager
        val uri = Uri.fromParts("tel", "12345", null)
        val extras = Bundle()
        val connectionManagerPhoneAccount: PhoneAccountHandle? = null
        extras.putParcelable(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, connectionManagerPhoneAccount)
        checkPermission()
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
        checkPermission()
        startActivity(intent)
    }

    fun StartDialerActivity() {
        val intent = Intent()
        intent.setClass(this, DialerActivity::class.java)
        startActivity(intent)
    }

    fun checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), 101)
        }
    }
    companion object {
        private const val REDIRECT_ROLE_REQUEST_CODE = 1

    }
}


