package com.nutriomatic.app.presentation.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nutriomatic.app.data.remote.Result
import com.nutriomatic.app.data.remote.api.response.ProfileResponse
import com.nutriomatic.app.data.remote.api.response.UpdateProfileResponse
import com.nutriomatic.app.data.remote.repository.UserRepository
import com.nutriomatic.app.presentation.helper.util.createRequestBodyInt
import com.nutriomatic.app.presentation.helper.util.createRequestBodyText
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class ProfileViewModel(private val repository: UserRepository) : ViewModel() {
    val detailProfile: LiveData<Result<ProfileResponse>> = repository.detailProfile
    val updateProfileResponse: LiveData<Result<UpdateProfileResponse>> =
        repository.updateProfileResponse

    fun getProfile() {
        viewModelScope.launch {
            repository.getProfile()
        }
    }

    fun updateProfile(
        name: String,
        email: String,
        gender: Int,
        telephone: String,
        birthdate: String,
        height: Int,
        weight: Int,
        weightGoal: Int,
        alType: Int,
        hgType: Int,
        photo: MultipartBody.Part? = null,
    ) {
        viewModelScope.launch {
            val nameBody = createRequestBodyText(name)
            val emailBody = createRequestBodyText(email)
            val genderBody = createRequestBodyInt(gender)
            val telephoneBody = createRequestBodyText(telephone)
            val birthdateBody = createRequestBodyText(birthdate)
            val heightBody = createRequestBodyInt(height)
            val weightBody = createRequestBodyInt(weight)
            val weightGoalBody = createRequestBodyInt(weightGoal)
            val alTypeBody = createRequestBodyInt(alType)
            val hgTypeBody = createRequestBodyInt(hgType)

            repository.updateProfile(
                nameBody,
                emailBody,
                genderBody,
                telephoneBody,
                birthdateBody,
                heightBody,
                weightBody,
                weightGoalBody,
                alTypeBody,
                hgTypeBody,
                photo
            )
        }
    }
}