package com.example.truecaller.defaultcall

import android.Manifest
import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.telecom.PhoneAccount
import android.telecom.PhoneAccountHandle
import android.telecom.TelecomManager
import androidx.compose.ui.res.painterResource
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.truecaller.R

class CallerManager public constructor(val ctx: Context) {
    private val TAG: String = CallerManager::class.java.simpleName
    private val PHONE_ACCOUNT_ID = TAG + ".PHONE_ACCOUNT_ID"
    private val PHONENUMBER: Uri = Uri.fromParts(PhoneAccount.SCHEME_TEL, "0914143822", null)

    private var telecomManager: TelecomManager
    private var phoneAccountHandle: PhoneAccountHandle

    private lateinit var phoneAccount: PhoneAccount

    init {
        this.telecomManager = ctx.getSystemService(Context.TELECOM_SERVICE) as TelecomManager
        this.phoneAccountHandle = PhoneAccountHandle(
            ComponentName(ctx, CallerConnectionService::class.java),
            PHONE_ACCOUNT_ID
        )
        registerPhoneAccount()
    }

    private fun registerPhoneAccount() {
        val builder = PhoneAccount.builder(
            phoneAccountHandle,
            ctx.resources.getText(R.string.app_name)
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setCapabilities(PhoneAccount.CAPABILITY_SELF_MANAGED)
        }
        this.phoneAccount = builder
            .setCapabilities(
//                PhoneAccount.CAPABILITY_CONNECTION_MANAGER or
                PhoneAccount.CAPABILITY_CALL_PROVIDER
            )
            .addSupportedUriScheme(PhoneAccount.SCHEME_TEL)
            .build()
        telecomManager.registerPhoneAccount(this.phoneAccount)
    }

    public fun enablePhoneAccount() {
        val intent = Intent(TelecomManager.ACTION_CHANGE_PHONE_ACCOUNTS)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        ctx.startActivity(intent)
    }

    public fun showPhoneAccount() {
        val intent = Intent().apply {
            component = ComponentName(
                "com.android.server.telecom",
                "com.android.server.telecom.settings.EnableAccountPreferenceActivity"
            )
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        ctx.startActivity(intent)
    }

    public fun incomingCall() {
        val extras: Bundle = Bundle()
        extras.putParcelable(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, phoneAccountHandle)
        extras.putBoolean(TelecomManager.METADATA_IN_CALL_SERVICE_UI, true)
        extras.putParcelable(TelecomManager.EXTRA_INCOMING_CALL_ADDRESS, this.PHONENUMBER)
        telecomManager.addNewIncomingCall(phoneAccountHandle, extras)
    }

    public fun outgoingCall(phoneNumber: String) {
        val outgoingUri =
            Uri.fromParts(PhoneAccount.SCHEME_SIP, phoneNumber, null)
        val outgoingExtras = Bundle()
        outgoingExtras.putParcelable(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, this.phoneAccountHandle)
        this.checkPermission()
        this.telecomManager.placeCall(outgoingUri, outgoingExtras)
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                ctx,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                ctx,
                Manifest.permission.MANAGE_OWN_CALLS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                ctx as Activity,
                arrayOf(Manifest.permission.MANAGE_OWN_CALLS, Manifest.permission.CALL_PHONE),
                101)
        }
    }
}