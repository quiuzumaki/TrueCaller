package com.example.truecaller

import android.Manifest
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.telephony.PhoneStateListener
import android.telephony.ServiceState
import android.telephony.TelephonyCallback
import android.telephony.TelephonyManager
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat

class CallerConnectionReceiver: BroadcastReceiver() {
    private lateinit var context: Context
    private val TAG = CallerConnectionReceiver::class.java.simpleName
    private lateinit var telephonyManager: TelephonyManager
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onReceive(context: Context?, intent: Intent?) {
        this.context = context!!
        this.telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        Log.d(TAG, "onReceive")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            telephonyManager.registerTelephonyCallback(
                context.mainExecutor,
                CallerListener()
            )
        } else {
            telephonyManager.listen(
                CallerPhoneStateListener(),
                PhoneStateListener.LISTEN_CALL_STATE
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    inner class CallerListener: TelephonyCallback(),
        TelephonyCallback.ServiceStateListener, TelephonyCallback.CallStateListener
    {
        override fun onServiceStateChanged(serviceState: ServiceState) {
            Log.i(TAG, serviceState.toString())
        }

        override fun onCallStateChanged(state: Int) {
            when(state) {
                TelephonyManager.CALL_STATE_RINGING -> {
                    // A new call arrived and is ringing or waiting. In the latter case, another call is already active.
                    Log.d(TAG, "CALL_STATE_RINGING")
                }
                TelephonyManager.CALL_STATE_IDLE -> {
                    // Call has ended/No activity
                    Log.d(TAG, "CALL_STATE_IDLE")
                }
                TelephonyManager.CALL_STATE_OFFHOOK -> {
                    // At least one call exists that is dialing, active, or on hold, and no calls are ringing or waiting.
                    Log.d(TAG, "CALL_STATE_OFFHOOK")
                }
            }
        }
    }

    // @RequiredAPI(Build.VERSION.SDK_INT <= 30)
    inner class CallerPhoneStateListener: PhoneStateListener() {
        override fun onCallStateChanged(state: Int, phoneNumber: String?) {
            super.onCallStateChanged(state, phoneNumber)
            when(state) {
                TelephonyManager.CALL_STATE_RINGING -> {
                // Checking phonenumber
                    isBlockedCalls(phoneNumber)
                }
                TelephonyManager.CALL_STATE_OFFHOOK -> {

                }
                TelephonyManager.CALL_STATE_IDLE -> {

                }
            }
        }
    }

    private fun isBlockedCalls(phone: String?) : Boolean {
        Log.d(TAG, phone!!)
        if (phone == null) return true
        return false
    }


}