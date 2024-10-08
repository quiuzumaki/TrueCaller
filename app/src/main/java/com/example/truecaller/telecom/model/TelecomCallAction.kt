package com.example.truecaller.telecom.model

import android.os.ParcelUuid
import android.os.Parcelable
import android.telecom.DisconnectCause
import kotlinx.parcelize.Parcelize

/**
 * Simple interface to represent related call actions to communicate with the registered call scope
 * in the [TelecomCallRepository.registerCall]
 *
 * Note: we are using [Parcelize] to make the actions parcelable so they can be directly used in the
 * call notification.
 */
sealed interface TelecomCallAction : Parcelable {
    @Parcelize
    object Answer : TelecomCallAction

    @Parcelize
    data class Disconnect(val cause: DisconnectCause) : TelecomCallAction

    @Parcelize
    object Hold : TelecomCallAction

    @Parcelize
    object Activate : TelecomCallAction

    @Parcelize
    data class ToggleMute(val isMute: Boolean) : TelecomCallAction

    @Parcelize
    data class SwitchAudioEndpoint(val endpointId: ParcelUuid) : TelecomCallAction

    @Parcelize
    data class TransferCall(val endpointId: ParcelUuid) : TelecomCallAction
}
