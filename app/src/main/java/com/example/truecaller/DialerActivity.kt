package com.example.truecaller

import android.app.role.RoleManager
import android.content.Intent
import android.os.Build
import androidx.activity.ComponentActivity


class DialerActivity: ComponentActivity() {
    private val REQUEST_ID = 1
    fun requestRole() {
        val roleManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            getSystemService(ROLE_SERVICE) as RoleManager
        } else {
            TODO("VERSION.SDK_INT < Q")
        }
        val intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_DIALER)
        startActivityForResult(intent, REQUEST_ID)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ID) {
            if (resultCode == RESULT_OK) {
                // Your app is now the default dialer app
            } else {
                // Your app is not the default dialer app
            }
        }
    }
}