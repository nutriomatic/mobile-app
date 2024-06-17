package com.nutriomatic.app.data.remote.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.google.gson.Gson
import com.nutriomatic.app.data.pref.UserModel
import com.nutriomatic.app.data.pref.UserPreference
import com.nutriomatic.app.data.remote.Result
import com.nutriomatic.app.data.remote.api.request.LoginRequest
import com.nutriomatic.app.data.remote.api.request.RegisterRequest
import com.nutriomatic.app.data.remote.api.response.ClassificationCaloryResponse
import com.nutriomatic.app.data.remote.api.response.ErrorResponse
import com.nutriomatic.app.data.remote.api.response.ProfileResponse
import com.nutriomatic.app.data.remote.api.response.RegisterResponse
import com.nutriomatic.app.data.remote.api.response.UpdateProfileResponse
import com.nutriomatic.app.data.remote.api.retrofit.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService,
) {
    private val _registerStatus = MutableLiveData<Result<RegisterResponse>>()
    val registerStatus: LiveData<Result<RegisterResponse>> = _registerStatus

    //    Login Data (token, role)
    private val _loginData = MutableLiveData<Result<List<String>>>()
    val loginData: LiveData<Result<List<String>>> = _loginData

    private val _detailProfile = MutableLiveData<Result<ProfileResponse>>()
    val detailProfile: LiveData<Result<ProfileResponse>> = _detailProfile

    private val _updateProfileResponse = MutableLiveData<Result<UpdateProfileResponse>>()
    val updateProfileResponse: LiveData<Result<UpdateProfileResponse>> = _updateProfileResponse

    private val _detailClassification = MutableLiveData<Result<ClassificationCaloryResponse>>()
    val detailClassification: LiveData<Result<ClassificationCaloryResponse>> = _detailClassification

    suspend fun register(name: String, email: String, password: String) {
        _registerStatus.value = Result.Loading
        try {
            val request = RegisterRequest(name, email, password)
            val response = apiService.register(request)
            _registerStatus.value = Result.Success(response)
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            _registerStatus.value = Result.Error(errorMessage ?: "An error occurred")
        }
    }


    suspend fun login(email: String, password: String) {
        _loginData.value = Result.Loading
        try {
            val request = LoginRequest(email, password)
            val response = apiService.login(request)
            _loginData.value = Result.Success(listOf(response.token, response.role))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            _loginData.value = Result.Error(errorMessage ?: "An error occurred")
        }
    }


    suspend fun getProfile() {
        _detailProfile.value = Result.Loading
        try {
//            val request = LoginRequest(email, password)
            val response = apiService.getProfile()
            _detailProfile.value = Result.Success(response)
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            _detailProfile.value = Result.Error(errorMessage ?: "An error occurred")
        }
    }

    suspend fun saveUserModel() {
        try {
            val response = apiService.getProfile()
            with(response.user) {
                userPreference.saveUserModel(
                    UserModel(
                        id = id,
                        name = name,
                        email = email,
                        gender = gender,
                        role = role,
                        telp = telp,
                        profpic = profpic,
                        birthdate = birthdate,
                        place = place,
                        height = height,
                        weight = weight,
                        weightGoal = weightGoal,
                        hgId = hgId,
                        hgType = hgType,
                        hgDesc = hgDesc,
                        alId = alId,
                        alType = alType,
                        alDesc = alDesc,
                        alValue = alValue
                    )
                )
            }
        } catch (e: HttpException) {
            val jsonString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
        }
    }

    suspend fun updateProfile(
        name: RequestBody,
        email: RequestBody,
        gender: RequestBody,
        telephone: RequestBody,
        birthdate: RequestBody,
        height: RequestBody,
        weight: RequestBody,
        weightGoal: RequestBody,
        alType: RequestBody,
        hgType: RequestBody,
        photo: MultipartBody.Part? = null,
    ) {
        _updateProfileResponse.value = Result.Loading
        try {
            val response = apiService.updateProfile(
                name,
                email,
                gender,
                telephone,
                birthdate,
                height,
                weight,
                weightGoal,
                alType,
                hgType,
                photo
            )

            _updateProfileResponse.value = Result.Success(response)
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            _updateProfileResponse.value = Result.Error(errorMessage ?: "An error occurred")
        }
    }

    fun getUserModel(): LiveData<UserModel> {
        return userPreference.getUserModel().asLiveData()
    }

//    suspend fun saveToken(token: String) {
//        userPreference.saveToken(token)
//    }

    suspend fun saveLoginData(token: String, email: String, role: String) {
        userPreference.saveLoginData(token, email, role)
    }

    fun getToken(): LiveData<String?> {
        return userPreference.getToken().asLiveData()
    }

    fun getTokenAndRole(): LiveData<List<String?>> {
        return userPreference.getTokenAndRole().asLiveData()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    suspend fun getClassification() {
        _detailClassification.value = Result.Loading
        try {
            val response = apiService.getClassification()
            _detailClassification.value = Result.Success(response)
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            _detailClassification.value = Result.Error(errorMessage ?: "An error occurred")
        }
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService,
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference, apiService)
            }.also { instance = it }
    }
}