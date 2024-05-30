package com.nutriomatic.app.presentation.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.nutriomatic.app.R

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(2000L)
        installSplashScreen()
        setContentView(R.layout.activity_auth)
        supportActionBar?.hide()
    }
}