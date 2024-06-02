package com.nutriomatic.app.presentation.product

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.navArgs
import com.bumptech.glide.Glide
import com.nutriomatic.app.data.fake.FakeDataSource
import com.nutriomatic.app.data.fake.model.Product
import com.nutriomatic.app.databinding.ActivityAddProductBinding
import java.util.UUID

class AddProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddProductBinding
    private var currentImageUri: Uri? = null
    private val args: AddProductActivityArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.apply {
            topAppBar.setNavigationOnClickListener { onBackPressed() }

            productImage.setOnClickListener {
                startGallery()
            }

            args.productId?.let { id ->
                val product: Product? = FakeDataSource.getProductById(UUID.fromString(id))
                product?.let {
                    Glide.with(this@AddProductActivity)
                        .load(it.photoUrl)
                        .into(productImage)
                    txtNameInput.setText(it.name)
                    selectType.setText(it.type)
                    txtProductDescInput.setText(it.description)
                    txtServingSizePerContInput.setText(it.servingSizePerContainer.toString())
                    txtFatInput.setText(it.fatGrams.toString())
                    txtCarboInput.setText(it.carbohydratesGrams.toString())
                    txtProteinInput.setText(it.proteinGrams.toString())
                    txtSodiumInput.setText(it.sodiumMilliGrams.toString())
                }
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
            binding.productImage.setImageURI(it)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}