package com.nutriomatic.app.presentation.product

import android.content.Intent
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
import androidx.navigation.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.nutriomatic.app.R
import com.nutriomatic.app.data.local.LocalData
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

class AddProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddProductBinding
    private var currentImageUri: Uri? = null
    private val args: AddProductActivityArgs by navArgs()
    private val viewModel by viewModels<AddProductViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private var ptType = 0
    private var productIsShow = false

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

            val productTypes =
                LocalData.getProductTypeNames(this@AddProductActivity) // getProductTypes()
            val productTypeAdapter = ArrayAdapter(
                this@AddProductActivity, android.R.layout.simple_dropdown_item_1line, productTypes
            )
            selectType.setAdapter(productTypeAdapter)

            selectType.setOnItemClickListener { parent, view, position, id ->
                val selectedPtName = parent.getItemAtPosition(position).toString()
                ptType = LocalData.getProductTypeCodeByName(
                    this@AddProductActivity,
                    selectedPtName
                )
            }

            val grades = LocalData.getGradeNames()
            val productGradeAdapter = ArrayAdapter(
                this@AddProductActivity, android.R.layout.simple_dropdown_item_1line, grades
            )
            selectGrade.setAdapter(productGradeAdapter)

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

                                Glide.with(this@AddProductActivity)
                                    .load(product.productPicture)
                                    .into(binding.productImage)


                                txtNameInput.setText(product.productName)

                                productIsShow = product.productIsshow
                                txtProductDescInput.setText(product.productDesc)
                                txtPriceInput.setText(product.productPrice.toString())
                                txtServingSizePerContInput.setText(product.productServingsize.toString())
                                txtFatInput.setText(product.productLemaktotal.toString())
                                txtCarboInput.setText(product.productKarbohidrat.toString())
                                txtProteinInput.setText(product.productProtein.toString())
                                txtSodiumInput.setText(product.productGaram.toString())
                                val productTypeName = LocalData.getProductTypeNameByCode(
                                    this@AddProductActivity,
                                    product.ptCode
                                )
                                selectType.setText(productTypeName, false)
                                selectGrade.setText(product.productGrade, false)

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
        binding.selectType.setOnItemClickListener { parent, view, position, id ->
            val selectedPtName = parent.getItemAtPosition(position).toString()
            ptType = LocalData.getProductTypeCodeByName(
                this@AddProductActivity,
                selectedPtName
            )
        }

        val productName = binding.txtNameInput.text.toString()
        val productDesc = binding.txtProductDescInput.text.toString()
        val productPrice = binding.txtPriceInput.text.toString()
        val productIsshow = productIsShow
        val productLemakTotal = binding.txtFatInput.text.toString()
        val productProtein = binding.txtCarboInput.text.toString()
        val productKarbohidrat = binding.txtProteinInput.text.toString()
        val productGaram = binding.txtSodiumInput.text.toString()
        val productGrade = binding.selectGrade.text.toString()
        val productServingSize = binding.txtServingSizePerContInput.text.toString()

        var body: MultipartBody.Part? = null
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(this, uri).reduceFileSize()
            val requestFile = imageFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            body = MultipartBody.Part.createFormData("file", imageFile.name, requestFile)
        }

        viewModel.updateProduct(
            id = productId,
            productName = productName,
            productPrice = productPrice.toDouble(),
            productDesc = productDesc,
            productIsShow = productIsshow,
            productLemakTotal = productLemakTotal.toDouble(),
            productProtein = productProtein.toDouble(),
            productKarbohidrat = productKarbohidrat.toDouble(),
            productGaram = productGaram.toDouble(),
            productGrade = productGrade,
            productServingSize = productServingSize.toInt(),
            ptType = ptType,
            file = body
        )

        viewModel.statusUpdateProduct.observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        Snackbar.make(
                            this@AddProductActivity,
                            binding.root,
                            result.data.message.toString(),
                            Snackbar.LENGTH_SHORT
                        ).show()
//                        finish()
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


    private fun createProduct() {
        currentImageUri?.let { uri ->
            val productName = binding.txtNameInput.text.toString()
            val productPrice = binding.txtPriceInput.text.toString().ifEmpty { "0" }
            val productDesc = binding.txtProductDescInput.text.toString()
            val productIsshow = false
            val productLemakTotal = binding.txtFatInput.text.toString().ifEmpty { "0" }
            val productProtein = binding.txtCarboInput.text.toString().ifEmpty { "0" }
            val productKarbohidrat = binding.txtProteinInput.text.toString().ifEmpty { "0" }
            val productGaram = binding.txtSodiumInput.text.toString().ifEmpty { "0" }
            val productGrade = binding.selectGrade.text.toString()
            val productServingSize =
                binding.txtServingSizePerContInput.text.toString().ifEmpty { "1" }
            val imageFile = uriToFile(this, uri).reduceFileSize()
            val requestFile = imageFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("file", imageFile.name, requestFile)
            Log.d("TYPE", ptType.toString())

            viewModel.createProduct(
                productName,
                productPrice.toDouble(),
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
        } ?: showToast(getString(R.string.please_choose_a_valid_image))
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