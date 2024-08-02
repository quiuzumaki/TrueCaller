package com.example.truecaller

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.telecom.Connection
import android.telecom.ConnectionRequest
import android.telecom.ConnectionService
import android.telecom.PhoneAccountHandle
import android.telecom.TelecomManager
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat


class CallerConnectionService: ConnectionService() {

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    override fun onCreateIncomingConnection(
        connectionManagerPhoneAccount: PhoneAccountHandle?,
        request: ConnectionRequest?
    ): Connection {
        val from = request?.extras?.getString("from")
        val connection = CallerConnection()
        connection.setAddress(Uri.parse("tel:$from"), TelecomManager.PRESENTATION_ALLOWED)
        connection.connectionProperties = Connection.PROPERTY_SELF_MANAGED
        connection.setRinging()
        connection.setCallerDisplayName("Calling", TelecomManager.PRESENTATION_ALLOWED)
        return connection
//        return super.onCreateIncomingConnection(connectionManagerPhoneAccount, request)
    }

    override fun onCreateOutgoingConnection(
        connectionManagerPhoneAccount: PhoneAccountHandle?,
        request: ConnectionRequest?
    ): Connection {
        return super.onCreateOutgoingConnection(connectionManagerPhoneAccount, request)
    }

    override fun onCreateOutgoingConnectionFailed(
        connectionManagerPhoneAccount: PhoneAccountHandle?,
        request: ConnectionRequest?
    ) {
        super.onCreateOutgoingConnectionFailed(connectionManagerPhoneAccount, request)
    }

    fun demo() {
        val telecomManager = getSystemService(Context.TELECOM_SERVICE) as TelecomManager
        val uri = Uri.fromParts("tel", "12345", null)
        val extras = Bundle()
        extras.putBoolean(TelecomManager.EXTRA_START_CALL_WITH_SPEAKERPHONE, true)
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
            return
        }
        telecomManager.placeCall(uri, extras)

    }
}