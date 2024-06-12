package com.nutriomatic.app.presentation.store

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nutriomatic.app.data.remote.Result
import com.nutriomatic.app.data.remote.api.response.BasicResponse
import com.nutriomatic.app.data.remote.api.response.ProductsResponse
import com.nutriomatic.app.data.remote.api.response.StoreResponse
import com.nutriomatic.app.data.remote.repository.ProductRepository
import com.nutriomatic.app.data.remote.repository.StoreRepository
import kotlinx.coroutines.launch

class StoreViewModel(
    private val repository: ProductRepository,
    private val storeRepository: StoreRepository
) : ViewModel() {
    val products: LiveData<Result<ProductsResponse>> = repository.products
    val createStoreResponse: LiveData<Result<BasicResponse>> = storeRepository.createStoreResponse
    val store: LiveData<Result<StoreResponse>> = storeRepository.store

    init {
        getStore()
        getProducts()
    }

    fun createStore(
        storeName: String,
        storeUsername: String,
        storeAddress: String,
        storeContact: String,
    ) {
        viewModelScope.launch {
            storeRepository.createStore(storeName, storeUsername, storeAddress, storeContact)
        }
    }

    private fun getStore() {
        viewModelScope.launch {
            storeRepository.getStore()
        }
    }

    private fun getProducts() {
        viewModelScope.launch {
            repository.getProducts()
        }
    }

}