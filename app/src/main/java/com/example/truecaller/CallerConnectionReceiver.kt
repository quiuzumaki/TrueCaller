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
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService

class CallerConnectionReceiver: BroadcastReceiver() {
    private lateinit var context: Context
    private val TAG = CallerConnectionReceiver::class.java.simpleName
    private lateinit var telephonyManager: TelephonyManager
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null) {
            this.context = context
        }
        this.telephonyManager = context?.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            telephonyManager.registerTelephonyCallback(
                context.mainExecutor,
                CallerStateListener()
            )

            telephonyManager.registerTelephonyCallback(
                context.mainExecutor,
                object: TelephonyCallback(), TelephonyCallback.ServiceStateListener {
                    override fun onServiceStateChanged(serviceState: ServiceState) {
                        val channelNumber = serviceState.channelNumber
                        Log.i(TAG, channelNumber.toString())
                    }
                }
            )
        } else {
            telephonyManager.listen(object : PhoneStateListener() {
                override fun onCallStateChanged(state: Int, phoneNumber: String?) {
                    Log.i("PhoneStateListener", "onCallStateChanged")
                }
            }, PhoneStateListener.LISTEN_CALL_STATE)
        }
    }

    private fun checkPermission() {
        if (ActivityCompat.checkSelfPermission(
                this.context,
                Manifest.permission.READ_PHONE_NUMBERS
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this.context,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this.context as Activity,
                arrayOf(Manifest.permission.READ_PHONE_NUMBERS, Manifest.permission.READ_PHONE_STATE),
            101
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private class CallerStateListener: TelephonyCallback() , TelephonyCallback.CallStateListener {
        private val TAG = CallerStateListener::class.java.simpleName
        override fun onCallStateChanged(state: Int) {
            when(state) {
                TelephonyManager.CALL_STATE_RINGING -> {
                    Log.d(TAG, "CALL_STATE_RINGING")
                }
                TelephonyManager.CALL_STATE_IDLE -> {
                    Log.d(TAG, "CALL_STATE_IDLE")
                }
                TelephonyManager.CALL_STATE_OFFHOOK -> {
                    Log.d(TAG, "CALL_STATE_OFFHOOK")
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private class CallerServiceStateListener: TelephonyCallback(), TelephonyCallback.ServiceStateListener {
        private val TAG = CallerServiceStateListener::class.java.simpleName
        override fun onServiceStateChanged(serviceState: ServiceState) {
//            Log.i("ServiceStateListener", "onServiceStateChanged")
            val channelNumber = serviceState.channelNumber
//            val phoneNumber = this@Cal
            Log.i(TAG, channelNumber.toString())
        }
    }
}