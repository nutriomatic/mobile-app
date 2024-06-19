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
import com.nutriomatic.app.presentation.helper.util.createRequestBodyText
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

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
            val productNameBody = createRequestBodyText(productName)
            val productPriceBody = createRequestBodyText(productPrice.toString())
            val productDescBody = createRequestBodyText(productDesc)
            val productIsshowBody = createRequestBodyText(productIsshow.toString())
            val productEnergiBody = createRequestBodyText(productEnergi.toString())
            val productGulaBody = createRequestBodyText(productGula.toString())
            val productLemakTotalBody = createRequestBodyText(productLemakTotal.toString())
            val productProteinBody = createRequestBodyText(productProtein.toString())
            val productKarbohidratBody = createRequestBodyText(productKarbohidrat.toString())
            val productGaramBody = createRequestBodyText(productGaram.toString())
            val productGradeBody = createRequestBodyText(productGrade)
            val productServingSizeBody = createRequestBodyText(productServingSize.toString())
            val ptNameBody = createRequestBodyText(ptType.toString())

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
            val productNameBody = createRequestBodyText(productName)
            val productPriceBody = createRequestBodyText(productPrice.toString())
            val productDescBody = createRequestBodyText(productDesc)
            val productIsShowBody = createRequestBodyText(productIsShow.toString())
            val productEnergiBody = createRequestBodyText(productEnergi.toString())
            val productGulaBody = createRequestBodyText(productGula.toString())
            val productLemakTotalBody = createRequestBodyText(productLemakTotal.toString())
            val productProteinBody = createRequestBodyText(productProtein.toString())
            val productKarbohidratBody = createRequestBodyText(productKarbohidrat.toString())
            val productGaramBody = createRequestBodyText(productGaram.toString())
            val productGradeBody = createRequestBodyText(productGrade)
            val productServingSizeBody = createRequestBodyText(productServingSize.toString())
            val ptNameBody = createRequestBodyText(ptType.toString())

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

    fun updateProductIsShow(
        id: String,
        productIsShow: Int,
    ) {
        viewModelScope.launch {
            val productIsShowBody = createRequestBodyText(productIsShow.toString())

            productRepository.updateProductIsShow(
                id = id,
                productIsshow = productIsShowBody
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
}