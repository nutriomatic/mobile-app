package com.nutriomatic.app.presentation.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nutriomatic.app.data.remote.Result
import com.nutriomatic.app.data.remote.api.response.RegisterResponse
import com.nutriomatic.app.data.remote.repository.UserRepository
import kotlinx.coroutines.launch

class RegisterViewModel(private val userRepository: UserRepository) : ViewModel() {
    val registerStatus: LiveData<Result<RegisterResponse>> = userRepository.registerStatus

    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            userRepository.register(username, email, password)
        }
    }
}