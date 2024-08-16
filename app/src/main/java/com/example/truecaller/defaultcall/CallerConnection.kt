package com.example.truecaller.defaultcall


import android.telecom.CallAudioState
import android.telecom.Connection
import android.telecom.ConnectionRequest
import android.telecom.DisconnectCause

class CallerConnection(var originalRequest: ConnectionRequest? = null): Connection() {
    private val TAG: String = CallerConnection::class.java.simpleName
    override fun onShowIncomingCallUi() {
        super.onShowIncomingCallUi()
    }

    override fun onCallAudioStateChanged(state: CallAudioState?) {
        super.onCallAudioStateChanged(state)
    }

    override fun onHold() {
        super.onHold()
        setOnHold()
    }

    override fun onUnhold() {
        super.onUnhold()
        setActive()
    }

    override fun onAnswer() {
        super.onAnswer()
        setActive()
    }

    override fun onReject() {
        super.onReject()
        setDisconnected(DisconnectCause(DisconnectCause.REJECTED))
        destroy()
    }

    override fun onDisconnect() {
        super.onDisconnect()
        destroy()
    }

    override fun onAbort() {
        super.onAbort()
        destroy()
    }
}