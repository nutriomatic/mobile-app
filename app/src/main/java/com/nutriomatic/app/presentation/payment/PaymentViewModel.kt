package com.nutriomatic.app.presentation.store

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nutriomatic.app.data.remote.Result
import com.nutriomatic.app.data.remote.api.response.AllTransactionsResponse
import com.nutriomatic.app.data.remote.api.response.BasicResponse
import com.nutriomatic.app.data.remote.api.response.ProductsResponse
import com.nutriomatic.app.data.remote.repository.ProductRepository
import com.nutriomatic.app.data.remote.repository.StoreRepository
import com.nutriomatic.app.data.remote.repository.TransactionRepository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class PaymentViewModel(
    private val productRepository: ProductRepository,
    private val storeRepository: StoreRepository,
    private val transactionRepository: TransactionRepository
) : ViewModel() {
    val productsStore: LiveData<Result<ProductsResponse>> = productRepository.productsStore
    val statusUploadProof: LiveData<Result<BasicResponse>> =
        transactionRepository.statusUploadProof
    val transactionByStoreIdResponse: LiveData<Result<AllTransactionsResponse>> =
        transactionRepository.getTransactionByStoreIdResponse

    fun getProductsByStore(storeId: String) {
        viewModelScope.launch {
            productRepository.getProductsByStore(storeId)
        }
    }

    fun getTransactionByStoreId(storeId: String) {
        viewModelScope.launch {
            transactionRepository.getTransactionByStoreId(storeId)
        }
    }

    fun uploadProofTransaction(
        file: MultipartBody.Part,
    ) {
        viewModelScope.launch {
            transactionRepository.uploadProofTransaction(file)
        }
    }

}