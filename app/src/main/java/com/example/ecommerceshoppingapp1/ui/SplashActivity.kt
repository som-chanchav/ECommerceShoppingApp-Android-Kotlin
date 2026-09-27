package com.example.ecommerceshoppingapp1.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.ecommerceshoppingapp1.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Simulate loading for 1.5 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            checkLoginStatus()
        }, 1500)
    }

    private fun checkLoginStatus() {
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val email = sharedPreferences.getString("session_email", null)

        if (email.isNullOrEmpty()) {
            // Not logged in -> go to Onboarding/Login
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            // Logged in -> go to Homepage
            startActivity(Intent(this, HomepageActivity::class.java))
        }
        finish()
    }
}
