package com.example.truecaller.screencallapp

import android.net.Uri
import android.os.Build
import android.telecom.Call
import android.telecom.CallScreeningService
import android.util.Log
import androidx.annotation.RequiresApi

class ScreeningService: CallScreeningService() {
    private val TAG = ScreeningService::class.java.toString()
    private lateinit var details: Call.Details
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onScreenCall(callDetails: Call.Details) {
        // Can check the direction of the call
        this.details = callDetails

        val isIncoming = callDetails.callDirection == Call.Details.DIRECTION_INCOMING
        if (isIncoming) {
            // the handle (e.g. phone number) that the Call is currently connected to
            val handle: Uri = callDetails.handle

            if (isBlockedCalls(handle.toString())) {
                disallowCall()
            } else {
                allowCall()
            }
        }
    }

    private fun disallowCall() {
        val response = CallResponse.Builder()
            .setDisallowCall(true)
            .setRejectCall(true)
            .setSkipCallLog(false)
            .setSkipNotification(true)
            .build()
        respondToCall(this.details, response)
    }

    private fun allowCall() {
        val response = CallResponse.Builder()
            .setDisallowCall(false)
            .setRejectCall(false)
            .setSkipCallLog(false)
            .setSkipNotification(false)
            .build()
        respondToCall(this.details, response)
    }

    fun isBlockedCalls(phone: String) : Boolean {
        Log.d(TAG, phone)
        return false
    }

}