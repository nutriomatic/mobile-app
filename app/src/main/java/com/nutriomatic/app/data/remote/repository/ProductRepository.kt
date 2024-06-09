package com.nutriomatic.app.data.remote.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.nutriomatic.app.data.pref.UserPreference
import com.nutriomatic.app.data.remote.Result
import com.nutriomatic.app.data.remote.api.request.CreateProductRequest
import com.nutriomatic.app.data.remote.api.response.CreateProductResponse
import com.nutriomatic.app.data.remote.api.response.ErrorResponse
import com.nutriomatic.app.data.remote.api.response.ProductsResponse
import com.nutriomatic.app.data.remote.api.retrofit.ApiService
import retrofit2.HttpException
import java.io.File

class ProductRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService,
) {
    private val _products = MutableLiveData<Result<ProductsResponse>>()
    val products: LiveData<Result<ProductsResponse>> = _products

    private val _statusCreateProduct = MutableLiveData<Result<CreateProductResponse>>()
    val statusCreateProduct: LiveData<Result<CreateProductResponse>> = _statusCreateProduct


    suspend fun getProducts() {
        _products.value = Result.Loading
        try {
            val response = apiService.getProducts()
            _products.value = Result.Success(response)
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            _products.value = Result.Error(errorMessage ?: "An error occurred")
        }
    }

    suspend fun createProduct(
        product_name: String,
        product_price: Double,
        product_desc: String,
        product_isshow: Boolean = false,
        product_lemaktotal: Double,
        product_protein: Double,
        product_karbohidrat: Double,
        product_garam: Double,
        product_grade: String = "Z",
        productServingsize: Int,
        pt_name: String,
        productPicture: File,
    ) {
        _statusCreateProduct.value = Result.Loading
        try {
            val request = CreateProductRequest(
                product_name,
                product_price,
                product_desc,
                product_isshow,
                product_lemaktotal,
                product_protein,
                product_karbohidrat,
                product_garam,
                product_grade,
                productServingsize,
                pt_name,
                productPicture
            )
            val response = apiService.createProduct(request)
            _statusCreateProduct.value = Result.Success(response)
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            _statusCreateProduct.value = Result.Error(errorMessage ?: "An error occurred")
        }
    }

    companion object {
        @Volatile
        private var instance: ProductRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService,
        ): ProductRepository =
            instance ?: synchronized(this) {
                instance ?: ProductRepository(userPreference, apiService)
            }.also { instance = it }
    }
}