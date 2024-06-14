package com.nutriomatic.app.data.remote.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.google.gson.Gson
import com.nutriomatic.app.data.remote.ProductPagingSource
import com.nutriomatic.app.data.pref.UserPreference
import com.nutriomatic.app.data.remote.Result
import com.nutriomatic.app.data.remote.api.response.BasicResponse
import com.nutriomatic.app.data.remote.api.response.CreateProductResponse
import com.nutriomatic.app.data.remote.api.response.ErrorResponse
import com.nutriomatic.app.data.remote.api.response.ProductAdvertiseResponse
import com.nutriomatic.app.data.remote.api.response.ProductByIdResponse
import com.nutriomatic.app.data.remote.api.response.ProductsItem
import com.nutriomatic.app.data.remote.api.response.ProductsResponse
import com.nutriomatic.app.data.remote.api.retrofit.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException

class ProductRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService,
) {
    private val _productsStore = MutableLiveData<Result<ProductsResponse>>()
    val productsStore: LiveData<Result<ProductsResponse>> = _productsStore

    private val _productsAdvertise = MutableLiveData<Result<ProductAdvertiseResponse>>()
    val productsAdvertise: LiveData<Result<ProductAdvertiseResponse>> = _productsAdvertise

    private val _statusCreateProduct = MutableLiveData<Result<CreateProductResponse>>()
    val statusCreateProduct: LiveData<Result<CreateProductResponse>> = _statusCreateProduct

    private val _statusUpdateProduct = MutableLiveData<Result<BasicResponse>>()
    val statusUpdateProduct: LiveData<Result<BasicResponse>> = _statusUpdateProduct

    private val _statusAdvertiseProduct = MutableLiveData<Result<BasicResponse>>()
    val statusAdvertiseProduct: LiveData<Result<BasicResponse>> = _statusAdvertiseProduct

    private val _detailProduct = MutableLiveData<Result<ProductByIdResponse>>()
    val detailProduct: LiveData<Result<ProductByIdResponse>> = _detailProduct


//    suspend fun getProducts() {
//        _products.value = Result.Loading
//        try {
//            val response = apiService.getProducts()
//            _products.value = Result.Success(response)
//        } catch (e: HttpException) {
//            val jsonInString = e.response()?.errorBody()?.string()
//            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
//            val errorMessage = errorBody.message
//            _products.value = Result.Error(errorMessage ?: "An error occurred")
//        }
//    }

    suspend fun getProductsByStore(storeId: String) {
        _productsStore.value = Result.Loading
        try {
            val response = apiService.getProductsStore(storeId)
            _productsStore.value = Result.Success(response)
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            _productsStore.value = Result.Error(errorMessage ?: "An error occurred")
        }
    }

    suspend fun getProductsAdvertise() {
        _productsAdvertise.value = Result.Loading
        try {
            val response = apiService.getProductsAdvertise()
            _productsAdvertise.value = Result.Success(response)
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            _productsAdvertise.value = Result.Error(errorMessage ?: "An error occurred")
        }
    }

    fun getProductsPaging(): LiveData<PagingData<ProductsItem>> {
        return Pager(
            config = PagingConfig(pageSize = 4),
            pagingSourceFactory = { ProductPagingSource(apiService) }
        ).liveData
    }

    suspend fun createProduct(
        productName: RequestBody,
        productPrice: RequestBody,
        productDesc: RequestBody,
        productIsshow: RequestBody,
        productLemakTotal: RequestBody,
        productProtein: RequestBody,
        productKarbohidrat: RequestBody,
        productGaram: RequestBody,
        productGrade: RequestBody,
        productServingSize: RequestBody,
        ptName: RequestBody,
        body: MultipartBody.Part,
    ) {
        _statusCreateProduct.value = Result.Loading
        try {
            val response = apiService.createProduct(
                productName,
                productPrice,
                productDesc,
                productIsshow,
                productLemakTotal,
                productProtein,
                productKarbohidrat,
                productGaram,
                productGrade,
                productServingSize,
                ptName,
                body
            )

            _statusCreateProduct.value = Result.Success(response)
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            _statusCreateProduct.value = Result.Error(errorMessage ?: "An error occurred")
        }
    }

    suspend fun updateProduct(
        id: String,
        productName: RequestBody,
//        productPrice: RequestBody,
        productDesc: RequestBody,
        productLemakTotal: RequestBody,
        productProtein: RequestBody,
        productKarbohidrat: RequestBody,
        productGaram: RequestBody,
        productGrade: RequestBody,
        productServingSize: RequestBody,
        ptName: RequestBody,
        body: MultipartBody.Part,
    ) {
        _statusUpdateProduct.value = Result.Loading
        try {
            val response = apiService.updateProductById(
                id,
                productName,
//                productPrice,
                productDesc,
                productLemakTotal,
                productProtein,
                productKarbohidrat,
                productGaram,
                productGrade,
                productServingSize,
                ptName,
                body
            )

            _statusUpdateProduct.value = Result.Success(response)
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            _statusUpdateProduct.value = Result.Error(errorMessage ?: "An error occurred")
        }
    }

    suspend fun getProductById(id: String) {
        _detailProduct.value = Result.Loading
        try {
            val response =
                apiService.getProductById(id)
            _detailProduct.value = Result.Success(response)
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            _detailProduct.value = Result.Error(errorMessage ?: "An error occurred")
        }
    }

    suspend fun advertiseProduct(id: String) {
        _statusAdvertiseProduct.value = Result.Loading
        try {
            val response =
                apiService.advertiseProduct(id)
            _statusAdvertiseProduct.value = Result.Success(response)
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            _statusAdvertiseProduct.value = Result.Error(errorMessage ?: "An error occurred")
        }
    }


    companion object {
        @Volatile
        private var instance: ProductRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService,
        ): ProductRepository =
            instance ?: synchronized(this) {
                instance ?: ProductRepository(userPreference, apiService)
            }.also { instance = it }
    }
}