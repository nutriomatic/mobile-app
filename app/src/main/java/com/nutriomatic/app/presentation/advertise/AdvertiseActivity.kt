package com.nutriomatic.app.presentation.advertise

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.nutriomatic.app.R

class AdvertiseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_advertise)

        supportActionBar?.hide()
        // Dapatkan referensi ke ConstraintLayout induk
        val advLayout = findViewById<ConstraintLayout>(R.id.advertise_layout)

        // Tambahkan penanganan klik
        advLayout.setOnClickListener {
            // Panggil finish() untuk mengakhiri aktivitas
            finish()
        }

    }
}