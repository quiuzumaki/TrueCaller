package com.example.truecaller.defaultcall

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.telecom.TelecomManager
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class CallerDeflectorActivity: ComponentActivity() {
    private val TAG: String = CallerDeflectorActivity::class.java.simpleName
    private val PHONENUMBER: Uri = Uri.fromParts("tel:", "0914143822", null)

    private var CALL_PERMISSION_REQUEST = 99
    private lateinit var callerManager: CallerManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        callerManager = CallerManager(this)

        val intent: Intent? = intent
        val phoneNumber = intent?.getStringExtra("uri") ?: "0914143822"
        val name = intent?.getStringExtra("name") ?: "quido"
        Log.d(TAG, "Receiver(%s\n%s)".format(phoneNumber, name))

//        callerManager.enablePhoneAccount()




        setContent {
            showOnCall(
                ContactDetail(name, phoneNumber, INFO_STATUS.SPAM),
                onAnswer = {},
                onReject = {}
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CALL_PERMISSION_REQUEST -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }

    private fun hasCallPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.MANAGE_OWN_CALLS)
            == PackageManager.PERMISSION_GRANTED)
        {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED)
                return true
        }
        return false
    }

    private fun requestInCallPermission() {

        // Here, thisActivity is the current activity
        if (!hasCallPermission()) {

            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.MANAGE_OWN_CALLS, Manifest.permission.CALL_PHONE),
                CALL_PERMISSION_REQUEST)
        } else {
            // Permission has already been granted
        }
    }
}


@Composable
fun showOnCall(
    contactDetail: ContactDetail,
    onAnswer: () -> Unit?,
    onReject: () -> Unit?
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        showInfo(contactDetail = contactDetail)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { onAnswer() },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green),
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
            ) {
                Icon(imageVector = Icons.Default.Call, contentDescription = "")
            }
            Button(
                onClick = { onReject() },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
            ) {
                Icon(imageVector = Icons.Default.Clear, contentDescription = "")
            }
        }
    }
}

@Composable
fun showInfo(contactDetail: ContactDetail) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val infoStatus = contactDetail.infoStatus
        val fontSize = 25.sp
        val modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)

        // STATUS
        Text(
            text = contactDetail.infoStatus.toString(),
            fontSize = fontSize,
            modifier = modifier
        )
        // Icon
        Box(
            modifier = Modifier
                .wrapContentSize()
                .clip(CircleShape)
                .background(
                    color = when (infoStatus) {
                        INFO_STATUS.KNOWN -> Color.White
                        INFO_STATUS.UNKNOWN -> Color.Gray
                        INFO_STATUS.SPAM -> Color.Red
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = Icons.Default.Person, contentDescription = "",
                modifier = Modifier.size(200.dp))
        }

        // PhoneNumber
        Text(text = contactDetail.phoneNumber, modifier = modifier, fontSize = fontSize)
        // Name
        Text(text = contactDetail.name, modifier = modifier, fontSize = fontSize)
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xffffff)
fun demoScreen() {
    showOnCall(
        ContactDetail("quido", "0911222121", INFO_STATUS.SPAM),
        {},
        {}
    )
}