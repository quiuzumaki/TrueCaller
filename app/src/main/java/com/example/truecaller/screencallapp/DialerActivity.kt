package com.example.truecaller.screencallapp

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
    private val ROLE_CALL_SCREENING_PERMISSION = RoleManager.ROLE_CALL_SCREENING
    private val ROLE_DIALER_PERMISSION = RoleManager.ROLE_DIALER
    private val intentLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            showToast("Success requesting!")
        } else {
            showToast("Failed requesting")
        }
    }
    private lateinit var roleManager: RoleManager
    @RequiresApi(Build.VERSION_CODES.Q)
    fun requestRoleCallScreening() {
        if (roleManager.isRoleAvailable(ROLE_CALL_SCREENING_PERMISSION) &&
            !roleManager.isRoleHeld(ROLE_CALL_SCREENING_PERMISSION)
        ) {
            intentLauncher.launch(
                roleManager.createRequestRoleIntent(ROLE_CALL_SCREENING_PERMISSION)
            )
        } else {
            showToast("App is already by this app")
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        roleManager = getSystemService(Context.ROLE_SERVICE) as RoleManager
        requestRoleCallScreening()
        setContent {
            DialerScreen (
                {
                    placeCall()
                }
            )
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

//    private fun addNewIncomingCall(incomingHandle: Uri) {
//        val extras = Bundle()
//        extras.putParcelable(TelecomManager.EXTRA_INCOMING_CALL_ADDRESS, incomingHandle)
//        telecomManager.addNewIncomingCall(TEST_PHONE_ACCOUNT_HANDLE, extras)
//    }
}