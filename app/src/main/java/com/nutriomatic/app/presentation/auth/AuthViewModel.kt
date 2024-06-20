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
    val loginData: LiveData<Result<List<String>>> = userRepository.loginData
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

    fun saveLoginData(token: String, email: String, role: String) {
        viewModelScope.launch {
//            saveToken(token)
            userRepository.saveLoginData(token, email, role)
//            userRepository.saveUserModel()
        }
    }

    fun getTokenAndRole(): LiveData<List<String?>> {
        return userRepository.getTokenAndRole()
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }
}