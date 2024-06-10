package com.nutriomatic.app.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nutriomatic.app.data.remote.Result
import com.nutriomatic.app.data.remote.api.response.ProductsResponse
import com.nutriomatic.app.data.remote.api.response.ProfileResponse
import com.nutriomatic.app.data.remote.repository.ProductRepository
import com.nutriomatic.app.data.remote.repository.UserRepository
import kotlinx.coroutines.launch

class HomeViewModel(
    private val userRepository: UserRepository,
    private val productRepository: ProductRepository
) : ViewModel() {
    val productsAdvertise: LiveData<Result<ProductsResponse>> = productRepository.products
    val detailProfile: LiveData<Result<ProfileResponse>> = userRepository.detailProfile


    init {
        getProducts()
        getProfile()
    }

    private fun getProducts() {
        viewModelScope.launch {
            productRepository.getProducts()
        }
    }

    private fun getProfile() {
        viewModelScope.launch {
            userRepository.getProfile()
        }
    }
}