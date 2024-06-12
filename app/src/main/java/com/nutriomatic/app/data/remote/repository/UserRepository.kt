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

    private val _loginToken = MutableLiveData<Result<String>>()
    val loginToken: LiveData<Result<String>> = _loginToken

    private val _detailProfile = MutableLiveData<Result<ProfileResponse>>()
    val detailProfile: LiveData<Result<ProfileResponse>> = _detailProfile

    private val _updateProfileResponse = MutableLiveData<Result<UpdateProfileResponse>>()
    val updateProfileResponse: LiveData<Result<UpdateProfileResponse>> = _updateProfileResponse

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
        _loginToken.value = Result.Loading
        try {
            val request = LoginRequest(email, password)
            val response = apiService.login(request)
            _loginToken.value = Result.Success(response.token)
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            _loginToken.value = Result.Error(errorMessage ?: "An error occurred")
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

    suspend fun saveTokenAndEmail(token: String, email: String) {
        userPreference.saveTokenAndEmail(token, email)
    }

    fun getToken(): LiveData<String?> {
        return userPreference.getToken().asLiveData()
    }

    suspend fun logout() {
        userPreference.logout()
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