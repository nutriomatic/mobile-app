package com.nutriomatic.app.presentation.admin

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.nutriomatic.app.data.remote.Result
import com.nutriomatic.app.data.remote.api.response.ProfileResponse
import com.nutriomatic.app.data.remote.api.response.Transaction
import com.nutriomatic.app.data.remote.repository.TransactionRepository
import com.nutriomatic.app.data.remote.repository.UserRepository
import kotlinx.coroutines.launch

class AdminHomeViewModel(
    private val userRepository: UserRepository,
    private val transactionRepository: TransactionRepository,
) : ViewModel() {
    val detailProfile: LiveData<Result<ProfileResponse>> = userRepository.detailProfile

    fun getAllTransactionsPaging(): LiveData<PagingData<Transaction>> =
        transactionRepository.getAllTransactionPaging()

    init {
        getProfile()
    }

    private fun getProfile() {
        viewModelScope.launch {
            userRepository.getProfile()
        }
    }
}