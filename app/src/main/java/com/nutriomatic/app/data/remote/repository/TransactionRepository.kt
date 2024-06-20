package com.nutriomatic.app.data.remote.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.google.gson.Gson
import com.nutriomatic.app.data.pref.UserPreference
import com.nutriomatic.app.data.remote.Result
import com.nutriomatic.app.data.remote.api.request.CreateTransactionRequest
import com.nutriomatic.app.data.remote.api.request.UpdateTransactionRequest
import com.nutriomatic.app.data.remote.api.response.AllTransactionsResponse
import com.nutriomatic.app.data.remote.api.response.BasicResponse
import com.nutriomatic.app.data.remote.api.response.ErrorResponse
import com.nutriomatic.app.data.remote.api.response.GetTransactionByIdResponse
import com.nutriomatic.app.data.remote.api.response.Transaction
import com.nutriomatic.app.data.remote.api.retrofit.ApiService
import com.nutriomatic.app.data.remote.paging.AllTransactionPagingSource
import okhttp3.MultipartBody
import retrofit2.HttpException

class TransactionRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService,
) {
    private val _statusCreateTransaction = MutableLiveData<Result<BasicResponse>>()
    val statusCreateTransaction: LiveData<Result<BasicResponse>> = _statusCreateTransaction

    private val _statusUploadProof = MutableLiveData<Result<BasicResponse>>()
    val statusUploadProof: LiveData<Result<BasicResponse>> = _statusUploadProof

    private val _updateTransactionStatusResponse = MutableLiveData<Result<BasicResponse>>()
    val updateTransactionStatusResponse: LiveData<Result<BasicResponse>> =
        _updateTransactionStatusResponse

    private val _getTransactionByIdResponse = MutableLiveData<Result<GetTransactionByIdResponse>>()
    val getTransactionByIdResponse: LiveData<Result<GetTransactionByIdResponse>> =
        _getTransactionByIdResponse

    private val _getTransactionByStoreIdResponse =
        MutableLiveData<Result<AllTransactionsResponse>>()
    val getTransactionByStoreIdResponse: LiveData<Result<AllTransactionsResponse>> =
        _getTransactionByStoreIdResponse


    suspend fun createTransaction(productId: String) {
        _statusCreateTransaction.value = Result.Loading
        try {
            val request = CreateTransactionRequest("default")
            val response =
                apiService.createTransaction(productId, request)
            _statusCreateTransaction.value = Result.Success(response)
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            _statusCreateTransaction.value = Result.Error(errorMessage ?: "An error occurred")
        }
    }

    fun getAllTransactionPaging(): LiveData<PagingData<Transaction>> {
        return Pager(
            config = PagingConfig(pageSize = 6),
            pagingSourceFactory = { AllTransactionPagingSource(apiService) }
        ).liveData
    }

    suspend fun getTransactionById(id: String) {
        _getTransactionByIdResponse.value = Result.Loading
        try {
            val response = apiService.getTransactionById(id)
            _getTransactionByIdResponse.value = Result.Success(response)
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            _getTransactionByIdResponse.value = Result.Error(errorMessage ?: "An error occurred")
        }
    }

    suspend fun getTransactionByStoreId(id: String) {
        _getTransactionByStoreIdResponse.value = Result.Loading
        try {
            val response = apiService.getTransactionByStoreId(id)
            _getTransactionByStoreIdResponse.value = Result.Success(response)
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            _getTransactionByStoreIdResponse.value =
                Result.Error(errorMessage ?: "An error occurred")
        }
    }

    suspend fun updateTransaction(id: String, status: String) {
        _updateTransactionStatusResponse.value = Result.Loading
        try {
            val request = UpdateTransactionRequest(status)
            val response = apiService.updateTransactionStatus(id, request)
            _updateTransactionStatusResponse.value = Result.Success(response)
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            _updateTransactionStatusResponse.value =
                Result.Error(errorMessage ?: "An error occurred")
        }

    }

    suspend fun uploadProofTransaction(file: MultipartBody.Part) {
        _statusUploadProof.value = Result.Loading
        try {
            val response = apiService.uploadProofTransaction(file)
            _statusUploadProof.value = Result.Success(response)
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            _statusUploadProof.value =
                Result.Error(errorMessage ?: "An error occurred")
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