package com.nutriomatic.app.presentation.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nutriomatic.app.data.remote.Result
import com.nutriomatic.app.data.remote.api.response.ProductByIdResponse
import com.nutriomatic.app.data.remote.repository.ProductRepository
import kotlinx.coroutines.launch

class ProductDetailViewModel(private val repository: ProductRepository) : ViewModel() {
    val detailProduct: LiveData<Result<ProductByIdResponse>> = repository.detailProduct

    fun getProductById(id: String) {
        viewModelScope.launch {
            repository.getProductById(id)
        }
    }
}