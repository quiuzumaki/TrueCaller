package com.example.truecaller.telecom

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.truecaller.telecom.call.TelecomCallActivity
import com.example.truecaller.telecom.call.TelecomCallService
import com.example.truecaller.telecom.model.TelecomCall
import com.example.truecaller.telecom.model.TelecomCallRepository

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TelecomCallSample() {
    // To record the audio for the call
    val permissions = mutableListOf(Manifest.permission.RECORD_AUDIO)

    // We should be using make_own_call permissions but this requires
    // implementation of the telecom API to work correctly.
    // Please see telecom example for full implementation
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        permissions.add(Manifest.permission.MANAGE_OWN_CALLS)
    }

    // To show call notifications we need permissions since Android 13
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        permissions.add(Manifest.permission.POST_NOTIFICATIONS)
    }

    PermissionBox(permissions = permissions) {
        TelecomCallOptions()
    }
}

/**
 * Screen to launch incoming and outgoing calls. It would normally be the dialer.
 */
@Preview(showBackground = true, backgroundColor = 0xffffff)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun TelecomCallOptions() {
    val context = LocalContext.current
    val repository = remember {
        TelecomCallRepository.instance ?: TelecomCallRepository.create(context.applicationContext)
    }
    val call by repository.currentCall.collectAsState()
    val hasOngoingCall = call is TelecomCall.Registered

    if (hasOngoingCall) {
        LaunchedEffect(Unit) {
            context.startActivity(
                Intent(context, TelecomCallActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                },
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val title = if (hasOngoingCall) {
            "There is an active call"
        } else {
            "No active call"
        }
        Text(text = title, style = MaterialTheme.typography.titleLarge)
        Button(
            enabled = !hasOngoingCall,
            onClick = {
                Toast.makeText(context, "Incoming call in 2 seconds", Toast.LENGTH_SHORT).show()
                context.launchCall(
                    action = TelecomCallService.ACTION_INCOMING_CALL,
                    name = "Alice",
                    uri = Uri.parse("tel:12345"),
                )
            },
        ) {
            Text(text = "Receive fake call")
        }
        Button(
            enabled = !hasOngoingCall,
            onClick = {
                context.launchCall(
                    action = TelecomCallService.ACTION_OUTGOING_CALL,
                    name = "Bob",
                    uri = Uri.parse("tel:54321"),
                )
            },
        ) {
            Text(text = "Make fake call")
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun Context.launchCall(action: String, name: String, uri: Uri) {
    startService(
        Intent(this, TelecomCallService::class.java).apply {
            this.action = action
            putExtra(TelecomCallService.EXTRA_NAME, name)
            putExtra(TelecomCallService.EXTRA_URI, uri)
        },
    )
}
