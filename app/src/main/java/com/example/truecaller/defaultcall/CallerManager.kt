package com.example.truecaller.defaultcall

import android.Manifest
import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.telecom.PhoneAccount
import android.telecom.PhoneAccountHandle
import android.telecom.TelecomManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.truecaller.R

class CallerManager public constructor(val ctx: Context) {
    private val TAG: String = CallerManager::class.java.simpleName
    private val PHONE_ACCOUNT_ID = TAG + ".PHONE_ACCOUNT_ID"

    private lateinit var telecomManager: TelecomManager
    private lateinit var phoneAccount: PhoneAccount
    private lateinit var phoneAccountHandle: PhoneAccountHandle

    init {
        this.telecomManager = ctx.getSystemService(Context.TELECOM_SERVICE) as TelecomManager
        this.phoneAccountHandle = PhoneAccountHandle(
            ComponentName(ctx, CallerConnectionService::class.java),
            PHONE_ACCOUNT_ID
        )
    }

    private fun registerPhoneAccount() {
        val builder = PhoneAccount.builder(
            phoneAccountHandle,
            ctx.resources.getText(R.string.app_name)
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setCapabilities(PhoneAccount.CAPABILITY_SELF_MANAGED)
        }
        val phoneAccount = builder.build()
        telecomManager.registerPhoneAccount(phoneAccount)
    }

    public fun showPhoneAccount() {
        val intent = Intent().apply {
            setComponent(ComponentName(
                "com.android.server.telecom",
                "com.android.server.telecom.settings.EnableAccountPreferenceActivity"
            ))
            setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        ctx.startActivity(intent)
    }

    public fun incomingCall() {
        val extras: Bundle = Bundle()
        extras.putParcelable(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, phoneAccountHandle)
        extras.putParcelable(TelecomManager.EXTRA_INCOMING_CALL_EXTRAS, PHONENUMBER)
        extras.putBoolean(TelecomManager.METADATA_IN_CALL_SERVICE_UI, true)
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
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                ctx as Activity,
                arrayOf(Manifest.permission.MANAGE_OWN_CALLS, Manifest.permission.CALL_PHONE),
                101)
        }
    }
}