package com.nutriomatic.app.presentation.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nutriomatic.app.data.remote.Result
import com.nutriomatic.app.data.remote.api.response.BasicResponse
import com.nutriomatic.app.data.remote.api.response.CreateProductResponse
import com.nutriomatic.app.data.remote.api.response.ProductByIdResponse
import com.nutriomatic.app.data.remote.repository.ProductRepository
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class AddProductViewModel(private val repository: ProductRepository) : ViewModel() {
    val statusCreateProduct: LiveData<Result<CreateProductResponse>> =
        repository.statusCreateProduct
    val statusUpdateProduct: LiveData<Result<BasicResponse>> =
        repository.statusUpdateProduct
    val statusAdvertiseProduct: LiveData<Result<BasicResponse>> =
        repository.statusAdvertiseProduct
    val detailProduct: LiveData<Result<ProductByIdResponse>> = repository.detailProduct


    fun createProduct(
        productName: String,
        productPrice: Double,
        productDesc: String,
        productIsshow: Boolean,
        productLemakTotal: Double,
        productProtein: Double,
        productKarbohidrat: Double,
        productGaram: Double,
        productGrade: String,
        productServingSize: Int,
        ptType: Int,
        file: MultipartBody.Part
    ) {
        viewModelScope.launch {
            val productNameBody = createRequestBody(productName)
            val productPriceBody = createRequestBody(productPrice.toString())
            val productDescBody = createRequestBody(productDesc)
            val productIsshowBody =
                createRequestBody(productIsshow.toString())  // Convert Boolean to String
            val productLemakTotalBody = createRequestBody(productLemakTotal.toString())
            val productProteinBody = createRequestBody(productProtein.toString())
            val productKarbohidratBody = createRequestBody(productKarbohidrat.toString())
            val productGaramBody = createRequestBody(productGaram.toString())
            val productGradeBody = createRequestBody(productGrade)
            val productServingSizeBody = createRequestBody(productServingSize.toString())
            val ptNameBody = createRequestBody(ptType.toString())

            repository.createProduct(
                productNameBody,
                productPriceBody,
                productDescBody,
                productIsshowBody,
                productLemakTotalBody,
                productProteinBody,
                productKarbohidratBody,
                productGaramBody,
                productGradeBody,
                productServingSizeBody,
                ptNameBody,
                file
            )
        }
    }


    fun updateProduct(
        id: String,
        productName: String,
        productPrice: Double,
        productDesc: String,
        productIsShow: Boolean,
        productLemakTotal: Double,
        productProtein: Double,
        productKarbohidrat: Double,
        productGaram: Double,
        productGrade: String,
        productServingSize: Int,
        ptType: Int,
        file: MultipartBody.Part?
    ) {
        viewModelScope.launch {
            val productNameBody = createRequestBody(productName)
            val productPriceBody = createRequestBody(productPrice.toString())
            val productDescBody = createRequestBody(productDesc)
            val productIsShowBody = createRequestBody(productIsShow.toString())
            val productLemakTotalBody = createRequestBody(productLemakTotal.toString())
            val productProteinBody = createRequestBody(productProtein.toString())
            val productKarbohidratBody = createRequestBody(productKarbohidrat.toString())
            val productGaramBody = createRequestBody(productGaram.toString())
            val productGradeBody = createRequestBody(productGrade)
            val productServingSizeBody = createRequestBody(productServingSize.toString())
            val ptNameBody = createRequestBody(ptType.toString())

            repository.updateProduct(
                id,
                productNameBody,
                productPriceBody,
                productDescBody,
                productIsShowBody,
                productLemakTotalBody,
                productProteinBody,
                productKarbohidratBody,
                productGaramBody,
                productGradeBody,
                productServingSizeBody,
                ptNameBody,
                file
            )
        }
    }


    fun getProductById(id: String) {
        viewModelScope.launch {
            repository.getProductById(id)
        }
    }

    fun advertiseProduct(id: String) {
        viewModelScope.launch {
            repository.advertiseProduct(id)
        }
    }

    private fun createRequestBody(value: String): RequestBody {
        return value.toRequestBody("text/plain".toMediaTypeOrNull())
    }

}