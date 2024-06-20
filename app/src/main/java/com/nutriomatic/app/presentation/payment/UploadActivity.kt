package com.nutriomatic.app.presentation.payment

import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.nutriomatic.app.R
import com.nutriomatic.app.data.remote.Result
import com.nutriomatic.app.databinding.ActivityUploadBinding
import com.nutriomatic.app.presentation.factory.ViewModelFactory
import com.nutriomatic.app.presentation.helper.util.reduceFileSize
import com.nutriomatic.app.presentation.helper.util.uriToFile
import com.nutriomatic.app.presentation.store.PaymentViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

class UploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadBinding
    private var currentImageUri: Uri? = null
    private var countdownMillis: Long = 300000 // 5 menit dalam milidetik
    private lateinit var countDownTimer: CountDownTimer
    private val viewModel by viewModels<PaymentViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        with(binding) {
//            topAppBar.setNavigationOnClickListener { onBackPressed() }

            ivBuktiBayar.setOnClickListener {
                startGallery()
            }
        }

        val storeId = intent.getStringExtra(STORE_ID)

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
                Toast.makeText(
                    this@UploadActivity,
                    "Sorry, QR Code expired",
                    Toast.LENGTH_SHORT
                )
                    .show()
                finish()
            }
        }.start()


        if (storeId !== null) {
            Glide.with(this)
                .load("https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=${storeId}")
                .placeholder(R.drawable.loading_bar)
                .into(binding.ivQris)

            binding.btnProses.setOnClickListener {
                currentImageUri?.let { uri ->
                    val imageFile = uriToFile(this@UploadActivity, uri).reduceFileSize()
                    val requestFile =
                        imageFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                    val bodyPhoto =
                        MultipartBody.Part.createFormData("file", imageFile.name, requestFile)

                    viewModel.uploadProofTransaction(file = bodyPhoto)

                    viewModel.statusUploadProof.observe(this@UploadActivity) { result ->
                        if (result != null) {
                            when (result) {
                                is Result.Loading -> {
                                    binding.progressBar.visibility = View.VISIBLE
                                }

                                is Result.Success -> {
                                    binding.progressBar.visibility = View.GONE
                                    Snackbar.make(
                                        binding.root,
                                        result.data.message.toString(),
                                        Snackbar.LENGTH_SHORT
                                    ).show()

//                                    val intent =
//                                        Intent(this@UploadActivity, AdvertiseActivity::class.java)
//                                    startActivity(intent)
//                                    finish()
                                }

                                is Result.Error -> {
                                    binding.progressBar.visibility = View.GONE

                                    Snackbar.make(
                                        binding.root,
                                        result.error,
                                        Snackbar.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }
                } ?: showToast(getString(R.string.please_choose_a_valid_image))
            }
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

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer.cancel()
    }
}