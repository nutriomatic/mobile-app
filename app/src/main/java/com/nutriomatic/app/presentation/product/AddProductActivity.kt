package com.nutriomatic.app.presentation.product

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navArgs
import com.bumptech.glide.Glide
import com.nutriomatic.app.R
import com.nutriomatic.app.data.fake.FakeDataSource
import com.nutriomatic.app.data.remote.Result
import com.nutriomatic.app.databinding.ActivityAddProductBinding
import com.nutriomatic.app.presentation.factory.ViewModelFactory
import com.nutriomatic.app.presentation.helper.util.reduceFileSize
import com.nutriomatic.app.presentation.helper.util.uriToFile
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
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

//                val product: Product? = FakeDataSource.getProductById(UUID.fromString(id))
//                product?.let {
//                    Glide.with(this@AddProductActivity)
//                        .load(it.photoUrl)
//                        .into(productImage)
//                    txtNameInput.setText(it.name)
//                    selectType.setText(it.type)
//                    txtProductDescInput.setText(it.description)
//                    txtServingSizePerContInput.setText(it.servingSizePerContainer.toString())
//                    txtFatInput.setText(it.fatGrams.toString())
//                    txtCarboInput.setText(it.carbohydratesGrams.toString())
//                    txtProteinInput.setText(it.proteinGrams.toString())
//                    txtSodiumInput.setText(it.sodiumMilliGrams.toString())
//                }
            }
        }
    }


    private fun createProduct() {
        currentImageUri?.let { uri ->
            val productName = binding.txtNameInput.text.toString()
            val productPrice = 12.99
            val productDesc = binding.txtProductDescInput.text.toString()
            val productIsshow = false
            val productLemakTotal = binding.txtFatInput.text.toString()
            val productProtein = binding.txtCarboInput.text.toString()
            val productKarbohidrat = binding.txtProteinInput.text.toString()
            val productGaram = binding.txtSodiumInput.text.toString()
            val productGrade = "Z"
            val productServingSize = binding.txtServingSizePerContInput.text.toString()
            val ptName = binding.selectType.text.toString()

            val imageFile = uriToFile(this, uri).reduceFileSize()
            val requestFile = imageFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("file", imageFile.name, requestFile)
            Log.d("GAMBAR", imageFile.name)

            viewModel.createProduct(
                productName,
                productPrice,
                productDesc,
                productIsshow,
                productLemakTotal.toDouble(),
                productProtein.toDouble(),
                productKarbohidrat.toDouble(),
                productGaram.toDouble(),
                productGrade,
                productServingSize.toInt(),
                ptName,
                body
            )

            viewModel.statusCreateProduct.observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is Result.Success -> {
                            binding.progressBar.visibility = View.GONE
                            val navHostFragment =
                                supportFragmentManager.findFragmentById(R.id.main_navigation) as NavHostFragment
                            val navController = navHostFragment.navController
                            navController.popBackStack(R.id.storeFragment, false)
                        }

                        is Result.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(
                                this, result.error, Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
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