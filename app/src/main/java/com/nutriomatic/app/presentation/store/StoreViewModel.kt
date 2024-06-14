package com.nutriomatic.app.presentation.store

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.nutriomatic.app.data.remote.Result
import com.nutriomatic.app.data.remote.api.response.BasicResponse
import com.nutriomatic.app.data.remote.api.response.ProductsItem
import com.nutriomatic.app.data.remote.api.response.ProductsResponse
import com.nutriomatic.app.data.remote.api.response.StoreResponse
import com.nutriomatic.app.data.remote.repository.ProductRepository
import com.nutriomatic.app.data.remote.repository.StoreRepository
import kotlinx.coroutines.launch

class StoreViewModel(
    private val productRepository: ProductRepository,
    private val storeRepository: StoreRepository
) : ViewModel() {
    val productsStore: LiveData<Result<ProductsResponse>> = productRepository.productsStore
    val createStoreResponse: LiveData<Result<BasicResponse>> = storeRepository.createStoreResponse
    val updateStoreStatus: LiveData<Result<BasicResponse>> = storeRepository.updateStoreResponse
    val store: LiveData<Result<StoreResponse>> = storeRepository.store

    fun getUserProductsPaging(storeId: String): LiveData<PagingData<ProductsItem>> = productRepository.getUserProductsPaging(storeId = storeId)

    init {
        getStore()
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

    fun updateStore(
        storeName: String,
        storeAddress: String,
        storeContact: String,
    ) {
        viewModelScope.launch {
            storeRepository.updateStore(storeName, storeAddress, storeContact)
        }
    }

    fun getStore() {
        viewModelScope.launch {
            storeRepository.getStore()
        }
    }

    fun getProductsByStore(storeId: String) {
        viewModelScope.launch {
            productRepository.getProductsByStore(storeId)
        }
    }

}