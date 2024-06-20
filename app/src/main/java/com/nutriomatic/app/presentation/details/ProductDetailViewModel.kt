package com.nutriomatic.app.presentation.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nutriomatic.app.data.remote.Result
import com.nutriomatic.app.data.remote.api.response.ProductByIdResponse
import com.nutriomatic.app.data.remote.api.response.StoreResponse
import com.nutriomatic.app.data.remote.repository.ProductRepository
import com.nutriomatic.app.data.remote.repository.StoreRepository
import kotlinx.coroutines.launch

class ProductDetailViewModel(
    private val repository: ProductRepository,
    private val storeRepository: StoreRepository
) : ViewModel() {
    val detailProduct: LiveData<Result<ProductByIdResponse>> = repository.detailProduct
    val storeDetail: LiveData<Result<StoreResponse>> = storeRepository.store

    fun getProductById(id: String) {
        viewModelScope.launch {
            repository.getProductById(id)
        }
    }

    fun getStoreById(id: String) {
        viewModelScope.launch {
            storeRepository.getStoreById(id)
        }
    }


}