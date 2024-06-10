package com.nutriomatic.app.data.remote.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.nutriomatic.app.data.remote.Result
import com.nutriomatic.app.data.remote.api.request.CreateStoreRequest
import com.nutriomatic.app.data.remote.api.response.BasicResponse
import com.nutriomatic.app.data.remote.api.response.ErrorResponse
import com.nutriomatic.app.data.remote.api.retrofit.ApiService
import retrofit2.HttpException

class StoreRepository(private val apiService: ApiService) {
    private val _createStoreResponse = MutableLiveData<Result<BasicResponse>>()
    val createStoreResponse: LiveData<Result<BasicResponse>> = _createStoreResponse

    suspend fun createStore(
        storeName: String,
        storeUsername: String,
        storeAddress: String,
        storeContact: String,
    ) {
        _createStoreResponse.value = Result.Loading
        try {
            val request = CreateStoreRequest(storeName, storeUsername, storeAddress, storeContact)
            val response = apiService.createStore(request)
            _createStoreResponse.value = Result.Success(response)
        } catch (e: HttpException) {
            val jsonString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            _createStoreResponse.value = Result.Error(errorMessage ?: "An error occurred")
        }
    }

    companion object {
        @Volatile
        private var instance: StoreRepository? = null
        fun getInstance(apiService: ApiService): StoreRepository = instance ?: synchronized(this) {
            instance ?: StoreRepository(apiService)
        }.also { instance = it }
    }
}