package com.example.truecaller.defaultapp

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.telecom.Call
import android.telecom.Connection
import android.telecom.ConnectionRequest
import android.telecom.ConnectionService
import android.telecom.DisconnectCause
import android.telecom.PhoneAccountHandle
import android.telecom.TelecomManager
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat


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

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    override fun onCreateIncomingConnection(
        connectionManagerPhoneAccount: PhoneAccountHandle?,
        request: ConnectionRequest?
    ): Connection {
        super.onCreateIncomingConnection(connectionManagerPhoneAccount, request)
        val accountHandle  = request?.accountHandle
        if (accountHandle != null) {
            val connection = CallerConnection()
            val address = request?.extras?.getString("from")

            connection.apply{
                setRinging()
                setAddress(Uri.parse("tel:$address"), TelecomManager.PRESENTATION_ALLOWED)
                setCallerDisplayName("Calling", TelecomManager.PRESENTATION_ALLOWED)
            }
            return connection
        } else {
            return Connection.createFailedConnection((DisconnectCause(DisconnectCause.ERROR,
                "Invalid inputs: " + accountHandle)))
        }
    }

    @RequiresApi(Build.VERSION_CODES.N_MR1)
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

    fun demo(
        connectionManagerPhoneAccount: PhoneAccountHandle?,
        request: ConnectionRequest?
    ): Connection {
        super.onCreateOutgoingConnection(connectionManagerPhoneAccount, request)
        val telecomManager = getSystemService(Context.TELECOM_SERVICE) as TelecomManager
        val uri = Uri.fromParts("tel", "12345", null)
        val extras = Bundle()
        extras.putParcelable(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, connectionManagerPhoneAccount)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return Connection.createFailedConnection(DisconnectCause(DisconnectCause.CANCELED))
        }
        telecomManager.placeCall(uri, extras)
        return CallerConnection()
    }

}