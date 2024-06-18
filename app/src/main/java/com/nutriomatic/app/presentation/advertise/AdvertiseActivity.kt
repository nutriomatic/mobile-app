package com.nutriomatic.app.presentation.advertise

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.nutriomatic.app.R
import com.nutriomatic.app.presentation.store.StoreFragment

class AdvertiseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_advertise)

        supportActionBar?.hide()

        val advLayout = findViewById<ConstraintLayout>(R.id.advertise_layout)

        advLayout.setOnClickListener {
//            val fragment = StoreFragment()
//            val fragmentManager = supportFragmentManager
//            val fragmentTransaction = fragmentManager.beginTransaction()
//
//            // Tambahkan animasi
//            fragmentTransaction.setCustomAnimations(
//                android.R.anim.fade_in,  // Enter animation
//                android.R.anim.fade_out, // Exit animation
//                android.R.anim.fade_in,  // Pop enter animation
//                android.R.anim.fade_out  // Pop exit animation
//            )
//
//            fragmentTransaction.replace(R.id.fragmentContainerMain, fragment)
//            fragmentTransaction.addToBackStack(null)
//            fragmentTransaction.commit()

            finish()
        }

    }
}