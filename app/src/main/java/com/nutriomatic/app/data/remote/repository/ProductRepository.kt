package com.nutriomatic.app.data.remote.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.nutriomatic.app.data.pref.UserPreference
import com.nutriomatic.app.data.remote.Result
import com.nutriomatic.app.data.remote.api.response.ErrorResponse
import com.nutriomatic.app.data.remote.api.response.ProductsResponse
import com.nutriomatic.app.data.remote.api.response.ProfileResponse
import com.nutriomatic.app.data.remote.api.retrofit.ApiService
import retrofit2.HttpException

class ProductRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService,
) {
    private val _products = MutableLiveData<Result<ProductsResponse>>()
    val products: LiveData<Result<ProductsResponse>> = _products


    suspend fun getProducts() {
        _products.value = Result.Loading
        try {
//            val request = LoginRequest(email, password)
            val response = apiService.getProducts()
            _products.value = Result.Success(response)
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            _products.value = Result.Error(errorMessage ?: "An error occurred")
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