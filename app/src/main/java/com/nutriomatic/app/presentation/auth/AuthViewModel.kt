package com.nutriomatic.app.presentation.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nutriomatic.app.data.pref.UserModel
import com.nutriomatic.app.data.remote.Result
import com.nutriomatic.app.data.remote.api.response.RegisterResponse
import com.nutriomatic.app.data.remote.repository.UserRepository
import kotlinx.coroutines.launch

class AuthViewModel(private val userRepository: UserRepository) : ViewModel() {
    val loginToken: LiveData<Result<String>> = userRepository.loginToken
    val registerStatus: LiveData<Result<RegisterResponse>> = userRepository.registerStatus

    fun login(email: String, password: String) {
        viewModelScope.launch {
            userRepository.login(email, password)
        }
    }

    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            userRepository.register(username, email, password)
        }
    }

    fun saveTokenAndUserModel(token: String) {
        viewModelScope.launch {
            saveToken(token)
            userRepository.saveUserModel()
        }
    }

    fun saveUserModel() {
        viewModelScope.launch {
            userRepository.saveUserModel()
        }
    }

    fun getUserModel(): LiveData<UserModel> {
        return userRepository.getUserModel()
    }

    suspend fun saveToken(token: String) {
        userRepository.saveToken(token)
    }

    fun getToken(): LiveData<String?> {
        return userRepository.getToken()
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }
}