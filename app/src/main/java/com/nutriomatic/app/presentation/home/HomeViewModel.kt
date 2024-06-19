package com.nutriomatic.app.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.nutriomatic.app.data.remote.Result
import com.nutriomatic.app.data.remote.api.response.ClassificationCaloryResponse
import com.nutriomatic.app.data.remote.api.response.ProductAdvertiseResponse
import com.nutriomatic.app.data.remote.api.response.ProductsItem
import com.nutriomatic.app.data.remote.api.response.ProfileResponse
import com.nutriomatic.app.data.remote.repository.ProductRepository
import com.nutriomatic.app.data.remote.repository.UserRepository
import kotlinx.coroutines.launch

class HomeViewModel(
    private val userRepository: UserRepository,
    private val productRepository: ProductRepository,
) : ViewModel() {
    val detailProfile: LiveData<Result<ProfileResponse>> = userRepository.detailProfile
    val detailClassification: LiveData<Result<ClassificationCaloryResponse>> =
        userRepository.detailClassification

    val searchProductsAdvertise: LiveData<Result<ProductAdvertiseResponse>> =
        productRepository.searchProductsAdvertise

    init {
        getClassification()
        getProfile()
    }


    fun getProductsAdvertisePaging(): LiveData<PagingData<ProductsItem>> =
        productRepository.getAdvertisedProductsPaging()

    fun getSearchProductsAdvertise(query: String) {
        viewModelScope.launch {
            productRepository.getSearchProductsAdvertise(query)
        }
    }

    private fun getProfile() {
        viewModelScope.launch {
            userRepository.getProfile()
        }
    }

    private fun getClassification() {
        viewModelScope.launch {
            userRepository.getClassification()
        }
    }
}