package com.nutriomatic.app.presentation.product

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.navArgs
import com.bumptech.glide.Glide
import com.nutriomatic.app.R
import com.nutriomatic.app.data.fake.FakeDataSource
import com.nutriomatic.app.data.fake.model.Product
import com.nutriomatic.app.databinding.ActivityAddProductBinding
import com.nutriomatic.app.presentation.factory.ViewModelFactory
import com.nutriomatic.app.presentation.helper.util.reduceFileSize
import com.nutriomatic.app.presentation.helper.util.uriToFile
import java.util.UUID

class AddProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddProductBinding
    private var currentImageUri: Uri? = null
    private val args: AddProductActivityArgs by navArgs()
    private val viewModel by viewModels<AddProductViewModel> {
        ViewModelFactory.getInstance(this)
    }

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

            btnSave.setOnClickListener { createProduct() }

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

    private fun createProduct() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(this, uri).reduceFileSize()
            val name = binding.txtNameInput.text.toString()
            val type = binding.selectType.text.toString()
            val description = binding.txtProductDescInput.text.toString()
            val servisSizeCont = binding.txtServingSizePerContInput.text.toString()
            val fat = binding.txtFatInput.text.toString()
            val carbohidrat = binding.txtCarboInput.text.toString()
            val protein = binding.txtProteinInput.text.toString()
            val sodium = binding.txtSodiumInput.text.toString()

//            val requestBody = description.toRequestBody("text/plain".toMediaType())
//            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
//            val multipartBody = MultipartBody.Part.createFormData(
//                "photo", imageFile.name, requestImageFile
//            )
            viewModel.createProduct(
                name,
                20.0,
                description,
                false,
                fat.toDouble(),
                protein.toDouble(),
                carbohidrat.toDouble(),
                sodium.toDouble(),
                "Z",
                servisSizeCont.toInt(),
                type,
                imageFile
            )

//            viewModel.statusCreateProduct.observe(this) { result ->
//                if (result != null) {
//                    when (result) {
//                        is Result.Loading -> {
//                            binding.progressIndicator.visibility = View.VISIBLE
//                        }
//
//                        is Result.Success -> {
//                            binding.progressIndicator.visibility = View.GONE
//                            val intent = Intent(this, MainActivity::class.java)
//                            intent.flags =
//                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//                            startActivity(intent)
//                        }
//
//                        is Result.Error -> {
//                            binding.progressIndicator.visibility = View.GONE
//                            Toast.makeText(
//                                this, result.error, Toast.LENGTH_SHORT
//                            ).show()
//                        }
//                    }
//                }
//            }
        } ?: showToast("500: Internal server error")
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

//    private fun showLoading(isLoading: Boolean) {
//        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
//    }
}