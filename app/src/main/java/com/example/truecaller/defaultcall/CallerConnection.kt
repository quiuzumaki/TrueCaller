package com.example.truecaller.defaultcall


import android.telecom.CallAudioState
import android.telecom.Connection
import android.telecom.ConnectionRequest
import android.telecom.DisconnectCause
import android.util.Log

class CallerConnection(var originalRequest: ConnectionRequest? = null): Connection() {
    private val TAG: String = CallerConnection::class.java.simpleName
    override fun onShowIncomingCallUi() {
        super.onShowIncomingCallUi()
        Log.d(TAG, "onShowIncommingUI")
    }

    override fun onCallAudioStateChanged(state: CallAudioState?) {
        super.onCallAudioStateChanged(state)
        Log.d(TAG, "onCallAudioStateChanged")
    }

    override fun onHold() {
        super.onHold()
        Log.d(TAG, "onHold")
        setOnHold()
    }

    override fun onUnhold() {
        super.onUnhold()
        Log.d(TAG, "onUnhold")
        setActive()
    }

    override fun onAnswer() {
        super.onAnswer()
        Log.d(TAG, "onAnswer")
        setActive()
    }

    override fun onReject() {
        super.onReject()
        setDisconnected(DisconnectCause(DisconnectCause.REJECTED))
        destroy()
    }

    override fun onDisconnect() {
        super.onDisconnect()
        Log.d(TAG, "onDisconnect")
        destroy()
    }

    override fun onAbort() {
        super.onAbort()
        Log.d(TAG, "onAbort")
        destroy()
    }
}