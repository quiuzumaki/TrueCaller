package com.example.truecaller.redirectapp

import android.net.Uri
import android.os.Build
import android.telecom.CallRedirectionService
import android.telecom.PhoneAccountHandle
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.Q)
class RedirectionService: CallRedirectionService() {
    private val TAG: String = RedirectionService::class.java.toString()
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onPlaceCall(
        handle: Uri,
        initialPhoneAccount: PhoneAccountHandle,
        allowInteractiveResponse: Boolean
    ) {
        // Determine if the call should proceed, be redirected, or cancelled.
        val callShouldProceed = true
        val callShouldRedirect = false
        when {
            callShouldProceed -> {
                placeCallUnmodified()
            }

            callShouldRedirect -> {
                // Update the URI to point to a different phone number or modify the
                // PhoneAccountHandle and redirect.
                redirectCall(handle, initialPhoneAccount, true)
            }

            else -> {
                cancelCall()
            }
        }
    }
}