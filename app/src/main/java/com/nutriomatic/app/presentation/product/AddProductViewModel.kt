package com.nutriomatic.app.presentation.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nutriomatic.app.data.remote.Result
import com.nutriomatic.app.data.remote.api.response.BasicResponse
import com.nutriomatic.app.data.remote.api.response.CreateProductResponse
import com.nutriomatic.app.data.remote.api.response.ProductByIdResponse
import com.nutriomatic.app.data.remote.repository.ProductRepository
import com.nutriomatic.app.data.remote.repository.TransactionRepository
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class AddProductViewModel(
    private val productRepository: ProductRepository,
    private val transactionRepository: TransactionRepository,
) : ViewModel() {
    val statusCreateProduct: LiveData<Result<CreateProductResponse>> =
        productRepository.statusCreateProduct
    val statusUpdateProduct: LiveData<Result<BasicResponse>> =
        productRepository.statusUpdateProduct
    val statusAdvertiseProduct: LiveData<Result<BasicResponse>> =
        productRepository.statusAdvertiseProduct
    val statusDeleteProduct: LiveData<Result<BasicResponse>> =
        productRepository.statusDeleteProduct
    val detailProduct: LiveData<Result<ProductByIdResponse>> = productRepository.detailProduct
    val statusCreateTransaction: LiveData<Result<BasicResponse>> =
        transactionRepository.statusCreateTransaction

    fun createProduct(
        productName: String,
        productPrice: Double,
        productDesc: String,
        productIsshow: Int = 0,
        productEnergi: Double,
        productGula: Double,
        productLemakTotal: Double,
        productProtein: Double,
        productKarbohidrat: Double,
        productGaram: Double,
        productGrade: String,
        productServingSize: Int,
        ptType: Int,
        file: MultipartBody.Part,
    ) {
        viewModelScope.launch {
            val productNameBody = createRequestBody(productName)
            val productPriceBody = createRequestBody(productPrice.toString())
            val productDescBody = createRequestBody(productDesc)
            val productIsshowBody = createRequestBody(productIsshow.toString())
            val productEnergiBody = createRequestBody(productEnergi.toString())
            val productGulaBody = createRequestBody(productGula.toString())
            val productLemakTotalBody = createRequestBody(productLemakTotal.toString())
            val productProteinBody = createRequestBody(productProtein.toString())
            val productKarbohidratBody = createRequestBody(productKarbohidrat.toString())
            val productGaramBody = createRequestBody(productGaram.toString())
            val productGradeBody = createRequestBody(productGrade)
            val productServingSizeBody = createRequestBody(productServingSize.toString())
            val ptNameBody = createRequestBody(ptType.toString())

            productRepository.createProduct(
                productName = productNameBody,
                productPrice = productPriceBody,
                productDesc = productDescBody,
                productIsshow = productIsshowBody,
                productEnergi = productEnergiBody,
                productGula = productGulaBody,
                productLemakTotal = productLemakTotalBody,
                productProtein = productProteinBody,
                productKarbohidrat = productKarbohidratBody,
                productGaram = productGaramBody,
                productGrade = productGradeBody,
                productServingSize = productServingSizeBody,
                ptName = ptNameBody,
                body = file
            )
        }
    }


    fun updateProduct(
        id: String,
        productName: String,
        productPrice: Double,
        productDesc: String,
        productIsShow: Int,
        productEnergi: Double,
        productGula: Double,
        productLemakTotal: Double,
        productProtein: Double,
        productKarbohidrat: Double,
        productGaram: Double,
        productGrade: String,
        productServingSize: Int,
        ptType: Int,
        file: MultipartBody.Part?,
    ) {
        viewModelScope.launch {
            val productNameBody = createRequestBody(productName)
            val productPriceBody = createRequestBody(productPrice.toString())
            val productDescBody = createRequestBody(productDesc)
            val productIsShowBody = createRequestBody(productIsShow.toString())
            val productEnergiBody = createRequestBody(productEnergi.toString())
            val productGulaBody = createRequestBody(productGula.toString())
            val productLemakTotalBody = createRequestBody(productLemakTotal.toString())
            val productProteinBody = createRequestBody(productProtein.toString())
            val productKarbohidratBody = createRequestBody(productKarbohidrat.toString())
            val productGaramBody = createRequestBody(productGaram.toString())
            val productGradeBody = createRequestBody(productGrade)
            val productServingSizeBody = createRequestBody(productServingSize.toString())
            val ptNameBody = createRequestBody(ptType.toString())

            productRepository.updateProduct(
                id = id,
                productName = productNameBody,
                productPrice = productPriceBody,
                productDesc = productDescBody,
                productIsshow = productIsShowBody,
                productEnergi = productEnergiBody,
                productGula = productGulaBody,
                productLemakTotal = productLemakTotalBody,
                productProtein = productProteinBody,
                productKarbohidrat = productKarbohidratBody,
                productGaram = productGaramBody,
                productGrade = productGradeBody,
                productServingSize = productServingSizeBody,
                ptName = ptNameBody,
                body = file
            )
        }
    }


    fun getProductById(id: String) {
        viewModelScope.launch {
            productRepository.getProductById(id)
        }
    }

    fun advertiseProduct(id: String) {
        viewModelScope.launch {
            productRepository.advertiseProduct(id)
        }
    }


    fun deleteProductById(id: String) {
        viewModelScope.launch {
            productRepository.deleteProductById(id)
        }
    }


    // transaction

    fun createTransaction(product_id: String) {
        viewModelScope.launch {
            transactionRepository.createTransaction(product_id)
        }
    }

    private fun createRequestBody(value: String): RequestBody {
        return value.toRequestBody("text/plain".toMediaTypeOrNull())
    }

}