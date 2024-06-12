package com.nutriomatic.app.data.remote.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.nutriomatic.app.data.remote.Result
import com.nutriomatic.app.data.remote.api.request.StoreRequest
import com.nutriomatic.app.data.remote.api.response.BasicResponse
import com.nutriomatic.app.data.remote.api.response.ErrorResponse
import com.nutriomatic.app.data.remote.api.response.StoreResponse
import com.nutriomatic.app.data.remote.api.retrofit.ApiService
import retrofit2.HttpException

class StoreRepository(private val apiService: ApiService) {
    private val _createStoreResponse = MutableLiveData<Result<BasicResponse>>()
    val createStoreResponse: LiveData<Result<BasicResponse>> = _createStoreResponse
    private val _updateStoreStatus = MutableLiveData<Result<BasicResponse>>()
    val updateStoreResponse: LiveData<Result<BasicResponse>> = _updateStoreStatus
    private val _store = MutableLiveData<Result<StoreResponse>>()
    val store: LiveData<Result<StoreResponse>> = _store

    suspend fun createStore(
        storeName: String,
        storeUsername: String,
        storeAddress: String,
        storeContact: String,
    ) {
        _createStoreResponse.value = Result.Loading
        try {
            val request = StoreRequest(storeName, storeUsername, storeAddress, storeContact)
            val response = apiService.createStore(request)
            _createStoreResponse.value = Result.Success(response)
        } catch (e: HttpException) {
            val jsonString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            _createStoreResponse.value = Result.Error(errorMessage ?: "An error occurred")
        }
    }

    suspend fun getStore(
    ) {
        _store.value = Result.Loading
        try {
            val response = apiService.getStore()
            _store.value = Result.Success(response)
        } catch (e: HttpException) {
            val jsonString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            _store.value = Result.Error(errorMessage ?: "An error occurred")
        }
    }


    suspend fun updateStore(
        storeName: String,
        storeUsername: String,
        storeAddress: String,
        storeContact: String,
    ) {
        _updateStoreStatus.value = Result.Loading
        try {
            val request = StoreRequest(storeName, storeUsername, storeAddress, storeContact)
            val response = apiService.updateStore(request)
            _updateStoreStatus.value = Result.Success(response)
        } catch (e: HttpException) {
            val jsonString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            _updateStoreStatus.value = Result.Error(errorMessage ?: "An error occurred")
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