package com.nutriomatic.app.presentation.store

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nutriomatic.app.data.remote.Result
import com.nutriomatic.app.data.remote.api.response.ProductsResponse
import com.nutriomatic.app.data.remote.repository.ProductRepository
import kotlinx.coroutines.launch

class StoreViewModel(private val repository: ProductRepository) : ViewModel() {
    val products: LiveData<Result<ProductsResponse>> = repository.products

    init {
        getProducts()
    }

    private fun getProducts() {
        viewModelScope.launch {
            repository.getProducts()
        }
    }

}