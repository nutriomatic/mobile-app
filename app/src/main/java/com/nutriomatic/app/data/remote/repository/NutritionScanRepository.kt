package com.nutriomatic.app.data.remote.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.google.gson.Gson
import com.nutriomatic.app.data.remote.Result
import com.nutriomatic.app.data.remote.api.response.BasicResponse
import com.nutriomatic.app.data.remote.api.response.ErrorResponse
import com.nutriomatic.app.data.remote.api.response.GetNutritionScanByIdResponse
import com.nutriomatic.app.data.remote.api.response.GetNutritionScanByUserIdResponse
import com.nutriomatic.app.data.remote.api.response.NutritionScan
import com.nutriomatic.app.data.remote.api.retrofit.ApiService
import com.nutriomatic.app.data.remote.paging.NutritionScanPagingSource
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException

class NutritionScanRepository(private val apiService: ApiService) {
    private val _createNutritionScanResponse = MutableLiveData<Result<BasicResponse>>()
    val createNutritionScanResponse: LiveData<Result<BasicResponse>> = _createNutritionScanResponse

    private val _getNutritionScanByIdResponse =
        MutableLiveData<Result<GetNutritionScanByIdResponse>>()
    val getNutritionScanByIdResponse: LiveData<Result<GetNutritionScanByIdResponse>> =
        _getNutritionScanByIdResponse

    private val _getNutritionScanByUserIdResponse =
        MutableLiveData<Result<GetNutritionScanByUserIdResponse>>()
    val getNutritionScanByUserIdResponse: LiveData<Result<GetNutritionScanByUserIdResponse>> =
        _getNutritionScanByUserIdResponse

    suspend fun createNutritionScan(name: RequestBody, photo: MultipartBody.Part) {
        _createNutritionScanResponse.value = Result.Loading
        try {
            val response = apiService.createNutritionScan(name, photo)
            _createNutritionScanResponse.value = Result.Success(response)
        } catch (e: HttpException) {
            val jsonString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            _createNutritionScanResponse.value = Result.Error(errorMessage ?: "An error occurred")
        }
    }

    suspend fun getNutritionScanById(id: String) {
        _getNutritionScanByIdResponse.value = Result.Loading
        try {
            val response = apiService.getNutritionScanById(id)
            _getNutritionScanByIdResponse.value = Result.Success(response)
        } catch (e: HttpException) {
            val jsonString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            _getNutritionScanByIdResponse.value = Result.Error(errorMessage ?: "An error occurred")
        }
    }

    fun getNutritionScanByUserIdPaging(): LiveData<PagingData<NutritionScan>> {
        return Pager(
            config = PagingConfig(pageSize = 6),
            pagingSourceFactory = { NutritionScanPagingSource(apiService) }
        ).liveData
    }

    companion object {
        @Volatile
        private var instance: NutritionScanRepository? = null
        fun getInstance(apiService: ApiService): NutritionScanRepository =
            instance ?: synchronized(this) {
                instance ?: NutritionScanRepository(apiService)
            }.also { instance = it }
    }
}