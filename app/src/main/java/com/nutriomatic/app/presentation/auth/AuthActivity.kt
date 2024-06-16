package com.nutriomatic.app.presentation.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.nutriomatic.app.R
import com.nutriomatic.app.presentation.MainActivity
import com.nutriomatic.app.presentation.factory.ViewModelFactory

class AuthActivity : AppCompatActivity() {
    private val viewModel by viewModels<AuthViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(2000L)
        installSplashScreen()
        viewModel.getToken().observe(this) { token ->
            if (token != null) {
                val intent = Intent(this, MainActivity::class.java)
                intent.flags =
                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
        }

        setContentView(R.layout.activity_auth)
        supportActionBar?.hide()
    }
}