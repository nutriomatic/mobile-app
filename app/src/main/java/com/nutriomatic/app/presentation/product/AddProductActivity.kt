package com.nutriomatic.app.presentation.product

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.navigation.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.nutriomatic.app.BuildConfig
import com.nutriomatic.app.data.remote.Result
import com.nutriomatic.app.data.remote.api.response.Product
import com.nutriomatic.app.databinding.ActivityAddProductBinding
import com.nutriomatic.app.presentation.advertise.AdvertiseActivity
import com.nutriomatic.app.presentation.factory.ViewModelFactory
import com.nutriomatic.app.presentation.helper.util.reduceFileSize
import com.nutriomatic.app.presentation.helper.util.uriToFile
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class AddProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddProductBinding
    private var currentImageUri: Uri? = null
    private val args: AddProductActivityArgs by navArgs()
    private val viewModel by viewModels<AddProductViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private var ptType = 0;


    private fun getProductTypes(): List<String> {
        return typeProduct.map { it["pt_name"].toString() }
    }

    private fun getProductTypeCode(ptName: String): Int {
        return typeProduct.find { it["pt_name"] == ptName }?.get("pt_type")?.toString()
            ?.toIntOrNull() ?: 0
    }

    private fun getProductTypeName(ptCode: Int): String? {
        return typeProduct.find { it["pt_type"] == ptCode }?.get("pt_name")?.toString()
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

            val productTypes = getProductTypes()
            val adapter = ArrayAdapter(
                this@AddProductActivity, android.R.layout.simple_dropdown_item_1line, productTypes
            )
            selectType.setAdapter(adapter)

            selectType.setOnItemClickListener { parent, view, position, id ->
                val selectedPtName = parent.getItemAtPosition(position).toString()
                ptType = getProductTypeCode(selectedPtName)
                Toast.makeText(
                    this@AddProductActivity, ptType.toString() + "helo", Toast.LENGTH_SHORT
                ).show()
            }

            btnSave.setOnClickListener { createProduct() }
            binding.btnAdv.setOnClickListener {
                showToast("Save product first!")
            }


            args.productId?.let { id ->
                viewModel.getProductById(id)
                topAppBar.title = "Update Product"

                viewModel.detailProduct.observe(this@AddProductActivity) { result ->
                    if (result != null) {
                        when (result) {
                            is Result.Loading -> {
                                binding.progressBar.visibility = View.VISIBLE
                            }

                            is Result.Success -> {
                                binding.progressBar.visibility = View.GONE
                                val product: Product = result.data.product
//                                Glide.with(this@AddProductActivity).load(product.productPicture)
//                                    .into(productImage)

                                Glide.with(this@AddProductActivity)
                                    .load(product.productPicture)
                                    .into(object : CustomTarget<Drawable>() {
                                        override fun onResourceReady(
                                            resource: Drawable,
                                            transition: Transition<in Drawable>?
                                        ) {
                                            // Simpan drawable ke file
                                            val file = File(cacheDir, "image.jpg")
                                            try {
                                                val outputStream = FileOutputStream(file)
                                                val bitmap = (resource as BitmapDrawable).bitmap
                                                bitmap.compress(
                                                    Bitmap.CompressFormat.JPEG,
                                                    100,
                                                    outputStream
                                                )
                                                outputStream.close()

                                                // Dapatkan URI dari file
                                                currentImageUri = FileProvider.getUriForFile(
                                                    this@AddProductActivity,
                                                    "${BuildConfig.APPLICATION_ID}.provider", // Ubah sesuai aplikasi Anda
                                                    file
                                                )
                                            } catch (e: IOException) {
                                                e.printStackTrace()
                                            }

                                            // Set gambar di ImageView
                                            binding.productImage.setImageDrawable(resource)
                                        }

                                        override fun onLoadCleared(placeholder: Drawable?) {
                                            // Handle jika diperlukan
                                        }
                                    })


//                                currentImageUri?.let {
//                                    binding.productImage.setImageURI(it)
//                                }


                                txtNameInput.setText(product.productName)
                                Log.d("KODEPT", product.ptCode.toString())
                                val ptName = getProductTypeName(product.ptCode)

                                val position = adapter.getPosition(ptName)
                                if (position != -1) {
                                    selectType.setText(adapter.getItem(position), false)
                                }

                                txtProductDescInput.setText(product.productDesc)
                                txtServingSizePerContInput.setText(product.productServingsize.toString())
                                txtFatInput.setText(product.productLemaktotal.toString())
                                txtCarboInput.setText(product.productKarbohidrat.toString())
                                txtProteinInput.setText(product.productProtein.toString())
                                txtSodiumInput.setText(product.productGaram.toString())

                                // update
                                btnSave.setOnClickListener { updateProduct(product.productId) }
                                btnAdv.setOnClickListener { advertiseProduct(product.productId) }
                            }

                            is Result.Error -> {
                                binding.progressBar.visibility = View.GONE
                                Toast.makeText(
                                    this@AddProductActivity, result.error, Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun advertiseProduct(productId: String) {
        viewModel.advertiseProduct(productId)

        viewModel.statusAdvertiseProduct.observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        val intent = Intent(this, AdvertiseActivity::class.java)
                        startActivity(intent)
                        finish()
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
    }

    private fun updateProduct(productId: String) {

        currentImageUri?.let { uri ->

            binding.selectType.setOnItemClickListener { parent, view, position, id ->
                val selectedPtName = parent.getItemAtPosition(position).toString()
                ptType = getProductTypeCode(selectedPtName)
            }

            val productName = binding.txtNameInput.text.toString()
            val productPrice = 0.0
            val productDesc = binding.txtProductDescInput.text.toString()
            val productIsshow = false
            val productLemakTotal = binding.txtFatInput.text.toString()
            val productProtein = binding.txtCarboInput.text.toString()
            val productKarbohidrat = binding.txtProteinInput.text.toString()
            val productGaram = binding.txtSodiumInput.text.toString()
            val productGrade = "Z"
            val productServingSize = binding.txtServingSizePerContInput.text.toString()

            val imageFile = uriToFile(this, uri).reduceFileSize()
            val requestFile = imageFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("file", imageFile.name, requestFile)

            viewModel.updateProduct(
                productId,
                productName,
//                productPrice,
                productDesc,
                productLemakTotal.toDouble(),
                productProtein.toDouble(),
                productKarbohidrat.toDouble(),
                productGaram.toDouble(),
                productGrade,
                productServingSize.toInt(),
                ptType,
                body
            )

            viewModel.statusUpdateProduct.observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is Result.Success -> {
                            binding.progressBar.visibility = View.GONE
                            finish()
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

        } ?: showToast("Please choose a valid image!")

    }


    private fun createProduct() {


        currentImageUri?.let { uri ->
            val productName = binding.txtNameInput.text.toString()
            val productPrice = 0.0
            val productDesc = binding.txtProductDescInput.text.toString()
            val productIsshow = false
            val productLemakTotal = binding.txtFatInput.text.toString()
            val productProtein = binding.txtCarboInput.text.toString()
            val productKarbohidrat = binding.txtProteinInput.text.toString()
            val productGaram = binding.txtSodiumInput.text.toString()
            val productGrade = "Z"
            val productServingSize = binding.txtServingSizePerContInput.text.toString()
//            val ptName = binding.selectType.text.toString()


            val imageFile = uriToFile(this, uri).reduceFileSize()
            val requestFile = imageFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("file", imageFile.name, requestFile)
            Log.d("TYPE", ptType.toString())

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
                ptType,
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
                            finish()
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
        } ?: showToast("Please choose a valid image!")

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

    companion object {
        val typeProduct = arrayOf(
            mapOf(
                "pt_name" to "Snacks", "pt_type" to 201
            ), mapOf(
                "pt_name" to "Chocolate Bar", "pt_type" to 202
            ), mapOf(
                "pt_name" to "Ice Cream", "pt_type" to 203
            ), mapOf(
                "pt_name" to "Bread", "pt_type" to 204
            ), mapOf(
                "pt_name" to "Cheese", "pt_type" to 205
            ), mapOf(
                "pt_name" to "Frozen Food", "pt_type" to 206
            ), mapOf(
                "pt_name" to "Water", "pt_type" to 301
            ), mapOf(
                "pt_name" to "Juice", "pt_type" to 302
            ), mapOf(
                "pt_name" to "Milk", "pt_type" to 303
            ), mapOf(
                "pt_name" to "Coffee", "pt_type" to 304
            ), mapOf(
                "pt_name" to "Tea", "pt_type" to 305
            )
        )
    }


//    private fun showLoading(isLoading: Boolean) {
//        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
//    }
}