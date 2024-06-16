package com.nutriomatic.app.presentation.admin

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nutriomatic.app.data.fake.FakeDataSource
import com.nutriomatic.app.data.fake.model.Transaction
import com.nutriomatic.app.data.remote.Result
import com.nutriomatic.app.data.remote.api.response.ProfileResponse
import com.nutriomatic.app.data.remote.repository.UserRepository
import kotlinx.coroutines.launch

class AdminHomeViewModel(private val userRepository: UserRepository) : ViewModel() {
    val transactions: List<Transaction> = FakeDataSource.generateFakeTransactions()
    val detailProfile: LiveData<Result<ProfileResponse>> = userRepository.detailProfile

    init {
        getProfile()
    }

    private fun getProfile() {
        viewModelScope.launch {
            userRepository.getProfile()
        }
    }
}