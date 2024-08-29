package com.example.truecaller.dialog

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.truecaller.screencallapp.CallerScreeningService
import com.example.truecaller.screencallapp.DialerScreen

class DialogActivity: ComponentActivity() {
    private val TAG: String = DialogActivity::class.java.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = intent
        val phone = intent?.extras?.getString(CallerScreeningService.INCOMING_CALL)
        Log.d(TAG, phone ?: "null")
        setContent {
            DialerScreen {

            }
        }
    }
}