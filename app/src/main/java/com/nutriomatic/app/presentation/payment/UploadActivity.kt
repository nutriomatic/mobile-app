package com.nutriomatic.app.presentation.payment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.nutriomatic.app.R
import com.nutriomatic.app.databinding.ActivityUploadBinding
import com.nutriomatic.app.presentation.advertise.AdvertiseActivity

class UploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadBinding
    private var currentImageUri: Uri? = null
    private var countdownMillis: Long = 300000 // 5 menit dalam milidetik
    private lateinit var countDownTimer: CountDownTimer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        with(binding) {
            topAppBar.setNavigationOnClickListener { onBackPressed() }


        }

        val storeId = intent.getStringExtra(STORE_ID)

        if (storeId !== null) {

            Glide.with(this)
                .load("https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=${storeId}")
                .placeholder(R.drawable.loading_bar)
                .into(binding.ivQris)

            binding.ivBuktiBayar.setOnClickListener {
                startGallery()
            }
            binding.btnProses.setOnClickListener {
                val intent = Intent(this@UploadActivity, AdvertiseActivity::class.java)
                startActivity(intent)
                finish()
            }



            countDownTimer = object : CountDownTimer(countdownMillis, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val minutes = (millisUntilFinished / 1000) / 60
                    val seconds = (millisUntilFinished / 1000) % 60
                    binding.tvLabelWarning.text = resources.getString(
                        R.string.this_qr_code_will_expired_in_05_00,
                        minutes,
                        seconds
                    )
                }

                override fun onFinish() {
                    // Lakukan sesuatu saat countdown selesai
                    Toast.makeText(
                        this@UploadActivity,
                        "Sorrt, QR Code expired",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    finish()
                }
            }.start()
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.ivBuktiBayar.setImageURI(it)
        }
    }

    companion object {
        private val STORE_ID = "store_id"
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer.cancel()
    }
}