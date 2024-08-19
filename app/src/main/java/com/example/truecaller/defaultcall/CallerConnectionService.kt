package com.example.truecaller.defaultcall

import android.content.Intent
import android.os.Bundle
import android.provider.CallLog.Calls.PRESENTATION_ALLOWED
import android.telecom.Connection
import android.telecom.ConnectionRequest
import android.telecom.ConnectionService
import android.telecom.DisconnectCause
import android.telecom.PhoneAccountHandle
import android.util.Log

class CallerConnectionService: ConnectionService() {
    private val TAG = ConnectionService::class.java.simpleName

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
            return createConnection(request)
        } else {
            return Connection.createFailedConnection((DisconnectCause(DisconnectCause.ERROR,
                "Invalid inputs: " + accountHandle)))
        }
    }

    override fun onCreateOutgoingConnection(
        connectionManagerPhoneAccount: PhoneAccountHandle?,
        request: ConnectionRequest?
    ): Connection {
        val accountHandle  = request?.accountHandle
        Log.i(TAG, "onCreateOutgoingConnection\nrequest.address: %s".format(request?.address.toString()))
        if (accountHandle != null) {
            return createConnection(request)
        } else {
            return Connection.createFailedConnection((DisconnectCause(DisconnectCause.ERROR,
                "Invalid inputs: " + accountHandle)))
        }
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

    private fun createConnection(request: ConnectionRequest, incoming: Boolean = false): CallerConnection {
        val extras = Bundle()
        extras.putString("name", "quido")
        val connection = CallerConnection()

        connection.apply {
            setAddress(request.address, PRESENTATION_ALLOWED)
            setExtras(extras)
            setConnectionCapabilities(
                Connection.CAPABILITY_CAN_SEND_RESPONSE_VIA_CONNECTION or
                Connection.CAPABILITY_SUPPORT_HOLD or
                Connection.CAPABILITY_HOLD
            )
            setCallerDisplayName(
                "Demo Caller",
                PRESENTATION_ALLOWED
            )
            setRingbackRequested(true)
            setActive()
            setRinging()
        }
        return connection
    }
}