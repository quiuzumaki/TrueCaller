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
        Log.i(TAG, "onCreateIncomingConnection\nrequest.address: %s".format(request?.address.toString()))
        if (accountHandle != null) {
            return createCall(request.address ?: Uri.fromParts("tel", "0914143822", null))
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

    override fun onCreateIncomingConnectionFailed(
        connectionManagerPhoneAccount: PhoneAccountHandle?,
        request: ConnectionRequest?
    ) {
        Log.d(TAG, "onCreateIncomingConnectionFailed")
        super.onCreateIncomingConnectionFailed(connectionManagerPhoneAccount, request)
    }

    override fun onCreateOutgoingConnectionFailed(
        connectionManagerPhoneAccount: PhoneAccountHandle?,
        request: ConnectionRequest?
    ) {
        Log.d(TAG, "onCreateOutgoingConnectionFailed")
        super.onCreateOutgoingConnectionFailed(connectionManagerPhoneAccount, request)
    }

    private fun createCall(phoneNumber: Uri?): CallerConnection {
        val connection = CallerConnection()
        val extras = Bundle()
        connection.setAddress(phoneNumber, PRESENTATION_ALLOWED)
        connection.extras = extras
        connection.setConnectionCapabilities(Connection.CAPABILITY_SUPPORT_DEFLECT)
        connection.setRingbackRequested(true)
        connection.setActive()
        connection.setRinging()
        return connection
    }
}