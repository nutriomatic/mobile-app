package com.nutriomatic.app.presentation.store

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nutriomatic.app.data.remote.Result
import com.nutriomatic.app.data.remote.api.response.ProductsResponse
import com.nutriomatic.app.data.remote.repository.ProductRepository
import com.nutriomatic.app.data.remote.repository.StoreRepository
import kotlinx.coroutines.launch

class PaymentViewModel(
    private val productRepository: ProductRepository,
    private val storeRepository: StoreRepository
) : ViewModel() {
    val productsStore: LiveData<Result<ProductsResponse>> = productRepository.productsStore

    fun getProductsByStore(storeId: String) {
        viewModelScope.launch {
            productRepository.getProductsByStore(storeId)
        }
    }

}