package com.nutriomatic.app.data.remote.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.nutriomatic.app.data.pref.UserPreference
import com.nutriomatic.app.data.remote.Result
import com.nutriomatic.app.data.remote.api.response.BasicResponse
import com.nutriomatic.app.data.remote.api.response.ErrorResponse
import com.nutriomatic.app.data.remote.api.retrofit.ApiService
import retrofit2.HttpException

class TransactionRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService,
) {

    private val _statusCreateTransaction = MutableLiveData<Result<BasicResponse>>()
    val statusCreateTransaction: LiveData<Result<BasicResponse>> = _statusCreateTransaction


    suspend fun createTransaction(product_id: String) {
        _statusCreateTransaction.value = Result.Loading
        try {
            val response =
                apiService.createTransaction(product_id)
            _statusCreateTransaction.value = Result.Success(response)
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            _statusCreateTransaction.value = Result.Error(errorMessage ?: "An error occurred")
        }
    }

    companion object {
        @Volatile
        private var instance: TransactionRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService,
        ): TransactionRepository =
            instance ?: synchronized(this) {
                instance ?: TransactionRepository(userPreference, apiService)
            }.also { instance = it }
    }
}