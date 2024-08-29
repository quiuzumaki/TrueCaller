package com.example.truecaller.screencallapp

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.telecom.Call
import android.telecom.CallScreeningService
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.truecaller.CallerNotification
import com.example.truecaller.defaultcall.CallerDeflectorActivity
import com.example.truecaller.defaultcall.ContactDetail
import com.example.truecaller.defaultcall.INFO_STATUS

class CallerScreeningService: CallScreeningService() {
    private val TAG = CallerScreeningService::class.java.simpleName

    companion object {
        const val INCOMING_CALL: String = "incoming_call"
        const val OUTGOING_CALL: String = "outgoing_call"
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
                CallerNotification.showNotification(this, ContactDetail("quido", "0914143822", INFO_STATUS.SPAM))
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
}