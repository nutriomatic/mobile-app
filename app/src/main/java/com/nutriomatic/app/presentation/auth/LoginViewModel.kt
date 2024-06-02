package com.nutriomatic.app.presentation.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nutriomatic.app.data.pref.UserModel
import com.nutriomatic.app.data.remote.Result
import com.nutriomatic.app.data.remote.api.response.LoginResult
import com.nutriomatic.app.data.remote.repository.UserRepository
import kotlinx.coroutines.launch


class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {
    val dataLogin: LiveData<Result<LoginResult>> = userRepository.dataUser

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            userRepository.saveSession(user)
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            userRepository.login(email, password)
        }
    }
}