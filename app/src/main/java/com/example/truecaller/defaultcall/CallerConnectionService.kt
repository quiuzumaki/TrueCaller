package com.example.truecaller.defaultcall

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.CallLog.Calls.PRESENTATION_ALLOWED
import android.telecom.Connection
import android.telecom.ConnectionRequest
import android.telecom.ConnectionService
import android.telecom.DisconnectCause
import android.telecom.PhoneAccountHandle
import android.telecom.TelecomManager
import android.telecom.VideoProfile
import android.util.Log


class CallerConnectionService: ConnectionService() {
    private val TAG = ConnectionService::class.java.simpleName
    private val ADDRESS = "0839624822"
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "CallerConnectionService")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
        Log.d(TAG, "onStartCommmand")
    }

    override fun onCreateIncomingConnection(
        connectionManagerPhoneAccount: PhoneAccountHandle?,
        request: ConnectionRequest?
    ): Connection {
        super.onCreateIncomingConnection(connectionManagerPhoneAccount, request)
        val accountHandle  = request?.accountHandle
        if (accountHandle != null) {
//            val connection = CallerConnection()
//            val address = request?.extras?.getString("from")
//
//            connection.apply{
//                setRinging()
//                setAddress(Uri.parse("tel:$address"), TelecomManager.PRESENTATION_ALLOWED)
//                setCallerDisplayName("Calling", TelecomManager.PRESENTATION_ALLOWED)
//            }
//            return connection
            return createCall(request.address)
        } else {
            return Connection.createFailedConnection((DisconnectCause(DisconnectCause.ERROR,
                "Invalid inputs: " + accountHandle)))
        }
    }

    override fun onCreateOutgoingConnection(
        connectionManagerPhoneAccount: PhoneAccountHandle?,
        request: ConnectionRequest?
    ): Connection {
        Log.i(TAG, "onCreateOutgoingConnection, request: " + request.toString())
        val handle: Uri? = request?.address
        val number: String? = handle?.schemeSpecificPart

        val connection: CallerConnection = CallerConnection(request)

        connection.apply {
            setAddress(handle,  TelecomManager.PRESENTATION_ALLOWED)
            setInitialized()
            setCallerDisplayName("BShield", TelecomManager.PRESENTATION_ALLOWED)
        }
        return connection
    }

    override fun onCreateOutgoingConnectionFailed(
        connectionManagerPhoneAccount: PhoneAccountHandle?,
        request: ConnectionRequest?
    ) {
        super.onCreateOutgoingConnectionFailed(connectionManagerPhoneAccount, request)
        Log.d(TAG, "onCreateOutgoingConnectionFailed")
    }

    private fun createCall(phoneNumber: Uri): CallerConnection {
        val conn = CallerConnection()
        val extras = Bundle()
        conn.setAddress(phoneNumber, PRESENTATION_ALLOWED)
        conn.setVideoState(VideoProfile.STATE_AUDIO_ONLY)
        conn.setExtras(extras)
        conn.setConnectionCapabilities(Connection.CAPABILITY_SUPPORT_DEFLECT)
        conn.setRingbackRequested(true)
        conn.setActive()
        return conn
    }
}