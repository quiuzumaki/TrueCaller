package com.example.truecaller

import android.app.role.RoleManager
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import com.example.truecaller.ui.theme.TrueCallerTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TrueCallerTheme {
                MainScreen()
            }
        }
        main()
    }
    fun main() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            val roleManager = getSystemService(Context.ROLE_SERVICE) as RoleManager
            // Check if the app needs to register call redirection role.
            val shouldRequestRole = roleManager.isRoleAvailable(RoleManager.ROLE_CALL_REDIRECTION) &&
                    !roleManager.isRoleHeld(RoleManager.ROLE_CALL_REDIRECTION)
            if (shouldRequestRole) {
                val intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_CALL_REDIRECTION)
                startActivityForResult(intent, REDIRECT_ROLE_REQUEST_CODE)
            }
        }
    }
    companion object {
        private const val REDIRECT_ROLE_REQUEST_CODE = 1
    }
}


