package com.example.truecaller.defaultapp

import android.Manifest
import android.app.Activity
import android.app.role.RoleManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.telecom.PhoneAccountHandle
import android.telecom.TelecomManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat


class DialerActivity: ComponentActivity() {
    private val PHONE = "091211212"
    private lateinit var intentLauncher: ActivityResultLauncher<Intent>
    @RequiresApi(Build.VERSION_CODES.Q)
    fun requestRole() {
        val roleManager = getSystemService(Context.ROLE_SERVICE) as RoleManager
        if (roleManager.isRoleAvailable(RoleManager.ROLE_DIALER)) {
            if (!roleManager.isRoleHeld(RoleManager.ROLE_DIALER)) {
                intentLauncher.launch(
                    roleManager.createRequestRoleIntent(RoleManager.ROLE_DIALER)
                )
            } else {
                showToast("App is already held by this app")
            }
        } else {
            showToast("App does not exist")
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DialerScreen (
                {
                    requestRole()
                    placeCall()
                }
            )
        }
        intentLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                showToast("Success requesting ROLE_BROWSER!")
            } else {
                showToast("Failed requesting ROLE_BROWSER")
            }
        }
    }

    private fun showToast(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_LONG)
    }

    fun placeCall(address: String? = PHONE) {
        val telecomManager = getSystemService(Context.TELECOM_SERVICE) as TelecomManager
        val extras = Bundle()
        val connectionManagerPhoneAccount: PhoneAccountHandle? = null
        extras.putParcelable(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, connectionManagerPhoneAccount)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), 101)
        }
        telecomManager.placeCall(Uri.fromParts("tel", address, null), extras)
    }
}