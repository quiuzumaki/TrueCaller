package com.example.truecaller.screencallapp

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.telecom.Call
import android.telecom.CallScreeningService
import android.telecom.Connection
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.truecaller.defaultcall.CallerDeflectorActivity
import com.example.truecaller.defaultcall.ContactDetail
import com.example.truecaller.defaultcall.INFO_STATUS
import com.example.truecaller.telecom.call.TelecomCallService

class CallerScreeningService: CallScreeningService() {
    private val TAG = CallerScreeningService::class.java.simpleName

    private enum class State {
        UNKNOWN_CALL,
        DISALLOW_CALL,
        ALLOW_CALL
    }

    private lateinit var details: Call.Details
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onScreenCall(callDetails: Call.Details) {
        this.details = callDetails
        val phoneNumber: String = callDetails.handle.schemeSpecificPart
        Log.d(TAG, callDetails.toString())

        when (callDetails.callDirection) {
            Call.Details.DIRECTION_INCOMING -> {
                Log.d(TAG, "INCOMING: $phoneNumber")
                this.launchCall(
                    action = TelecomCallService.ACTION_INCOMING_CALL,
                    name = "quido",
                    uri = Uri.parse("tel:$phoneNumber"),
                )
            }
            Call.Details.DIRECTION_OUTGOING -> {
                Log.d(TAG, "OUTCOMING: $phoneNumber")
            }
            Call.Details.DIRECTION_UNKNOWN -> {
                Log.d(TAG, "UNKNOWN: $phoneNumber")
            }
        }
    }

    public fun disallowCall() {
        val response = CallResponse.Builder()
            .setDisallowCall(true)
            .setRejectCall(true)
            .setSkipCallLog(false)
            .setSkipNotification(true)
            .build()
        respondToCall(this.details, response)
    }

    public fun allowCall() {
        val response = CallResponse.Builder()
            .setDisallowCall(false)
            .setRejectCall(false)
            .setSkipCallLog(false)
            .setSkipNotification(false)
            .build()
        respondToCall(this.details, response)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun isBlockedCalls(phone: String) : Boolean {
        Log.d(TAG, phone)
        return false
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun Context.launchCall(action: String, name: String, uri: Uri) {
//        startService(
//            Intent(this, TelecomCallService::class.java).apply {
//                this.action = action
//                putExtra(TelecomCallService.EXTRA_NAME, name)
//                putExtra(TelecomCallService.EXTRA_URI, uri)
//            },
//        )
        startActivity(
            Intent(this, CallerDeflectorActivity::class.java).apply {
                this.action = action
                this.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                putExtra("name", name)
                putExtra("uri", uri.schemeSpecificPart)
            }
        )
    }
}