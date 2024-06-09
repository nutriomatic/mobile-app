package com.nutriomatic.app.presentation.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nutriomatic.app.data.pref.UserModel
import com.nutriomatic.app.data.remote.Result
import com.nutriomatic.app.data.remote.api.response.ProfileResponse
import com.nutriomatic.app.data.remote.repository.UserRepository
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: UserRepository) : ViewModel() {
    val detailProfile: LiveData<Result<ProfileResponse>> = repository.detailProfile

    init {
        getProfile()
    }

    fun getUserModel(): LiveData<UserModel> {
        return repository.getUserModel()
    }

    private fun getProfile() {
        viewModelScope.launch {
            repository.getProfile()
        }
    }
}