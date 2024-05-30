package com.nutriomatic.app.presentation.preview

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.navArgs
import com.nutriomatic.app.databinding.ActivityPreviewBinding

class PreviewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPreviewBinding
    private val args: PreviewActivityArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            ivPhotoPreview.setImageURI(Uri.parse(args.imageUriString))
            btnRetake.setOnClickListener { finish() }
            btnSubmit.setOnClickListener { finish() }
        }
    }
}