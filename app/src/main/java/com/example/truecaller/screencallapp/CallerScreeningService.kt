package com.example.truecaller.screencallapp

import android.content.Context
import android.os.Build
import android.telecom.Call
import android.telecom.CallScreeningService
import android.telecom.Connection
import android.util.Log
import androidx.annotation.RequiresApi

class CallerScreeningService: CallScreeningService() {
    private val TAG = CallerScreeningService::class.java.toString()
    private enum class State {
        UNKNOWN_CALL,
        DISALLOW_CALL,
        ALLOW_CALL
    }
//    private val UNKNOWN_CALL = -1
//    private val DISALLOW_CALL = 0
//    private val ALLOW_CALL = 1

    private lateinit var details: Call.Details
//    private lateinit var context: Context
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onScreenCall(callDetails: Call.Details) {
        // Can check the direction of the call
        this.details = callDetails
        val phoneNumber: String = callDetails.handle.schemeSpecificPart
        Log.d(TAG, callDetails.toString())
        when (callDetails.callDirection) {
            Call.Details.DIRECTION_INCOMING -> {
                Log.d(TAG, "INCOMING: $phoneNumber")
                if (isBlockedCalls(phoneNumber)) {
                    disallowCall()
                } else {
                    allowCall()
                }
            }
            Call.Details.DIRECTION_OUTGOING -> {
                Log.d(TAG, "OUTCOMING: $phoneNumber")
            }
            Call.Details.DIRECTION_UNKNOWN -> {
                Log.d(TAG, "UNKNOWN: $phoneNumber")
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

    @RequiresApi(Build.VERSION_CODES.R)
    private fun isBlockedCalls(phone: String) : Boolean {
        Log.d(TAG, phone)
//        if (this.details.callerNumberVerificationStatus != Connection.VERIFICATION_STATUS_PASSED) {
//            return true
//        }
        return false
    }
}