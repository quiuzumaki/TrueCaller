package com.example.truecaller

import android.net.Uri
import android.os.Build
import android.telecom.Call
import android.telecom.CallScreeningService
import android.telecom.Connection
import android.util.Log
import androidx.annotation.RequiresApi

class ScreeningService: CallScreeningService() {
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onScreenCall(callDetails: Call.Details) {
        // Can check the direction of the call
        val isIncoming = callDetails.callDirection == Call.Details.DIRECTION_INCOMING

        if (isIncoming) {
            // the handle (e.g. phone number) that the Call is currently connected to
            val handle: Uri = callDetails.handle

            // determine if you want to allow or reject the call
            when (callDetails.callerNumberVerificationStatus) {
                Connection.VERIFICATION_STATUS_FAILED -> {
                    // Network verification failed, likely an invalid/spam call.
                }
                Connection.VERIFICATION_STATUS_PASSED -> {
                    // Network verification passed, likely a valid call.
                }
                else -> {
                    // Network could not perform verification.
                    // This branch matches Connection.VERIFICATION_STATUS_NOT_VERIFIED.
                }
            }
        }
        val response = CallResponse.Builder()
            // Sets whether the incoming call should be blocked.
            .setDisallowCall(false)
            // Sets whether the incoming call should be rejected as if the user did so manually.
            .setRejectCall(false)
            // Sets whether ringing should be silenced for the incoming call.
            .setSilenceCall(false)
            // Sets whether the incoming call should not be displayed in the call log.
            .setSkipCallLog(false)
            // Sets whether a missed call notification should not be shown for the incoming call.
            .setSkipNotification(false)
            .build()

        // Call this function to provide your screening response.
        respondToCall(callDetails, response)
    }

    fun isBlockedCalls(phone: String) : Boolean {
        Log.d("ScreeningService", phone)
        return false
    }

}