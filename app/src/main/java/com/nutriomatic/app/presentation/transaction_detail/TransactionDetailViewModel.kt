package com.nutriomatic.app.presentation.transaction_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nutriomatic.app.data.remote.Result
import com.nutriomatic.app.data.remote.api.response.BasicResponse
import com.nutriomatic.app.data.remote.api.response.GetTransactionByIdResponse
import com.nutriomatic.app.data.remote.repository.TransactionRepository
import kotlinx.coroutines.launch

class TransactionDetailViewModel(private val transactionRepository: TransactionRepository) :
    ViewModel() {
    val transaction: LiveData<Result<GetTransactionByIdResponse>> =
        transactionRepository.getTransactionByIdResponse

    val updateTransactionResponse: LiveData<Result<BasicResponse>> =
        transactionRepository.updateTransactionStatusResponse

    fun getTransactionById(id: String) {
        viewModelScope.launch {
            transactionRepository.getTransactionById(id)
        }
    }

    fun updateTransaction(id: String, status: String) {
        viewModelScope.launch {
            transactionRepository.updateTransaction(id, status)
        }
    }
}